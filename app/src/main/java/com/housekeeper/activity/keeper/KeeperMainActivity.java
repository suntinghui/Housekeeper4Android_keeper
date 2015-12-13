package com.housekeeper.activity.keeper;

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

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImSubAccountsAppDto;
import com.ares.house.dto.app.StartupImageAppDto;
import com.housekeeper.activity.DownloadSplashImageService;
import com.housekeeper.activity.MyTabActivity;
import com.housekeeper.activity.landlord.LandlordMainActivity;
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

        ActivityUtil.getSharedPreferences().edit().putString(Constants.kCURRENT_TYPE, RoleTypeEnum.KEEPER).commit();

        clearTopActivity();

        ActivityManager.getInstance().pushActivity(this);

        this.initTabHost();

        this.requestVerifyToken();

        setCheckTab(this.getIntent().getIntExtra("INDEX", 0));

        // 必须在setCheckTab的下面。否则会有刚启动应用选定栏目不起作用的问题。
        this.registerTabhostReceiver();

        this.requestCheckSplashImage();
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
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, KeeperMeActivity.class)));
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
        map.put("userType", ActivityUtil.getSharedPreferences().getString(Constants.kCURRENT_TYPE, RoleTypeEnum.LANDLORD));

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
