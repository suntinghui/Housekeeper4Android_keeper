package com.housekeeper.activity.landlord;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.keeper.KeeperAddHouseRelationActivity;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.proguard.S;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by sth on 9/10/15.
 * <p/>
 * 关联
 */
public class LandlordRelationActivity extends BaseActivity implements QRCodeView.ResultHandler, View.OnClickListener {

    private ZBarView zbarView;

    private EditText codeEditText;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_landlord_relation);

        this.findViewById(R.id.backBtn).setVisibility(View.INVISIBLE);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("关联");

        zbarView = (ZBarView) this.findViewById(R.id.zbarView);
        zbarView.setResultHandler(this);

        codeEditText = (EditText) this.findViewById(R.id.codeEditText);
        doneBtn = (Button) this.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        zbarView.startSpotAndShowRect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        zbarView.stopSpotAndHiddenRect();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void handleResult(String result) {
        // Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        vibrate();

        requestRelation(result.replaceAll("HOUSE_", ""));
    }

    @Override
    public void handleCameraError() {
        Toast.makeText(this, "打开相机出错,请手动输入", Toast.LENGTH_SHORT).show();
    }

    private void requestRelation(String code) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("code", code);

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_LANDLORDJOIN, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(LandlordRelationActivity.this, "关联成功", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LandlordRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        if (TextUtils.isEmpty(codeEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入关联码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneBtn:
                if (checkValue()) {
                    requestRelation(codeEditText.getText().toString().trim());
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
