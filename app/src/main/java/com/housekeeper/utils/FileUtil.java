package com.housekeeper.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.housekeeper.HousekeeperApplication;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

	/**
	 * 返回程序路径 以 / 结束
	 *
	 * @return
	 */
	public static String getFilePath() {
		// 其它程序无法访问
		// String path = ApplicationEnvironment.getInstance().getApplication().getFilesDir().getPath()+"/download/";
		String path = Environment.getExternalStorageDirectory() + File.separator + HousekeeperApplication.getInstance().getPackageName() + File.separator;
		File file = new File(path);
		if (!file.exists()) {
			// file.mkdir();
			// creating missing parent directories if necessary
			file.mkdirs();
		}

		return path;
	}

	public static String createFolder(String name) {
		String path = Environment.getExternalStorageDirectory() + File.separator + HousekeeperApplication.getInstance().getPackageName() + File.separator + name + File.separator;

		File file = new File(path);
		if (!file.exists()) {
			// file.mkdir();
			// creating missing parent directories if necessary
			file.mkdirs();
		}

		return path;
	}

	public static ArrayList<File> fileList(String pathName) {
		ArrayList<File> list = new ArrayList<File>();

		File file = new File(createFolder(pathName));
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				list.add(f);
			}
		}

		return list;
	}

	// 判断文件是否存在
	public static boolean fileExists(String pathName, String fileName) {
		File file = new File(getFilePath() + pathName + File.separator + fileName);
		return file.exists();
	}

	// 删除目录下的所有文件
	public static void deleteFiles(String pathName) {
		File fileDir = new File(getFilePath() + pathName + File.separator);
		for (File file : fileDir.listFiles()) {
			file.delete();
			Log.e("delete file", file.getPath());
		}

		Log.e("delete file", "已删除所有附件");
	}

	public static void deleteFile(String pathName, String fileName) {
		File file = new File(getFilePath() + pathName + File.separator + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 读取文本数据
	 *
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getAssets().open(fileName);
			if (is != null) {

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}
}
