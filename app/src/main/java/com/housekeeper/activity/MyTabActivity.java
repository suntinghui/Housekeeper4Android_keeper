package com.housekeeper.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImSubAccountsAppDto;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * Created by sth on 9/10/15.
 */
public class MyTabActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestVerifyToken();
    }


    private void requestVerifyToken() {
        // 程序第一次启动的时候没有BASE-TOKEN则不发送。
        if (!ActivityUtil.getSharedPreferences().contains(Constants.Base_Token))
            return;

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_VERIFY_TOKEN, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImSubAccountsAppDto.class);
                    AppMessageDto<ImSubAccountsAppDto> dtos = objectMapper.readValue(response, type);
                    if (dtos.getStatus() == AppResponseStatus.SUCCESS) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request);
    }

    // Volley
    protected RequestQueue mRequestQueue = null;

    protected RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    // Adds the specified request to the global queue using the Default TAG.
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(this);
        getRequestQueue().add(req);
    }

    public void cancelRequest() {
        try {
            this.mRequestQueue.cancelAll(this);
        } catch (Exception e) {

        }
    }

    protected void clearTopActivity() {
        for (Activity act : ActivityManager.getInstance().getAllActivity()) {
            if (!(act instanceof KeeperMainActivity)) {
                act.finish();
            }
        }
    }
}
