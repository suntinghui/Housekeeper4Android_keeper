package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.TextView;

import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.activity.view.NetErrorDialog;
import com.housekeeper.activity.view.NoZoomControllWebView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.utils.NetUtil;
import com.wufriends.housekeeper.keeper.R;

public class ShowWebViewActivity extends BaseActivity implements OnClickListener {

    private Button backBtn = null;
    private Button shareBtn = null;

    private TextView titleTextView = null;
    private NoZoomControllWebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_webview);

        String title = this.getIntent().getStringExtra("title");
        String url = this.getIntent().getStringExtra("url");

        backBtn = (Button) this.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);

        // 是否显示分享，默认不显示
        boolean showShareBtn = this.getIntent().getBooleanExtra("SHOW_SHARE", false);
        shareBtn.setVisibility(showShareBtn ? View.VISIBLE : View.GONE);

        titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        webView = (NoZoomControllWebView) this.findViewById(R.id.webview);
        WebSettings setting = webView.getSettings();

        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);

        setting.setSupportZoom(true);
        setting.setLoadsImagesAutomatically(true);

        setting.setBuiltInZoomControls(true);

        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        webView.loadUrl(url);
    }

    public void onResume() {
        super.onResume();

        if (!NetUtil.isNetworkAvailable(this)) {
            NetErrorDialog.getInstance().show(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            this.backAction();
        }
    }

    public void onBackPressed() {
        this.backAction();
    }

    private void backAction() {
        // 为推送准备
        if (ActivityManager.getInstance().getAllActivity().size() == 1) {
            Intent intent = new Intent(this, KeeperMainActivity.class);
            this.startActivity(intent);
            this.finish();

        } else {
            this.finish();
        }
    }

}
