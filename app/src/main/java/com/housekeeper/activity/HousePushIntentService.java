package com.housekeeper.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.housekeeper.activity.keeper.KeeperAddLandlordRelationQRActivity;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.activity.landlord.LandlordMainActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.JsonUtil;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;
import com.wufriends.housekeeper.keeper.R;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import java.util.Map;

public class HousePushIntentService extends UmengBaseIntentService {

    // 如果需要打开Activity，请调用Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)；否则无法打开Activity。
    @Override
    protected void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);

        Log.e("PUSH", "----------------收到推送了------------------------");

        FeedbackPush.getInstance(context).init(FeedBackactivity.class, true);
        if (FeedbackPush.getInstance(context).onFBMessage(intent)) {
            // The push message is reply from developer.
            return;
        }

        UMessage msg = null;
        try {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            msg = new UMessage(new JSONObject(message));
            UTrack.getInstance(context).trackMsgClick(msg);

            Map<String, String> map = JsonUtil.jsonToMap(msg.custom);
            String functionType = map.get("functionType").trim();
            String functionData = map.get("functionData").trim();

            Log.e("PUSH", functionType + " -- " + functionData);

            Intent actionIntent = jumpAction(context, Integer.parseInt(functionType), functionData);
            showNotification(context, msg, actionIntent);

        } catch (Exception e) {
            e.printStackTrace();

            Intent tempIntent = new Intent(context, KeeperMainActivity.class);
            showNotification(context, msg, tempIntent);
        }
    }

    // 通知栏显示当前播放信息，利用通知和 PendingIntent来启动对应的activity
    public void showNotification(Context context, UMessage msg, Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        Notification mNotification = builder.build();
        mNotification.icon = R.drawable.ic_launcher;// notification通知栏图标
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        // 自定义布局
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setImageViewResource(R.id.notification_large_icon, R.drawable.ic_launcher);
        contentView.setTextViewText(R.id.notification_title, msg.title);
        contentView.setTextViewText(R.id.notification_text, msg.text);
        mNotification.contentView = contentView;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法，但是必须定义contentIntent
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(103, mNotification);
    }

    private Intent jumpAction(Context context, int functionType, String functionData) {
        /**
         * 跳转数据 json格式 a系统消息的时候是{"url":"{type}/{id}/html.app"} 跳转标示1定投列表 2抢投列表 3转让列表 4打开链接 5好友列表 6我的债权列表
         */
        Intent intent = new Intent();

        try {
            switch (functionType) {

                case 0: // 无任何操作

                    break;

                case 4: {
                    intent = new Intent(context, ShowWebViewActivity.class);
                    intent.putExtra("title", "系统消息");
                    intent.putExtra("url", Constants.HOST_IP + "/rpc/" + JsonUtil.jsonToMap(functionData).get("url"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                break;

                case 100: { // 关联成功   100 -- {"id":10,"type":"join","userType":"LANDLORD"}
                    Intent intent100 = new Intent(Constants.ACTION_CHECK_RELATION);
                    sendBroadcast(intent100);
                }

                break;

                default:
                    Log.e("PUSH", "没有找到相应的类型");
                    if (!ActivityUtil.checkAppRunning(this)) {
                        intent = new Intent(context, KeeperMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }
}
