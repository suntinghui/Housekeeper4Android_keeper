package com.housekeeper.activity.keeper;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.Paginable;
import com.ares.house.dto.app.ReserveListAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.KeeperReserveLayout;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.AdapterUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/31/15.
 */
public class KeeperReserveListActivity extends BaseActivity implements View.OnClickListener {

    private CustomNetworkImageView headImageView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView moneyTextView;

    private TextView noDataTextView;
    private LinearLayout contentLayout;

    private List<ReserveListAppDto> list = new ArrayList<ReserveListAppDto>();

    private WaitLeaseListAppDto appDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_reserve_list);

        appDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.requestReserveList();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("意向看房");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.addressTextView = (TextView) this.findViewById(R.id.addressTextView);
        this.cityTextView = (TextView) this.findViewById(R.id.cityTextView);
        this.moneyTextView = (TextView) this.findViewById(R.id.moneyTextView);

        this.noDataTextView = (TextView) this.findViewById(R.id.noDataTextView);
        this.contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
    }

    public void requestReserveList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", appDto.getHouseId() + "");
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

                        list = dto.getData().getList();

                        responseReserveList();

                    } else {
                        Toast.makeText(KeeperReserveListActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void responseReserveList() {
        this.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        this.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        this.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        this.headImageView.setImageUrl(Constants.HOST_IP + appDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        this.addressTextView.setText(appDto.getCommunity() + "  " + appDto.getHouseType());
        this.cityTextView.setText(appDto.getAreaStr() + " • " + appDto.getLeaseType() + " • " + appDto.getSize());
        this.moneyTextView.setText(appDto.getLeaseMonthMoney());

        if (list.isEmpty()) {
            this.noDataTextView.setVisibility(View.VISIBLE);
            this.contentLayout.setVisibility(View.GONE);
        } else {
            this.noDataTextView.setVisibility(View.GONE);
            this.contentLayout.setVisibility(View.VISIBLE);
        }

        this.contentLayout.removeAllViews();
        for (ReserveListAppDto dto : list) {
            KeeperReserveLayout layout = new KeeperReserveLayout(this);
            layout.setData(dto);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, AdapterUtil.dip2px(this, 20), 0, 0);
            this.contentLayout.addView(layout, params);
        }
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
