package com.housekeeper.activity.tenant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.UserAppDto;
import com.ares.house.dto.app.UserHouseListAppDto;
import com.ecloud.pulltozoomview.PullToZoomBase;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.WithdrawalsActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.AdapterUtil;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * Created by sth on 9/16/15.
 */
public class TenantMeActivity extends BaseActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx scrollView;

    private CustomNetworkImageView headImageView = null; // 头像
    private TextView telphoneTextView = null; // 加密的手机号
    private TextView balanceTextView = null; // 账户余额
    private Button withdrawBtn = null; // 提现

    private LinearLayout contentLayout = null;
    private TextView noDataTextView = null;

    private UserAppDto userDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tenant_me);

        loadViewForCode();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestUserHouse();
    }

    private void loadViewForCode() {
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        // 背景上的内容，头像，姓名等
        View headView = LayoutInflater.from(this).inflate(R.layout.tenant_me_head, null, false);
        scrollView.setHeaderView(headView);

        // 背景图片
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        scrollView.setZoomView(zoomView);

        // 下面的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.tenant_me_content, null, false);
        scrollView.setScrollContentView(contentView);

        contentLayout = (LinearLayout) contentView.findViewById(R.id.contentLayout);
        noDataTextView = (TextView) contentView.findViewById(R.id.noDataTextView);
        noDataTextView.setVisibility(View.GONE);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        initView();
    }

    private void initView() {
        telphoneTextView = (TextView) scrollView.getHeaderView().findViewById(R.id.telphoneTextView);
        balanceTextView = (TextView) scrollView.getHeaderView().findViewById(R.id.balanceTextView);
        withdrawBtn = (Button) scrollView.getHeaderView().findViewById(R.id.withdrawBtn);
        withdrawBtn.setOnClickListener(this);

        headImageView = (CustomNetworkImageView) scrollView.getHeaderView().findViewById(R.id.headImageView);
        headImageView.setErrorImageResId(R.drawable.head_keeper_default);
        headImageView.setDefaultImageResId(R.drawable.head_keeper_default);

        scrollView.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
            }

            @Override
            public void onPullZoomEnd() {
                requestUserHouse();
            }
        });
    }

    private void requestUserHouse() {
        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_USER_HOUSE, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, UserAppDto.class);
                    AppMessageDto<UserAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        userDto = dto.getData();

                        responseUserHouse();

                    } else {
                        Toast.makeText(TenantMeActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseUserHouse() {
        headImageView.setImageUrl(Constants.HOST_IP + userDto.getLogoUrl(), ImageCacheManager.getInstance().getImageLoader());
        telphoneTextView.setText(userDto.getEncryptTelphone());
        balanceTextView.setText(userDto.getSurplusMoney() + " 元");

        if (userDto.getHouses().isEmpty()) {
            noDataTextView.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }

        // 设置下面的列表数据
        contentLayout.removeAllViews();

        int i = 0;
        for (UserHouseListAppDto dto : userDto.getHouses()) {
//            TenantMeItemLayout itemLayout = new TenantMeItemLayout(this);
//            itemLayout.setData(dto);
//            itemLayout.setSurplusMoney(userDto.getSurplusMoney());
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, i == 0 ? 0 : AdapterUtil.dip2px(this, 20), 0, 0);
//            contentLayout.addView(itemLayout, params);

            i++;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdrawBtn: {
                Intent intent = new Intent(this, WithdrawalsActivity.class);
                this.startActivityForResult(intent, 100);
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
