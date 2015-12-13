package com.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.EarningsListAppDto;
import com.housekeeper.activity.view.YesterdayEarningsAdapter;
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
 * Created by sth on 12/10/15.
 */
public class YesterdayEarningsActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView = null;
    private YesterdayEarningsAdapter adapter = null;
    private List<EarningsListAppDto> mList = new ArrayList<EarningsListAppDto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_yesterday_earnings);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.requestUserEarningList();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("昨日收益");

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new YesterdayEarningsAdapter(this);
        listView.setAdapter(adapter);

        ActivityUtil.setEmptyView(this, listView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUserEarningList();
            }
        });
    }

    public void requestUserEarningList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", "2");

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_EARNINGS_LIST, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, EarningsListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<List<EarningsListAppDto>> dto = null;

                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        mList = dto.getData();

                        adapter.setData(mList);

                    } else {
                        Toast.makeText(YesterdayEarningsActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求请稍候...");
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
