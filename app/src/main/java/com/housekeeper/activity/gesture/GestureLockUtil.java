package com.housekeeper.activity.gesture;

import android.content.SharedPreferences.Editor;

import com.housekeeper.client.Constants;
import com.housekeeper.utils.ActivityUtil;

public class GestureLockUtil {

	// 用户ID以区分不同用户。
	public static final String LOCK = ActivityUtil.getSharedPreferences().getString(Constants.UserName, "") + "LOCK";
	public static final String LOCK_KEY = ActivityUtil.getSharedPreferences().getString(Constants.UserName, "") + "LOCK_KEY";

	/**
	 * 判断是否设置了手势密码
	 * 
	 * @param context
	 */
	public static boolean hasSetGestureUtil() {

		String lock = ActivityUtil.getSharedPreferences().getString(LOCK_KEY, null);
		if (lock == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void clearGestureLock() {
		Editor editor = ActivityUtil.getSharedPreferences().edit();
		editor.putString(LOCK_KEY, null);
		editor.commit();
	}

	public static void setGestureLock(String value) {
		Editor editor = ActivityUtil.getSharedPreferences().edit();
		editor.putString(LOCK_KEY, value);
		editor.commit();
	}

	public static String getGestureLock() {
		String lock = ActivityUtil.getSharedPreferences().getString(LOCK_KEY, null);
		return lock;
	}

}
