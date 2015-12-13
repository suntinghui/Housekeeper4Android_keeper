package com.housekeeper.activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.housekeeper.HousekeeperApplication;
import com.housekeeper.activity.view.NetErrorDialog;
import com.housekeeper.activity.view.ProgressHUD;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.utils.NetUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PushAgent.getInstance(this).onAppStart();

        ActivityManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HousekeeperApplication.getInstance().setCurrentActivity(this);

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.cancelRequest();

        ActivityManager.getInstance().popActivity();

        // TODO 如何解决与手势的冲突问题？？？？？

        // 下面的代码是为推送准备的。如果在未启动应用的情况下通过推送打开了某一界面，希望关掉界面后能再打开应用。
        // if (!(this instanceof LoginActivity)) {
        // if (!constantsMainActivity()) {
        // this.startActivity(new Intent(this, SelectIdentityActivity.class));
        // }
        // }

    }

    // Volley
    private RequestQueue mRequestQueue = null;

    private RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    // Adds the specified request to the global queue using the Default TAG.

    /**
     * @param <T>
     * @param req
     * @param message 如果message＝null,则不弹出提示框。如果message="",则默认弹出提示框“请稍候...”
     */
    public <T> boolean addToRequestQueue(Request<T> req, String message) {
        if (!NetUtil.isNetworkAvailable(this)) {
            NetErrorDialog.getInstance().show(this);

            return false;
        }

        this.showProgress(message);

        req.setTag(this);
        getRequestQueue().add(req);

        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                hideProgress();
            }
        });

        return true;
    }

    public void cancelRequest() {
        try {
            mRequestQueue.cancelAll(this);
        } catch (Exception e) {
        }
    }

    // 等待提示框
    private static ProgressHUD hud = null;

    public void showProgress(String message) {
        try {
            if (message == null)
                return;

            if ("".equals(message.trim()))
                message = "请稍候...";

            if (hud == null) {
                hud = ProgressHUD.show(this, message, true, new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO
                        cancelRequest();

                        dialog.dismiss();
                    }
                });
            } else {
                hud.setMessage(message);
            }

            if (!hud.isShowing())
                hud.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        try {
            hud.dismiss();
            hud = null;
        } catch (Exception e) {
        }
    }

    protected void onDestory() {
        // TODO Auto-generated method stub

    }

}
