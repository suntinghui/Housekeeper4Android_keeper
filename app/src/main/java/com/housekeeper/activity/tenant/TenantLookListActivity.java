package com.housekeeper.activity.tenant;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.Paginable;
import com.ares.house.dto.app.ReserveListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.keeper.KeeperRentRecordListActivity;
import com.housekeeper.activity.view.LookListAdapter;
import com.housekeeper.activity.view.PublishRoommateAdapter;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/29/15.
 */
public class TenantLookListActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView = null;
    private LookListAdapter adapter = null;
    private List<ReserveListAppDto> mList = new ArrayList<ReserveListAppDto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_look_list);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.requestPublishList();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("看房记录");

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new LookListAdapter(this);
        listView.setAdapter(adapter);

        ActivityUtil.setEmptyView(this, listView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPublishList();
            }
        });
    }

    public void requestPublishList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        // tempMap.put("houseId", this.getIntent().getStringExtra("houseId"));
        tempMap.put("pageNo", "1");
        tempMap.put("pageSize", Integer.MAX_VALUE + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_RESERVE_LIST, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, ReserveListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<ReserveListAppDto>> dto = null;

                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        mList = dto.getData().getList();

                        adapter.setData(mList);

                    } else {
                        Toast.makeText(TenantLookListActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;
        }
    }

}
