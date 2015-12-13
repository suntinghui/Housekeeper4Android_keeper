package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.FeedBackactivity;
import com.housekeeper.activity.ProductTourActivity;
import com.housekeeper.activity.ShowWebViewActivity;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RoleTypeEnum;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sth on 9/13/15.
 * <p/>
 * 房管家  我  个人认证
 */
public class KeeperSystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private DavinciView feedbackView;
    private DavinciView aboutusView;
    private DavinciView ourHistoryView;
    private DavinciView questionView;
    private DavinciView contactusView;
    private DavinciView logoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_system_setting);

        this.initView();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("系统设置");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

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
            case R.id.backBtn:
                this.finish();
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

                Intent intent = new Intent(KeeperSystemSettingActivity.this, KeeperMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                KeeperSystemSettingActivity.this.startActivity(intent);
            }
        }).show();
    }
}
