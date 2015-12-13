package com.housekeeper.activity.landlord;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.housekeeper.activity.MyTabActivity;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.umeng.analytics.MobclickAgent;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/10/15.
 */
public class LandlordMainActivity extends MyTabActivity {

    private TabHost tabhost = null;
    private RadioGroup main_radiogroup = null;

    private TabhostReceiver tabhostReceiver = null;

    private long exitTimeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landlord_main);

        clearTopActivity();

        ActivityManager.getInstance().pushActivity(this);

        this.initTabHost();

        setCheckTab(this.getIntent().getIntExtra("INDEX", 0));

        // 必须在setCheckTab的下面。否则会有刚启动应用选定栏目不起作用的问题。
        this.registerTabhostReceiver();

    }

    private void initTabHost() {
        // 获取按钮
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

        // 设置监听事件
        CheckListener checkradio = new CheckListener();
        main_radiogroup.setOnCheckedChangeListener(checkradio);

        // 往TabWidget添加Tab
        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, LandlordHomeActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, LandlordRelationActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, LandlordSettingActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.cancelRequest();

        ActivityManager.getInstance().popActivity();

        this.unregisterReceiver(tabhostReceiver);
    }

    // 指定打开的时候显示哪一个界面。通过intent的index（int）来指定
    private void setCheckTab(int index) {
        try {
            tabhost.setCurrentTab(index);

            switch (index) {
                case 0:
                    ((RadioButton) this.findViewById(R.id.tab_main_home)).setChecked(true);
                    break;

                case 1:
                    ((RadioButton) this.findViewById(R.id.tab_main_add)).setChecked(true);
                    break;

                case 2:
                    ((RadioButton) this.findViewById(R.id.tab_main_setting)).setChecked(true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 监听类
    public class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            try {
                // setCurrentTab 通过标签索引设置当前显示的内容
                // setCurrentTabByTag 通过标签名设置当前显示的内容
                switch (checkedId) {
                    case R.id.tab_main_home:
                        tabhost.setCurrentTab(0);
                        break;

                    case R.id.tab_main_add:
                        tabhost.setCurrentTab(1);
                        break;

                    case R.id.tab_main_setting:
                        tabhost.setCurrentTab(2);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // 定位tabhost
    private void registerTabhostReceiver() {
        tabhostReceiver = new TabhostReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHECK_TABHOST);
        this.registerReceiver(tabhostReceiver, filter);
    }

    public class TabhostReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("INDEX", 0);
            setCheckTab(index);
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

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
