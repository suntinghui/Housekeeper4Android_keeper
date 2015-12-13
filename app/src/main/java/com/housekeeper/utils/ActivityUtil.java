package com.housekeeper.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.housekeeper.HousekeeperApplication;
import com.wufriends.housekeeper.keeper.R;

public class ActivityUtil {

    /**
     * 判断应用是否正在运行
     * <p/>
     * ture 正在运行 反之，则说明还没有启动应用
     *
     * @param context
     * @return
     */
    public static boolean checkAppRunning(Context context) {
        boolean isAppRunning = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(context.getPackageName()) && info.baseActivity.getPackageName().equals(context.getPackageName())) {
                isAppRunning = true;
                break;
            }
        }

        return isAppRunning;
    }

    public static void shakeView(View view) {
        YoYo.with(Techniques.Shake).duration(500).playOn(view);
    }

    public static void shakeView(View parentView, int resId) {
        YoYo.with(Techniques.Shake).duration(500).playOn(parentView.findViewById(resId));
    }

    // 只要在设置ListView的Adapter后调用此静态方法即可让ListView正确的显示在其父ListView的ListItem中。
    // 但是要注意的是，子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

	/*
     * public static void setEmptyView(Context context, ListView listView) { ImageView emptyView = new ImageView(context);
	 * 
	 * RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); lp.addRule(RelativeLayout.CENTER_IN_PARENT); emptyView.setLayoutParams(lp);
	 * 
	 * emptyView.setImageResource(R.drawable.nodata); emptyView.setScaleType(ScaleType.CENTER_INSIDE);
	 * 
	 * ((ViewGroup) listView.getParent()).addView(emptyView); listView.setEmptyView(emptyView); }
	 */


    public static ImageView setEmptyView(Activity context, ListView listView) {
        LinearLayout emptyLayout = (LinearLayout) context.findViewById(R.id.emptyLayout);
        listView.setEmptyView(emptyLayout);
        ImageView noDataImageView = (ImageView) emptyLayout.findViewById(R.id.noDataImageView);
        return noDataImageView;
    }

    public static ImageView setEmptyView(LinearLayout layout, ListView listView) {
        LinearLayout emptyLayout = (LinearLayout) layout.findViewById(R.id.emptyLayout);
        listView.setEmptyView(emptyLayout);
        ImageView noDataImageView = (ImageView) emptyLayout.findViewById(R.id.noDataImageView);
        return noDataImageView;
    }


    public static SharedPreferences getSharedPreferences() {
        return HousekeeperApplication.getInstance().getSharedPreferences("HOUSEKEEPER", Context.MODE_PRIVATE);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionCode() {
        try {
            PackageManager manager = HousekeeperApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(HousekeeperApplication.getInstance().getPackageName(), 0);
            String version = info.versionCode + "";
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 判断是否安装了应用
    public static boolean isPackageExists(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }
}
