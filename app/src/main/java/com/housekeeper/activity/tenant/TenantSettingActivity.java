package com.housekeeper.activity.tenant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.BindedBankActivity;
import com.housekeeper.activity.BindingBankActivity;
import com.housekeeper.activity.FeedBackactivity;
import com.housekeeper.activity.ProductTourActivity;
import com.housekeeper.activity.ShowWebViewActivity;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.RoleTypeEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sth on 9/16/15.
 */
public class TenantSettingActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView settingView;
    private DavinciView bankCardView;
    private DavinciView feedbackView;
    private DavinciView aboutusView;
    private DavinciView ourHistoryView;
    private DavinciView questionView;
    private DavinciView contactusView;
    private DavinciView logoutView;

    private HashMap<String, String> bankInfoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tenant_setting);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.requestBankInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setVisibility(View.INVISIBLE);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("设置");

        settingView = (DavinciView) this.findViewById(R.id.settingView);
        settingView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_01);
        settingView.getTitleTextView().setText("个人设置");
        settingView.getTipTextView().setText("");
        settingView.setOnClickListener(this);

        bankCardView = (DavinciView) this.findViewById(R.id.bankCardView);
        bankCardView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_02);
        bankCardView.getTitleTextView().setText("银行卡");
        bankCardView.getTipTextView().setText("未绑定");
        bankCardView.setOnClickListener(this);

        feedbackView = (DavinciView) this.findViewById(R.id.feedbackView);
        feedbackView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_03);
        feedbackView.getTitleTextView().setText("意见反馈");
        feedbackView.getTipTextView().setText("");
        feedbackView.setOnClickListener(this);

        aboutusView = (DavinciView) this.findViewById(R.id.aboutusView);
        aboutusView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_04);
        aboutusView.getTitleTextView().setText("关于我们");
        aboutusView.getTipTextView().setText("");
        aboutusView.setOnClickListener(this);

        ourHistoryView = (DavinciView) this.findViewById(R.id.ourHistoryView);
        ourHistoryView.getLogoImageView().setBackgroundResource(R.drawable.keeper_img_15);
        ourHistoryView.getTitleTextView().setText("我们的故事");
        ourHistoryView.getTipTextView().setText("");
        ourHistoryView.setOnClickListener(this);

        questionView = (DavinciView) this.findViewById(R.id.questionView);
        questionView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_05);
        questionView.getTitleTextView().setText("常见问题");
        questionView.getTipTextView().setText("");
        questionView.setOnClickListener(this);

        contactusView = (DavinciView) this.findViewById(R.id.contactusView);
        contactusView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_06);
        contactusView.getTitleTextView().setText("联系客服  " + Constants.PHONE_SERVICE);
        contactusView.getTipTextView().setText("");
        contactusView.setOnClickListener(this);

        logoutView = (DavinciView) this.findViewById(R.id.logoutView);
        logoutView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_07);
        logoutView.getTitleTextView().setText("注销登录");
        logoutView.getTipTextView().setText("");
        logoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingView: {
                Intent intent = new Intent(this, TenantPersonalVerifyActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.bankCardView:
                if (!this.gotoBankCard()) {
                    Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.feedbackView: {
                Intent intent = new Intent(this, FeedBackactivity.class);
                this.startActivity(intent);
            }

            break;

            case R.id.aboutusView: {
                Intent intent = new Intent(this, ShowWebViewActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", Constants.HOST_IP + "/app/about.html");
                startActivity(intent);
            }
            break;

            case R.id.ourHistoryView: {
                startActivityForResult(new Intent(this, ProductTourActivity.class), 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return;

            case R.id.questionView: {
                Intent intent = new Intent(this, ShowWebViewActivity.class);
                intent.putExtra("title", "常见问题");
                intent.putExtra("url", Constants.HOST_IP + "/app/help.html");
                startActivity(intent);
            }
            break;

            case R.id.contactusView: {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.PHONE_SERVICE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            break;

            case R.id.logoutView:
                this.logout();

                break;
        }

    }

    private void requestBankInfo() {
        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        bankInfoMap = (HashMap<String, String>) dto.getData();

                        responseBankInfo();

                    } else {
                        Toast.makeText(TenantSettingActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseBankInfo() {
        // 绑定银行卡状态 a未绑定 c绑定失败 d确认中 e已绑定
        String status = "";

        char c = bankInfoMap.get("BANK_CARD_STATUS").charAt(0);
        switch (c) {
            case 'a':
                status = "未绑定";
                break;

            case 'c':
                status = "绑定失败";
                break;

            case 'd':
                status = "确认中";
                break;

            case 'e':
                status = "已绑定";
                break;
        }
        bankCardView.getTipTextView().setText(status);
    }

    private boolean gotoBankCard() {
        if (bankInfoMap == null) {
            this.requestBankInfo();
            return false;
        }

        String bank_id = bankInfoMap.get("BANK_ID");
        if (null == bank_id || TextUtils.isEmpty(bank_id) || TextUtils.equals(bank_id, "null")) {
            Intent intent = new Intent(TenantSettingActivity.this, BindingBankActivity.class);
            TenantSettingActivity.this.startActivityForResult(intent, 0);

        } else {
            Intent intent = new Intent(TenantSettingActivity.this, BindedBankActivity.class);
            intent.putExtra("MAP", bankInfoMap);
            TenantSettingActivity.this.startActivityForResult(intent, 0);
        }

        return true;
    }


    private void logout() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("\n您确定要退出登录吗？").setContentText("").setCancelText("取消").setConfirmText("确定").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();

                ActivityUtil.getSharedPreferences().edit().putString(Constants.Base_Token, "").commit();
                ActivityUtil.getSharedPreferences().edit().putString(Constants.kCURRENT_TYPE, RoleTypeEnum.NONE).commit();

                Intent intent = new Intent(TenantSettingActivity.this, KeeperMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                TenantSettingActivity.this.startActivity(intent);
            }
        }).show();
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
