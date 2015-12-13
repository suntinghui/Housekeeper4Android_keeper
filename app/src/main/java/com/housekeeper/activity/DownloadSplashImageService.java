package com.housekeeper.activity;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ares.house.dto.app.StartupImageAppDto;
import com.housekeeper.client.Constants;
import com.housekeeper.utils.FileUtil;
import com.housekeeper.utils.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sth on 10/21/15.
 */
public class DownloadSplashImageService extends Service {

    public static final String DIR_NAME = "SPLASH";

    private int downloadCount = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<StartupImageAppDto> list = (List<StartupImageAppDto>) intent.getSerializableExtra("LIST");
        if (list == null) {
            this.stopSelf();
        }

        // 首先判断是否存在文件夹，如果不存在直接创建
        ArrayList<File> locList = FileUtil.fileList(DIR_NAME);

        ArrayList<String> serviceList = new ArrayList<String>();
        for (StartupImageAppDto dto : list) {
            serviceList.add(MD5Util.getMD5String(dto.getImgUrl()) + ".jpg");
        }

        if (serviceList.isEmpty()) {
            FileUtil.deleteFiles(DIR_NAME);
            this.stopSelf();
        }

        for (File locFile : locList) {
            if (!serviceList.contains(locFile.getName())) {
                FileUtil.deleteFile(DIR_NAME, locFile.getName());
            }
        }

        for (String serFile : serviceList) {
            if (!locList.contains(serFile)) {
                downloadCount++;

                new DownloadTask(Constants.HOST_IP + list.get(serviceList.indexOf(serFile)).getImgUrl(), serFile).execute();
            }
        }

        return START_STICKY;
    }

    class DownloadTask extends AsyncTask {
        private String url;
        private String name;

        public DownloadTask(String url, String name) {
            this.url = url;
            this.name = name;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            InputStream is = downloadFile(this.url);
            if (null != is) {
                saveFile(name, is);
            }

            return null;
        }
    }

    // 根据文件名拼出文件在服务器上的地址，并下载返回InputStream
    private InputStream downloadFile(String urlStr) {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 保存文件到手机中
    private boolean saveFile(String fileName, InputStream is) {
        try {
            boolean isExists = false;

            File mWorkingPath = new File(FileUtil.createFolder(DIR_NAME));
            File outFile = new File(mWorkingPath, fileName);
            if (outFile.exists()) {
                isExists = true;
                outFile.delete();
            }
            OutputStream out = new FileOutputStream(outFile);
            // Transfer bytes from in to out
//			byte[] buf = new byte[is.available()];
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();

            if (isExists) {
                Log.d("UPDATE", fileName + "文件已经被删除替换！");

            } else {
                Log.d("UPDATE", fileName + "文件已经添加！");

            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            downloadCount--;

            if (downloadCount == 0) {
                this.stopSelf();

                Log.e("===", "下载服务已关闭***********************************");
            }
        }
        return false;
    }
}
