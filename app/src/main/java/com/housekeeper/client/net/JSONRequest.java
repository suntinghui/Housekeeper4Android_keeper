package com.housekeeper.client.net;

import org.codehaus.jackson.map.DeserializationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.HousekeeperApplication;
import com.housekeeper.activity.LoginActivity;
import com.housekeeper.activity.RegisterActivity4;
import com.housekeeper.activity.RegisterAndLoginActivity;
import com.housekeeper.activity.keeper.KeeperPersonalVerifyActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.NoSetInfoException;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.RoleTypeEnum;
import com.housekeeper.client.TokenExpiredException;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.StringUtil;

public class JSONRequest extends StringRequest {

    private static final int CACHE_EXPIRES_TIME = 1 * 365 * 24 * 60 * 60; // 有效期一年
    private static final int REFRESH_NEED = 0;

    private HashMap<String, String> map = null;

    private String id = RequestEnum.USER_LOGIN;

    /**
     * 不缓存, 默认错误处理
     *
     * @param context
     * @param id
     * @param map
     * @param listener
     */
    public JSONRequest(Context context, String id, HashMap<String, String> map, Response.Listener<String> listener) {
        this(context, id, map, false, listener, new ResponseErrorListener(context));
    }

    /**
     * 自定义是否缓存，默认错误处理
     *
     * @param context
     * @param id
     * @param map
     * @param shouldCache
     * @param listener
     * @param
     */
    public JSONRequest(Context context, String id, HashMap<String, String> map, boolean shouldCache, Response.Listener<String> listener) {
        this(context, id, map, shouldCache, listener, new ResponseErrorListener(context));
    }

    public JSONRequest(Context context, String id, HashMap<String, String> map, boolean shouldCache, Response.Listener<String> listener, ResponseErrorListener errorListener) {
        this(context, id, map, shouldCache, true, listener, errorListener);
    }

    public JSONRequest(Context context, String id, HashMap<String, String> map, boolean shouldCache, boolean needRetryPolicy, Response.Listener<String> listener, ResponseErrorListener errorListener) {
        super(RequestEnum.getRequest(id).getMethod(), RequestEnum.getRequest(id).getUrl(), listener, errorListener);

        if (map != null) {
            Log.e("request data", map.toString());
        }

        this.id = id;

        this.setTag(context);
        this.map = map;
        this.setShouldCache(shouldCache);

        if (needRetryPolicy) {
            RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.setRetryPolicy(retryPolicy);

        } else {
            RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.setRetryPolicy(retryPolicy);
        }
    }

    // 首先所有的请求的报文头中都有Base-Token属性，详情见Request类中的getHeader方法；
    // 所有的响应报文中都需要检查Base-Token属性，每次取到该值后进行更新。特殊地，如果登录与验证TOKEN接口没有返回Base-Token属性则清空该属性。该逻辑见本类中的parseNetworkResponse；
    // 针对所有的报文，如果响应代码是LOGIN，则跳转到登录界面，其他则正常解析接口。
    // 特殊地，通过接口请求用户个人信息图片时（不同于取商品图片），上述处理方法也包括，但是从业务逻辑上不会出现请求图片接口返回要求登录的情况，因为接口中数据依赖于其他接口的返回数据。
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        TreeMap<String, String> headerMap = (TreeMap<String, String>) response.headers;

        String responseStr = null;

        try {
            responseStr = new String(response.data, "UTF-8");

            Log.e("===", "response:" + responseStr);

            checkToken(responseStr);

        } catch (TokenExpiredException e) {
            e.printStackTrace();

            // 因为页面可能同时发起多个请求会导致同时启动多个登录界面，所以将登录界面的launchMode改为singleInstance。
            // 但是，singleInstance与startActivityForResult冲突，所以重新设置了一个变量来解决登录成功后重新刷新界面的问题。

            Constants.NEED_REFRESH_LOGIN = true;

            if (ActivityUtil.getSharedPreferences().getString(Constants.UserName, null) == null) {
                Intent intent = new Intent(HousekeeperApplication.getInstance().getCurrentActivity(), RegisterAndLoginActivity.class);
                HousekeeperApplication.getInstance().getCurrentActivity().startActivityForResult(intent, 0);

            } else {
                Intent intent = new Intent(HousekeeperApplication.getInstance().getCurrentActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("FROM", LoginActivity.FROM_TOKEN_EXPIRED);
                HousekeeperApplication.getInstance().getCurrentActivity().startActivityForResult(intent, LoginActivity.FROM_TOKEN_EXPIRED);
            }

            return Response.error(new IgnoreError(e));

        } catch (NoSetInfoException e) {
            e.printStackTrace();

            if (ActivityUtil.getSharedPreferences().getString(Constants.kCURRENT_TYPE, RoleTypeEnum.KEEPER).equalsIgnoreCase(RoleTypeEnum.KEEPER)) {
                Looper.prepare();

                Intent intent = new Intent(HousekeeperApplication.getInstance().getCurrentActivity(), KeeperPersonalVerifyActivity.class);
                intent.putExtra("exception", true);
                HousekeeperApplication.getInstance().getCurrentActivity().startActivityForResult(intent, RegisterActivity4.REQUESTCODE_SETINFO);
            }

            return Response.error(new IgnoreError(e));

        } catch (Exception e) {
            e.printStackTrace();

            return Response.error(new ParseError(e));
        }

        try {
            saveToken(headerMap);

            Cache.Entry mFakeCache = HttpHeaderParser.parseCacheHeaders(response);
            mFakeCache.etag = null;
            mFakeCache.softTtl = REFRESH_NEED;
            mFakeCache.ttl = System.currentTimeMillis() + CACHE_EXPIRES_TIME * 1000;

            return Response.success(responseStr, mFakeCache);

        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }

    }

    private void saveToken(TreeMap<String, String> headerMap) {
        Editor editor = ActivityUtil.getSharedPreferences().edit();
        if (headerMap.containsKey(Constants.Base_Token)) {
            editor.putString(Constants.Base_Token, headerMap.get(Constants.Base_Token));
        }

        if (headerMap.containsKey(Constants.Set_Cookie)) {
            String cookie = headerMap.get(Constants.Set_Cookie);
            HashMap<String, String> tempMap = string2Map(cookie);
            if (tempMap.containsKey("JSESSIONID")) {
                editor.putString(Constants.SESSIONID, tempMap.get("JSESSIONID"));
            }
        }
        editor.commit();
    }

    private void checkToken(String response) throws TokenExpiredException, NoSetInfoException, Exception {
        if (response == null) {
            throw new Exception();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Object.class);
        AppMessageDto<?> dto = objectMapper.readValue(response, type);
        if (dto == null) {
            throw new Exception();
        }

        if (dto.getStatus() == AppResponseStatus.LOGIN) {
            throw new TokenExpiredException();

        } else if (dto.getStatus() == AppResponseStatus.SETINFO) {
            throw new NoSetInfoException();
        }
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
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

    @Override
    protected void onFinish() {
        super.onFinish();
    }

    // Volley以url作为Cache Key,因为本项目中有的请求地址有可能地址相同而参数不同，所以重写本方法重定义Cache Key
    // Cache Key : http://182.92.217.168:8888/rpc/goods/type/v_list.app{}
    public String getCacheKey() {
        String cacheKey = "";
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(super.getUrl());
            if (this.getBody() != null) {
                sb.append("&");
                sb.append(new String(this.getBody()));
            }

            cacheKey = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();

            cacheKey = super.getCacheKey();
        }

        Log.e("===", "Cache Key : " + cacheKey);

        return StringUtil.MD5Crypto(cacheKey);
    }

    public void addMarker(String tag) {
        Log.e("===", "marker:" + tag);

        super.addMarker(tag);
    }

    private HashMap<String, String> string2Map(String text) {
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            for (String temp : text.split(";")) {
                String str[] = temp.split("=");
                if (str.length == 2) {
                    map.put(str[0].trim(), str[1].trim());
                }
            }
            return map;
        } catch (Exception e) {
            return new HashMap<String, String>();
        }

    }

}
