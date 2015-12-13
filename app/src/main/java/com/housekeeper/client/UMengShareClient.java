package com.housekeeper.client;

import android.app.Activity;

import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class UMengShareClient {

	public static void setAPPID(Activity context) {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, Constants.WX_APP_ID, Constants.WX_AppSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, Constants.WX_APP_ID, Constants.WX_AppSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();
	}

}
