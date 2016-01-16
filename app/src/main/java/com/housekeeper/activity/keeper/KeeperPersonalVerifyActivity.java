package com.housekeeper.activity.keeper;

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
import com.ares.house.dto.app.UserInfoStatusAppDto;
import com.housekeeper.HousekeeperApplication;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.BindedBankActivity;
import com.housekeeper.activity.BindingBankActivity;
import com.housekeeper.activity.CropImageActivity;
import com.housekeeper.activity.ManageGestureLockActivity;
import com.housekeeper.activity.ModifyLoginPWDActivity;
import com.housekeeper.activity.SetTransferPWDActivity;
import com.housekeeper.activity.VerifyEmergencyContactActivity;
import com.housekeeper.activity.VerifyHasSetTransferPWDActivity;
import com.housekeeper.activity.view.CropImageView;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
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
 * <p>
 * 租户  我  个人认证
 */
public class KeeperPersonalVerifyActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView headLogoView;
    private CustomNetworkImageView headImageView;
    private DavinciView transferPwdView;
    private DavinciView modifyLoginPwdView;
    private DavinciView modifyGestureLockView;

    private DavinciView bankCardView;

    private DavinciView cardIdView;
    private DavinciView jobPhotoView;

    private MyAppAgentDto agentDto = null;

    private Map<String, String> statusMap = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_personal_verify);

        agentDto = (MyAppAgentDto) this.getIntent().getSerializableExtra("DTO");

        if (this.getIntent().getBooleanExtra("exception", false)) {
            Toast.makeText(this, "请先完善个人认证信息", Toast.LENGTH_SHORT).show();
        }

        this.initView();
    }

    public void onResume() {
        super.onResume();

        requestUserInfoStatus();

        requestAllState();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("个人认证");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        headLogoView = (DavinciView) this.findViewById(R.id.headLogoView);
        headLogoView.getLogoImageView().setVisibility(View.GONE);
        headLogoView.getTitleTextView().setText("设置头像");
        headLogoView.getImportantTextView().setVisibility(View.VISIBLE);
        headLogoView.getTipTextView().setText("");
        headLogoView.setOnClickListener(this);

        headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        headImageView.setErrorImageResId(R.drawable.head_keeper_default);
        headImageView.setDefaultImageResId(R.drawable.head_keeper_default);
        if (agentDto != null) {
            headImageView.setImageUrl(Constants.HOST_IP + agentDto.getLogoUrl() + "?random=" + ActivityUtil.getSharedPreferences().getString(Constants.HEAD_RANDOM, "0"), ImageCacheManager.getInstance().getImageLoader());
        }

        transferPwdView = (DavinciView) this.findViewById(R.id.transferPwdView);
        transferPwdView.getLogoImageView().setVisibility(View.GONE);
        transferPwdView.getTitleTextView().setText("交易密码");
        transferPwdView.getImportantTextView().setVisibility(View.VISIBLE);
        transferPwdView.getTipTextView().setText("");
        transferPwdView.setOnClickListener(this);

        modifyLoginPwdView = (DavinciView) this.findViewById(R.id.modifyLoginPwdView);
        modifyLoginPwdView.getLogoImageView().setVisibility(View.GONE);
        modifyLoginPwdView.getTitleTextView().setText("修改登录密码");
        modifyLoginPwdView.getTipTextView().setText("");
        modifyLoginPwdView.setOnClickListener(this);

        modifyGestureLockView = (DavinciView) this.findViewById(R.id.modifyGestureLockView);
        modifyGestureLockView.getLogoImageView().setVisibility(View.GONE);
        modifyGestureLockView.getTitleTextView().setText("管理手势密码");
        modifyGestureLockView.getTipTextView().setText("");
        modifyGestureLockView.setOnClickListener(this);

        bankCardView = (DavinciView) this.findViewById(R.id.bankCardView);
        bankCardView.getLogoImageView().setVisibility(View.GONE);
        bankCardView.getTitleTextView().setText("银行卡");
        bankCardView.getImportantTextView().setVisibility(View.VISIBLE);
        bankCardView.getTipTextView().setText("");
        bankCardView.setOnClickListener(this);

        cardIdView = (DavinciView) this.findViewById(R.id.cardIdView);
        cardIdView.getLogoImageView().setVisibility(View.GONE);
        cardIdView.getTitleTextView().setText("身份证照片");
        cardIdView.getImportantTextView().setVisibility(View.VISIBLE);
        cardIdView.getTipTextView().setText("");
        cardIdView.setOnClickListener(this);

        jobPhotoView = (DavinciView) this.findViewById(R.id.jobPhotoView);
        jobPhotoView.getLogoImageView().setVisibility(View.GONE);
        jobPhotoView.getTitleTextView().setText("工作证照片");
        jobPhotoView.getImportantTextView().setVisibility(View.VISIBLE);
        jobPhotoView.getTipTextView().setText("");
        jobPhotoView.setOnClickListener(this);
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
                if (statusMap == null)
                    return;

                // 如果已经设置了交易密码，则进入提示界面;如果没有设置则直接进入设置界面
                if (StringUtils.isBlank(statusMap.get("TRANSACTION_PASSWORD"))) {
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

            case R.id.modifyLoginPwdView: { // 修改登录密码
                Intent intent = new Intent(this, ModifyLoginPWDActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.modifyGestureLockView: { // 管理手势密码
                Intent intent = new Intent(this, ManageGestureLockActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.bankCardView: {
                requestBankInfo();
            }
            break;

            case R.id.cardIdView: {
                Intent intent = new Intent(this, KeeperIDCardActivity.class);
                this.startActivity(intent);
            }
            break;

            case R.id.jobPhotoView: {
                Intent intent = new Intent(this, KeeperEmployeeCardActivity.class);
                this.startActivity(intent);
            }
            break;
        }
    }

    // 获取个人信息状态
    private void requestUserInfoStatus() {
        JSONRequest request = new JSONRequest(this, RequestEnum.USER_INFO_STATUS, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, UserInfoStatusAppDto.class);
                    AppMessageDto<UserInfoStatusAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        UserInfoStatusAppDto statusDto = dto.getData();

                        headLogoView.getImportantTextView().setVisibility(statusDto.isUserLogo() ? View.VISIBLE : View.GONE);
                        transferPwdView.getImportantTextView().setVisibility(statusDto.isTransactionPassword() ? View.VISIBLE : View.GONE);
                        bankCardView.getImportantTextView().setVisibility(statusDto.isBankCard() ? View.VISIBLE : View.GONE);
                        cardIdView.getImportantTextView().setVisibility(statusDto.isIdcard() ? View.VISIBLE : View.GONE);
                        jobPhotoView.getImportantTextView().setVisibility(statusDto.isWork() ? View.VISIBLE : View.GONE);

                    } else {
                        Toast.makeText(KeeperPersonalVerifyActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
    }

    private void requestBankInfo() {
        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        HashMap<String, String> map = (HashMap<String, String>) dto.getData();
                        String bank_id = map.get("BANK_ID");
                        if (null == bank_id || TextUtils.isEmpty(bank_id) || TextUtils.equals(bank_id, "null")) {
                            Intent intent = new Intent(KeeperPersonalVerifyActivity.this, BindingBankActivity.class);
                            KeeperPersonalVerifyActivity.this.startActivityForResult(intent, 0);

                        } else {
                            Intent intent = new Intent(KeeperPersonalVerifyActivity.this, BindedBankActivity.class);
                            intent.putExtra("MAP", map);
                            KeeperPersonalVerifyActivity.this.startActivityForResult(intent, 0);
                        }

                    } else {
                        Toast.makeText(KeeperPersonalVerifyActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
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

                    Toast.makeText(KeeperPersonalVerifyActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

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
        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_INFO, null, new Response.Listener<String>() {

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
            transferPwdView.getTipTextView().setText(StringUtils.isBlank(statusMap.get("TRANSACTION_PASSWORD")) ? "未设置" : "已设置");

            // 绑定银行卡状态 a未绑定 c绑定失败 d确认中 e已绑定
            String status = "";
            switch (statusMap.get("BANK_CARD_STATUS").charAt(0)) {
                case 'a':
                    status = "未绑定";
                    break;

                case 'c':
                    status = "绑定失败";
                    break;

                case 'd':
                    status = "确认中";
                    break;

                case 'e':
                    status = "已绑定";
                    break;
            }
            bankCardView.getTipTextView().setText(status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideAllState() {
        transferPwdView.getTipTextView().setText("");
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
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
