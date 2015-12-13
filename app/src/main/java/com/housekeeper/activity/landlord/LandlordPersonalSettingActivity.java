package com.housekeeper.activity.landlord;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.ares.house.dto.app.MyAppAgentDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.CropImageActivity;
import com.housekeeper.activity.SetTransferPWDActivity;
import com.housekeeper.activity.VerifyEmergencyContactActivity;
import com.housekeeper.activity.VerifyHasSetTransferPWDActivity;
import com.housekeeper.activity.VerifyWechatActivity;
import com.housekeeper.activity.keeper.KeeperMeActivity;
import com.housekeeper.activity.view.CropImageView;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sth on 9/13/15.
 * <p/>
 * 房东 设置  个人设置
 */
public class LandlordPersonalSettingActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView headLogoView;
    private CustomNetworkImageView headImageView;
    private DavinciView transferPwdView;

    private DavinciView contactView;
    private DavinciView wechatView;

    private Map<String, String> statusMap = null;

    /* 头像上传相关 */
    private static String localTempImageFileName = "";
    private static final int FLAG_CHOOSE_IMG = 0x100;// 相册选择
    private static final int FLAG_CHOOSE_PHONE = 0x101;// 拍照
    private static final int FLAG_MODIFY_FINISH = 0x102;

    public static final File FILE_SDCARD = Environment.getExternalStorageDirectory();
    public static final String IMAGE_PATH = "gugu";
    public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
    public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL, "images/screenshots");

    private String headerImageStr = "";

    private MyAppAgentDto agentDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_landlord_personal_setting);

        agentDto = (MyAppAgentDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    public void onResume() {
        super.onResume();

        requestAllState();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("个人认证");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        headLogoView = (DavinciView) this.findViewById(R.id.headLogoView);
        headLogoView.getLogoImageView().setVisibility(View.GONE);
        headLogoView.getTitleTextView().setText("设置头像");
        headLogoView.getTipTextView().setText("");
        headLogoView.setOnClickListener(this);

        headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        headImageView.setErrorImageResId(R.drawable.user_default_head);
        headImageView.setDefaultImageResId(R.drawable.user_default_head);
        if (agentDto != null) {
            headImageView.setImageUrl(Constants.HOST_IP + agentDto.getLogoUrl() + "?random=" + ActivityUtil.getSharedPreferences().getString(Constants.HEAD_RANDOM, "0"), ImageCacheManager.getInstance().getImageLoader());
        }

        transferPwdView = (DavinciView) this.findViewById(R.id.transferPwdView);
        transferPwdView.getLogoImageView().setVisibility(View.GONE);
        transferPwdView.getTitleTextView().setText("交易密码");
        transferPwdView.getTipTextView().setText("");
        transferPwdView.setOnClickListener(this);

        contactView = (DavinciView) this.findViewById(R.id.contactView);
        contactView.getLogoImageView().setVisibility(View.GONE);
        contactView.getTitleTextView().setText("紧急联系人");
        contactView.getTipTextView().setText("");
        contactView.setOnClickListener(this);

        wechatView = (DavinciView) this.findViewById(R.id.wechatView);
        wechatView.getLogoImageView().setVisibility(View.GONE);
        wechatView.getTitleTextView().setText("微信认证");
        wechatView.getTipTextView().setText("");
        wechatView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.headLogoView: {
                CropImageView.CROP_TYPE = CropImageView.CROP_TYPE_HEADER;
                choosePic();
            }
            break;

            case R.id.transferPwdView: {
                // 如果已经设置了交易密码，则进入提示界面;如果没有设置则直接进入设置界面
                if (statusMap.get("TRANSACTION_PASSWORD").charAt(0) == 'a') {
                    Intent intent = new Intent(this, SetTransferPWDActivity.class);
                    intent.putExtra("TYPE", SetTransferPWDActivity.TYPE_SET);
                    intent.putExtra("loginPassword", ""); // 初次设置不需要登录密码；修改需要登录密码。
                    this.startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent(this, VerifyHasSetTransferPWDActivity.class);
                    this.startActivityForResult(intent, 0);
                }
            }
            break;

            case R.id.contactView: {
                Intent intent = new Intent(this, VerifyEmergencyContactActivity.class);
                this.startActivityForResult(intent, 0);
            }
            break;

            case R.id.wechatView: {
                Intent intent = new Intent(this, VerifyWechatActivity.class);
                intent.putExtra("status", statusMap.get("WEBCHAT").charAt(0));
                this.startActivityForResult(intent, 0);
            }
            break;
        }
    }

    private void requestSetUserLogo() {
        if (null == headerImageStr || TextUtils.isEmpty(headerImageStr))
            return;

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("logo", headerImageStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_SET_LOGO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    Toast.makeText(LandlordPersonalSettingActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        ActivityUtil.getSharedPreferences().edit().putString(Constants.HEAD_RANDOM, System.currentTimeMillis() + "").commit();

                        headImageView.setImageUrl(Constants.HOST_IP + dto.getData() + "?random=" + ActivityUtil.getSharedPreferences().getString(Constants.HEAD_RANDOM, "0"), ImageCacheManager.getInstance().getImageLoader());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在提交数据...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            requestAllState();

        } else if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) { // 选择图片
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (null == cursor) {
                        Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Log.i("===", "path=" + path);
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", path);
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Log.i("===", "path=" + uri.getPath());
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {// 拍照
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);

        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                Log.i("===", "截取到的图片路径是 = " + path);

                Bitmap b = BitmapFactory.decodeFile(path);

                headImageView.setImageBitmap(b);

                headerImageStr = bitmap2Base64(b);

                requestSetUserLogo();
            }
        }
    }

    private String bitmap2Base64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void requestAllState() {
        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        statusMap = dto.getData();

                        responseAllState();

                    } else {
                        hideAllState();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
    }

    private void responseAllState() {
        try {
            transferPwdView.getTipTextView().setText(statusMap.get("TRANSACTION_PASSWORD").charAt(0) == 'a' ? "未设置" : "已设置");
            contactView.getTipTextView().setText(statusMap.get("EMERGENCY_CONTACT").charAt(0) == 'a' ? "未设置" : "已设置");
            wechatView.getTipTextView().setText(getStateMsg(statusMap.get("WEBCHAT").charAt(0)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * a未提交 b已提交待审核 c审核失败 d审核中 e审核通过
     */
    private String getStateMsg(char c) {
        String msg = "未提交";

        switch (c) {
            case 'a':
                msg = "未提交";
                break;

            case 'b':
                msg = "已提交";
                break;

            case 'c':
                msg = "审核失败";
                break;

            case 'd':
                msg = "审核中";
                break;

            case 'e':
                msg = "已设置";
                break;

            default:
                msg = "未知状态";
                break;
        }
        return msg;
    }

    private void hideAllState() {
        transferPwdView.getTipTextView().setText("");
        contactView.getTipTextView().setText("");
        wechatView.getTipTextView().setText("");
    }

    /**
     * 用户头像上传
     */
    /**
     * 下面是选择照片
     */
    private void choosePic() {
        try {
            final PopupWindow pop = new PopupWindow(this);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivityForResult(intent, FLAG_CHOOSE_IMG);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
    }

    private void backgroundRecovery() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(filePath, localTempImageFileName);
                // localTempImgDir和localTempImageFileName是自己定义的名字
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, FLAG_CHOOSE_PHONE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
