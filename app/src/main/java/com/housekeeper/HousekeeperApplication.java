package com.housekeeper;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class HousekeeperApplication extends Application {

	private static HousekeeperApplication instance = null;

	private Activity mCurrentActivity = null;

	public void onCreate() {
		super.onCreate();

		com.umeng.socialize.utils.Log.LOG = true;

		instance = this;
	}

	public static synchronized HousekeeperApplication getInstance() {
		return instance;
	}

	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	public void setCurrentActivity(Activity mCurrentActivity) {
		this.mCurrentActivity = mCurrentActivity;
	}

	/**
	 * 返回配置文件的日志开关
	 * 
	 * @return
	 */
	public boolean getLoggingSwitch() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			boolean b = appInfo.metaData.getBoolean("LOGGING");
			return b;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean getAlphaSwitch() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			boolean b = appInfo.metaData.getBoolean("ALPHA");
			return b;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

}
