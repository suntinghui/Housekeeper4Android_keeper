package com.housekeeper.activity.landlord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.LandlordAppDto;
import com.ares.house.dto.app.LandlordHouseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.WithdrawalsActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.LandlordHomeLayout;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.AdapterUtil;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * Created by sth on 9/10/15.
 */
public class LandlordHomeActivity extends BaseActivity implements View.OnClickListener {

    private CustomNetworkImageView logoImageView;
    private TextView balanceTextView;
    private TextView withdrawTextView;

    private LinearLayout totalRentLayout = null;
    private TextView totalRentTextView = null;
    private LinearLayout loanMoneyLayout = null;
    private TextView loanMoneyTextView = null;

    private LinearLayout contentLayout = null;

    private LandlordAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_landlord_home);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLandlordInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("首页");

        this.logoImageView = (CustomNetworkImageView) this.findViewById(R.id.logoImageView);
        this.balanceTextView = (TextView) this.findViewById(R.id.balanceTextView);
        this.withdrawTextView = (TextView) this.findViewById(R.id.withdrawTextView);
        this.withdrawTextView.setOnClickListener(this);

        this.totalRentLayout = (LinearLayout) this.findViewById(R.id.totalRentLayout);
        this.totalRentLayout.setOnClickListener(this);

        this.totalRentTextView = (TextView) this.findViewById(R.id.totalRentTextView);

        this.loanMoneyLayout = (LinearLayout) this.findViewById(R.id.loanMoneyLayout);
        this.loanMoneyLayout.setOnClickListener(this);

        this.loanMoneyTextView = (TextView) this.findViewById(R.id.loanMoneyTextView);

        this.contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
    }

    private void requestLandlordInfo() {
        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_LANDLORD, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, LandlordAppDto.class);
                    AppMessageDto<LandlordAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        infoDto = dto.getData();

                        responseLandlordInfo();

                    } else {
                        Toast.makeText(LandlordHomeActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        this.addToRequestQueue(request, "正在提交数据...");
    }

    private void responseLandlordInfo() {
        this.logoImageView.setImageUrl(Constants.HOST_IP + infoDto.getLogoUrl(), ImageCacheManager.getInstance().getImageLoader());
        this.balanceTextView.setText(infoDto.getSurplusMoney());
        this.totalRentTextView.setText(infoDto.getTotalRent());
        this.loanMoneyTextView.setText(StringUtils.isBlank(infoDto.getLoanMoney()) ? "0.00" : infoDto.getLoanMoney());

        this.contentLayout.removeAllViews();
        for (LandlordHouseListAppDto dto : infoDto.getHouses()) {
            LandlordHomeLayout layout = new LandlordHomeLayout(this);
            layout.setData(dto);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, AdapterUtil.dip2px(this, 20));
            this.contentLayout.addView(layout, params);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdrawTextView: {
                Intent intent = new Intent(this, WithdrawalsActivity.class);
                this.startActivityForResult(intent, 100);
            }
            break;

            case R.id.totalRentLayout: {
                Intent intent = new Intent(this, LandlordContactKeeperActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.loanMoneyLayout: {
                Intent intent = new Intent(this, LandlordContactKeeperActivity.class);
                this.startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTimeMillis = 0;

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTimeMillis = System.currentTimeMillis();
        } else {
            MobclickAgent.onKillProcess(this); // 用来保存统计数据

            for (Activity act : ActivityManager.getInstance().getAllActivity()) {
                act.finish();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
