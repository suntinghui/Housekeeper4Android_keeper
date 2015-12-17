package com.housekeeper.activity.keeper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImSubAccountsAppDto;
import com.ares.house.dto.app.StartupImageAppDto;
import com.housekeeper.activity.DownloadSplashImageService;
import com.housekeeper.activity.HousePushIntentService;
import com.housekeeper.activity.MyTabActivity;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.UMengShareClient;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengUpdateAgent;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sth on 9/10/15.
 */
public class KeeperMainActivity extends MyTabActivity {

    private TabHost tabhost = null;
    private RadioGroup main_radiogroup = null;

    private TabhostReceiver tabhostReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keeper_main);

        clearTopActivity();

        ActivityManager.getInstance().pushActivity(this);

        this.initTabHost();

        this.requestVerifyToken();

        setCheckTab(this.getIntent().getIntExtra("INDEX", 0));

        // 必须在setCheckTab的下面。否则会有刚启动应用选定栏目不起作用的问题。
        this.registerTabhostReceiver();

        this.requestCheckSplashImage();

        this.aboutUmeng();
    }

    private void initTabHost() {
        // 获取按钮
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

        // 设置监听事件
        CheckListener checkradio = new CheckListener();
        main_radiogroup.setOnCheckedChangeListener(checkradio);

        // 往TabWidget添加Tab
        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, KeeperHomeActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, KeeperFindHouseActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, KeeperAddHouseActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, KeeperMeActivityEx.class)));
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
                    ((RadioButton) this.findViewById(R.id.tab_main_find)).setChecked(true);
                    break;

                case 2:
                    ((RadioButton) this.findViewById(R.id.tab_main_addhouse)).setChecked(true);
                    break;

                case 3:
                    ((RadioButton) this.findViewById(R.id.tab_main_me)).setChecked(true);
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

                    case R.id.tab_main_find:
                        tabhost.setCurrentTab(1);
                        break;

                    case R.id.tab_main_addhouse:
                        tabhost.setCurrentTab(2);
                        break;

                    case R.id.tab_main_me:
                        tabhost.setCurrentTab(3);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void requestVerifyToken() {
        // 程序第一次启动的时候没有BASE-TOKEN则不发送。
        if (!ActivityUtil.getSharedPreferences().contains(Constants.Base_Token))
            return;

        JSONRequest request = new JSONRequest(KeeperMainActivity.this, RequestEnum.USER_VERIFY_TOKEN, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImSubAccountsAppDto.class);
                    AppMessageDto<ImSubAccountsAppDto> dtos = objectMapper.readValue(response, type);
                    if (dtos.getStatus() == AppResponseStatus.SUCCESS) {
                        if (dtos != null) {

                        }
                    }
                } catch (Exception e) {
                }

            }
        });

        this.addToRequestQueue(request);
    }

    private void requestCheckSplashImage() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userType", Constants.ROLE);

        JSONRequest request = new JSONRequest(KeeperMainActivity.this, RequestEnum.STARTUP_IMAGE, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, StartupImageAppDto.class);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                    AppMessageDto<ArrayList<StartupImageAppDto>> dto = objectMapper.readValue(response, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Intent intent = new Intent(KeeperMainActivity.this, DownloadSplashImageService.class);
                        intent.putExtra("LIST", dto.getData());
                        KeeperMainActivity.this.startService(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request);
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

    private void aboutUmeng() {
        // UMeng
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);
        MobclickAgent.setAutoLocation(true);

        // 推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mPushAgent.setDebugMode(false);
        mPushAgent.setPushIntentServiceClass(HousePushIntentService.class);

        String deviceToken = UmengRegistrar.getRegistrationId(this);
        Log.e("UMENG", "UMENG DEVICE TOKEN : " + deviceToken);
        SharedPreferences.Editor editor = ActivityUtil.getSharedPreferences().edit();
        editor.putString(Constants.DEVICETOKEN, deviceToken);
        editor.commit();

        // 解决在通知栏里面显示的始终是最新的那一条的问题，谨慎使用，以免用户看到消息过多卸载应用。
        // 合并
        mPushAgent.setMergeNotificaiton(true);

        mPushAgent.onAppStart();

        // UMeng检查更新
        checkUpdate();

        UMengShareClient.setAPPID(this);

        // 在线参数更新
        OnlineConfigAgent.getInstance().updateOnlineConfig(this);
        String value = OnlineConfigAgent.getInstance().getConfigParams(this, "ServiceTelphone");
        Constants.PHONE_SERVICE = value;
    }

    // 检查更新
    private void checkUpdate() {
        // 因为友盟的更新设置是静态的参数，如果在应用中不止一次调用了检测更新的方法，而每次的设置都不一样，请在每次检测更新的函数之前先恢复默认设置再设置参数，避免在其他地方设置的参数影响到这次更新
        UmengUpdateAgent.setDefault();
        // updateOnlyWifi 布尔值true(默认)只在wifi环境下检测更新，false在所有网络环境中均检测更新。
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // deltaUpdate 布尔值true(默认)使用增量更新，false使用全量更新。看了FAQ，貌似增量更新会可能有问题，为了保险起见，不使用增量更新
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);
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
