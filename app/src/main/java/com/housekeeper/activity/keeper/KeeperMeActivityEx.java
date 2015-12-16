package com.housekeeper.activity.keeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.ares.house.dto.app.UserAppDto;
import com.ares.house.dto.app.UserHouseListAppDto;
import com.ecloud.pulltozoomview.PullToZoomBase;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.InvestmentActivity;
import com.housekeeper.activity.ShowWebViewActivity;
import com.housekeeper.activity.TransferHistoryActivity;
import com.housekeeper.activity.WithdrawalRecordsActivity;
import com.housekeeper.activity.WithdrawalsActivity;
import com.housekeeper.activity.tenant.TenantCardSettingActivity;
import com.housekeeper.activity.tenant.TenantLookListActivity;
import com.housekeeper.activity.tenant.TenantPersonalVerifyActivity;
import com.housekeeper.activity.tenant.TenantSettingActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.AdapterUtil;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;
import com.yuan.magic.MagicScrollView;
import com.yuan.magic.MagicTextView;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 9/10/15.
 * <p>
 * 房管家 我 系统设置
 */
public class KeeperMeActivityEx extends BaseActivity implements View.OnClickListener {

    private LinearLayout headLayout = null; // 我
    private TextView topCardTextView = null; // 银行卡
    private TextView topRecordTextView = null; // 看房记录
    private TextView topSettingTextView = null; // 系统

    private CircleImageView headImageView = null;

    private LinearLayout hqAccountLayout = null;
    private MagicScrollView magicScrollView = null;
    private MagicTextView totalMoneyTextView = null;// 累计收益
    private TextView yesterdayEarningsTextView = null; // 昨日收益：0.00元

    private TextView guguTextView = null; // 鼓鼓理财，为您创造10%的活期理财收益

    private TextView moneyTextView = null;
    private TextView commissionMoneyTextView = null;
    private TextView manageHouseCountTextView = null;

    private MyAppAgentDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keeper_me_ex);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestUserMy();
    }

    private void initView() {
        headLayout = (LinearLayout) this.findViewById(R.id.headLayout);
        headLayout.setOnClickListener(this);

        headImageView = (CircleImageView) this.findViewById(R.id.headImageView);

        topCardTextView = (TextView) this.findViewById(R.id.topCardTextView);
        topCardTextView.setOnClickListener(this);

        topRecordTextView = (TextView) this.findViewById(R.id.topRecordTextView);
        topRecordTextView.setOnClickListener(this);

        topSettingTextView = (TextView) this.findViewById(R.id.topSettingTextView);
        topSettingTextView.setOnClickListener(this);

        hqAccountLayout = (LinearLayout) this.findViewById(R.id.hqAccountLayout);
        hqAccountLayout.setOnClickListener(this);

        totalMoneyTextView = (MagicTextView) this.findViewById(R.id.totalMoneyTextView);
        totalMoneyTextView.setLargeFontSize(35);
        totalMoneyTextView.setSmallFontSize(35);
        totalMoneyTextView.setValue(0.00);

        magicScrollView = (MagicScrollView) this.findViewById(R.id.magicScrollView);

        yesterdayEarningsTextView = (TextView) this.findViewById(R.id.yesterdayEarningsTextView);

        guguTextView = (TextView) this.findViewById(R.id.guguTextView);
        guguTextView.setText(Html.fromHtml("<font color=#23AFF5>鼓鼓理财，</font><font color=#333333>为您创造10%的活期理财收益</font>"));
        guguTextView.setOnClickListener(this);

        moneyTextView = (TextView) this.findViewById(R.id.moneyTextView);
        commissionMoneyTextView = (TextView) this.findViewById(R.id.commissionMoneyTextView);
        manageHouseCountTextView = (TextView) this.findViewById(R.id.manageHouseCountTextView);

        this.findViewById(R.id.balanceLayout).setOnClickListener(this);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int[] location = new int[2];

            totalMoneyTextView.getLocationInWindow(location);
            totalMoneyTextView.setLocHeight(location[1]);

            magicScrollView.sendScroll(MagicScrollView.UP, 0);
        }

        ;
    };

    private void requestUserMy() {
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
                        Toast.makeText(KeeperMeActivityEx.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据请稍候...");
    }

    private void responseUserMy() {
        headImageView.setImageURL(Constants.HOST_IP + infoDto.getLogoUrl());
        if (StringUtils.isBlank(infoDto.getLogoUrl())) {
            headImageView.setBorderWidth(0);
        } else {
            headImageView.setBorderWidth(2);
        }

        double totalEarnings = Double.parseDouble(infoDto.getHqMoney());
        // 只有当数字大于0.10的时候，才会有涨动的动画，而且，如果小于0.10，金额会显示为0.00，且界面卡动。
        if (totalEarnings >= 0.10) {
            totalMoneyTextView.setValue(totalEarnings);
            magicScrollView.AddListener(totalMoneyTextView);
            mHandler.sendEmptyMessageDelayed(0, 100);
        } else {
            totalMoneyTextView.setText(infoDto.getHqMoney());
        }

        totalMoneyTextView.setText(infoDto.getHqMoney());

        yesterdayEarningsTextView.setText("昨日收益：" + infoDto.getHqYesterday() + " 元");
        moneyTextView.setText(infoDto.getSurplusMoney());
        commissionMoneyTextView.setText(infoDto.getTotalCommission());
        manageHouseCountTextView.setText(infoDto.getHouseCount() + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headLayout: {
                Intent intent = new Intent(this, KeeperPersonalVerifyActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.topCardTextView: {
                Intent intent = new Intent(this, TenantCardSettingActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.topRecordTextView: {
                Intent intent = new Intent(this, TransferHistoryActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.topSettingTextView: {
                Intent intent = new Intent(this, KeeperSystemSettingActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.hqAccountLayout: { // 活期账户，昨日收益
                InvestmentActivity.setDefaultType(InvestmentActivity.TYPE_HQ);
                Intent intent = new Intent(this, InvestmentActivity.class);
                intent.putExtra("type", InvestmentActivity.TYPE_HQ);
                this.startActivity(intent);
            }
            break;

            case R.id.balanceLayout: { // 账户余额
                Intent intent = new Intent(this, WithdrawalsActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.guguTextView: {
                Intent intent = new Intent(this, ShowWebViewActivity.class);
                intent.putExtra("title", "鼓鼓理财");
                intent.putExtra("url", "http://www.baggugu.com/app/about.html");
                startActivity(intent);
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
