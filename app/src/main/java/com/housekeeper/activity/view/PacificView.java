package com.housekeeper.activity.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImageAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.CropImageActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.AdapterUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 9/28/15.
 */
public class PacificView extends LinearLayout implements View.OnClickListener {

    private BaseActivity context;

    private LinearLayout contentLayout = null;
    private LinearLayout uploadLayout = null;

    private int tag = 0;
    public static int CURRENT_TAG = 0; // 当前操作的控件

    public static final String TYPE_GETIMG_API = "TYPE_GETIMG_API"; // 需要通过接口来获利图片流，默认值
    public static final String TYPE_GETIMG_ADDRESS = "TYPE_GETIMG_ADDRESS"; // 直接通过地址获取图片
    public String getImageType = TYPE_GETIMG_API;

    /* 头像上传相关 */
    private static String localTempImageFileName = "";
    private static final int FLAG_CHOOSE_IMG = 0x100;// 相册选择
    private static final int FLAG_CHOOSE_PHONE = 0x101;// 拍照
    private static final int FLAG_MODIFY_FINISH = 0x102;

    public static final File FILE_SDCARD = Environment.getExternalStorageDirectory();
    public static final String IMAGE_PATH = "housekeeper";
    public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
    public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL, "images/screenshots");

    private String headerImageStr = "";

    private PacificListener listener = null;

    private boolean editable = true;

    public PacificView(Context context) {
        super(context);

        this.initView(context);
    }

    public PacificView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_pacific, this);

        contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);

        uploadLayout = (LinearLayout) this.findViewById(R.id.uploadLayout);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (((BaseActivity) context).getWindowManager().getDefaultDisplay().getWidth() / 1.7));
        params.setMargins(0, AdapterUtil.dip2px(context, 20), 0, 0);
        uploadLayout.setLayoutParams(params);
        uploadLayout.setOnClickListener(this);
        uploadLayout.setVisibility(View.GONE);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setGetImageType(String getType) {
        this.getImageType = getType;
    }

    public void responseDownloadImageList(int maxCount, List<ImageAppDto> imageList) {
        if (editable && maxCount > imageList.size()) {
            uploadLayout.setVisibility(View.VISIBLE);
        } else {
            uploadLayout.setVisibility(View.GONE);
        }

        contentLayout.removeAllViews();

        for (ImageAppDto imageDto : imageList) {
            DiaoyuView deleteView = new DiaoyuView(this.context);
            deleteView.setValue(this, imageDto, this.getImageType, editable);

            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (((BaseActivity) context).getWindowManager().getDefaultDisplay().getWidth() / 1.7));
            params.setMargins(0, AdapterUtil.dip2px(this.context, 20), 0, 0);
            contentLayout.addView(deleteView, params);
        }
    }

    public void responseUploadImage() {
        if (this.listener != null) {
            this.listener.downloadImageList();
        }
    }

    public void responseDeleteImage() {
        if (this.listener != null) {
            this.listener.downloadImageList();
        }
    }

    public void setPacificListener(PacificListener listener) {
        this.listener = listener;

        this.listener.downloadImageList();
    }

    public PacificListener getPacificListener() {
        return this.listener;
    }

    public interface PacificListener {
        public void downloadImageList();

        public void uploadImage(String imageBase64);

        public void deleteImage(String imageId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadLayout:
                CURRENT_TAG = this.tag;
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        CropImageView.CROP_TYPE = CropImageView.CROP_TYPE_HEADER;
        choosePic();
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        // 判断是否操作的当前的控件，如果不是直接返回。
        if (this.tag != CURRENT_TAG)
            return;

        if (requestCode == FLAG_CHOOSE_IMG && resultCode == Activity.RESULT_OK) { // 选择图片
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (null == cursor) {
                        Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Log.i("===", "path=" + path);
                    Intent intent = new Intent(context, CropImageActivity.class);
                    intent.putExtra("path", path);
                    context.startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Log.i("===", "path=" + uri.getPath());
                    Intent intent = new Intent(context, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    context.startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == Activity.RESULT_OK) {// 拍照
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(context, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            context.startActivityForResult(intent, FLAG_MODIFY_FINISH);

        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                Log.i("===", "截取到的图片路径是 = " + path);

                Bitmap b = BitmapFactory.decodeFile(path);

                headerImageStr = bitmap2Base64(b);

                if (this.listener != null) {
                    this.listener.uploadImage(headerImageStr);
                }
            }
        }
    }

    private String bitmap2Base64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 下面是选择照片
     */
    private void choosePic() {
        try {
            final PopupWindow pop = new PopupWindow(this);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.popup_pick_photo, null);

            final LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.popupLayout);
            pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setFocusable(true);
            pop.setOutsideTouchable(true);
            pop.setContentView(view);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundRecovery();
                }
            });

            RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
            TextView cameraTextView = (TextView) view.findViewById(R.id.cameraTextView);
            TextView PhotoTextView = (TextView) view.findViewById(R.id.PhotoTextView);
            TextView cancelTextView = (TextView) view.findViewById(R.id.cancelTextView);
            rootLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
            });

            cameraTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pop.dismiss();
                    ll_popup.clearAnimation();

                    takePhoto();
                }
            });

            PhotoTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pop.dismiss();
                    ll_popup.clearAnimation();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    context.startActivityForResult(intent, FLAG_CHOOSE_IMG);
                }
            });

            cancelTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
            });

            pop.showAtLocation(findViewById(R.id.rootLayout), Gravity.BOTTOM, 0, 0);

            backgroundDarken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void backgroundDarken() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.6f;
        context.getWindow().setAttributes(lp);
    }

    private void backgroundRecovery() {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
    }

    public void takePhoto() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                localTempImageFileName = "";
                localTempImageFileName = String.valueOf((new Date()).getTime()) + ".png";
                File filePath = FILE_PIC_SCREENSHOT;
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(filePath, localTempImageFileName);
                // localTempImgDir和localTempImageFileName是自己定义的名字
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                context.startActivityForResult(intent, FLAG_CHOOSE_PHONE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
