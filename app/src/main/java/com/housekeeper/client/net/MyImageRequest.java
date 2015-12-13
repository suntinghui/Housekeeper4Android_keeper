package com.housekeeper.client.net;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.housekeeper.client.Constants;
import com.housekeeper.utils.ActivityUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sth on 9/29/15.
 */
public class MyImageRequest extends ImageRequest {

    public MyImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/x-www-form-urlencoded");
        params.put("ClientType", "Android");
        params.put("OSVersion", "" + android.os.Build.VERSION.SDK_INT);
        params.put("APPVersion", "" + ActivityUtil.getVersionCode());
        params.put(Constants.Base_Token, ActivityUtil.getSharedPreferences().getString(Constants.Base_Token, ""));
        params.put(Constants.SESSIONID, "JSESSIONID=" + ActivityUtil.getSharedPreferences().getString(Constants.SESSIONID, ""));

        return params;
    }
}
