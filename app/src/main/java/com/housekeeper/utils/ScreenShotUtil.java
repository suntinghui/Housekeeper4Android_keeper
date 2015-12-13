package com.housekeeper.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class ScreenShotUtil {

	// 程序入口
	public static void shoot(Activity a, String filePath) {
		// ScreenShotUtil.savePic(, filePath);
		ScreenShotUtil.takeScreenShot(a);
	}

	// 获取指定Activity的截屏，保存到png文件
	public static void takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		if (bitmap != null) {
			try {
				FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "screenshot.jpg");
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.e("===", "screen shot  is null !!!");
		}
	}

	// 保存到sdcard
	private static void savePic(Bitmap b, String strFileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}