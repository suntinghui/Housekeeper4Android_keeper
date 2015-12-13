package com.housekeeper.activity.keeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.MyAppAgentDto;
import com.ecloud.pulltozoomview.PullToZoomBase;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.BindedBankActivity;
import com.housekeeper.activity.BindingBankActivity;
import com.housekeeper.activity.WithdrawalRecordsActivity;
import com.housekeeper.activity.WithdrawalsActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sth on 9/10/15.
 * <p/>
 * 房管家 我 系统设置
 */
public class KeeperMeActivity extends BaseActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx scrollView;

    private TextView telphoneTextView = null; // 加密的手机号
    private CustomNetworkImageView headImageView = null; // 头像

    private TextView houseCountTextView = null; // 管理房源总数
    private TextView totalRentTextView = null; // 共收租金
    private TextView leaseCountTextView = null; // 租赁中数量
    private TextView totalWaitRentTextView = null; // 代收租金
    private TextView waitLeaseCountTextView = null; // 待租中
    private TextView surplusMoneyTextView = null; // 绑定银行卡状态

    private DavinciView personVerifyView; // 个人设置
    private DavinciView rentRecordsView; // 收租记录
    private DavinciView withdrawView; // 佣金提现
    private DavinciView withdrawRecordsView; // 提现记录
    private DavinciView settingView; // 系统设置

    private MyAppAgentDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keeper_me);

        loadViewForCode();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestUserMy(null);
    }

    private void loadViewForCode() {
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        // 背景上的内容，头像，姓名等
        View headView = LayoutInflater.from(this).inflate(R.layout.keeper_me_head, null, false);
        scrollView.setHeaderView(headView);

        // 背景图片
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        scrollView.setZoomView(zoomView);

        // 下面的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.keeper_me_content, null, false);
        scrollView.setScrollContentView(contentView);

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

        headImageView = (CustomNetworkImageView) scrollView.getHeaderView().findViewById(R.id.headImageView);
        headImageView.setErrorImageResId(R.drawable.head_keeper_default);
        headImageView.setDefaultImageResId(R.drawable.head_keeper_default);

        houseCountTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.houseCountTextView);
        totalRentTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.totalRentTextView);
        leaseCountTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.leaseCountTextView);
        totalWaitRentTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.totalWaitRentTextView);
        waitLeaseCountTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.waitLeaseCountTextView);
        surplusMoneyTextView = (TextView) scrollView.getPullRootView().findViewById(R.id.surplusMoneyTextView);

        personVerifyView = (DavinciView) scrollView.getPullRootView().findViewById(R.id.personVerifyView);
        personVerifyView.getLogoImageView().setBackgroundResource(R.drawable.keeper_me_01);
        personVerifyView.getTitleTextView().setText("个人认证");
        personVerifyView.getTipTextView().setText("");
        personVerifyView.setOnClickListener(this);

        rentRecordsView = (DavinciView) scrollView.getPullRootView().findViewById(R.id.rentRecordsView);
        rentRecordsView.getLogoImageView().setBackgroundResource(R.drawable.keeper_me_02);
        rentRecordsView.getTitleTextView().setText("收租记录");
        rentRecordsView.getTipTextView().setText("");
        rentRecordsView.setOnClickListener(this);

        withdrawView = (DavinciView) scrollView.getPullRootView().findViewById(R.id.withdrawView);
        withdrawView.getLogoImageView().setBackgroundResource(R.drawable.keeper_me_03);
        withdrawView.getTitleTextView().setText("佣金提现");
        withdrawView.getTipTextView().setText("");
        withdrawView.setOnClickListener(this);

        withdrawRecordsView = (DavinciView) scrollView.getPullRootView().findViewById(R.id.withdrawRecordsView);
        withdrawRecordsView.getLogoImageView().setBackgroundResource(R.drawable.keeper_me_04);
        withdrawRecordsView.getTitleTextView().setText("提现记录");
        withdrawRecordsView.getTipTextView().setText("");
        withdrawRecordsView.setOnClickListener(this);

        settingView = (DavinciView) scrollView.getPullRootView().findViewById(R.id.settingView);
        settingView.getLogoImageView().setBackgroundResource(R.drawable.keeper_me_05);
        settingView.getTitleTextView().setText("系统设置");
        settingView.getTipTextView().setText("");
        settingView.setOnClickListener(this);

        scrollView.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
            }

            @Override
            public void onPullZoomEnd() {
                requestUserMy("正在请求数据...");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personVerifyView: {
                Intent intent = new Intent(this, KeeperPersonalVerifyActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.rentRecordsView: {
                Intent intent = new Intent(this, KeeperRentRecordListActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.withdrawView: {
                Intent intent = new Intent(this, WithdrawalsActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.withdrawRecordsView: {
                Intent intent = new Intent(this, WithdrawalRecordsActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.settingView: {
                Intent intent = new Intent(this, KeeperSystemSettingActivity.class);
                this.startActivity(intent);
            }
            break;
        }
    }

    private void requestUserMy(String msg) {
        JSONRequest request = new JSONRequest(this, RequestEnum.USER_MY, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, MyAppAgentDto.class);
                    AppMessageDto<MyAppAgentDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        infoDto = dto.getData();

                        responseUserMy();

                    } else {
                        Toast.makeText(KeeperMeActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, msg);
    }

    private void responseUserMy() {
        telphoneTextView.setText(infoDto.getEncryptTelphone());
        headImageView.setImageUrl(Constants.HOST_IP + infoDto.getLogoUrl() + "?random=" + ActivityUtil.getSharedPreferences().getString(Constants.HEAD_RANDOM, "0"), ImageCacheManager.getInstance().getImageLoader());

        houseCountTextView.setText(infoDto.getHouseCount() + "");
        totalRentTextView.setText(infoDto.getTotalRent() + "");
        leaseCountTextView.setText(infoDto.getLeaseCount() + "");
        totalWaitRentTextView.setText(infoDto.getTotalWaitRent() + "");
        waitLeaseCountTextView.setText(infoDto.getWaitLeaseCount() + "");
        surplusMoneyTextView.setText(infoDto.getSurplusMoney() + "");
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
