package com.housekeeper.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 设置交易密码
 *
 * @author sth
 */
public class SetTransferPWDActivity extends BaseActivity implements OnClickListener {

    private EditText newPwdEditText;
    private EditText confirmNewPwdEditText;
    private Button nextBtn;

    private int type = TYPE_SET;
    public static final int TYPE_UPDATE = 0x01;
    public static final int TYPE_SET = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_transfer_pwd);

        type = this.getIntent().getIntExtra("TYPE", TYPE_SET);

        initView();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("设置交易密码");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        newPwdEditText = (EditText) this.findViewById(R.id.newPwdEditText);
        newPwdEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        confirmNewPwdEditText = (EditText) this.findViewById(R.id.confirmNewPwdEditText);
        confirmNewPwdEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        nextBtn = (Button) this.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;

            case R.id.nextBtn:
                if (checkValue()) {
                    requestSetPwd();
                }
                break;
        }
    }

    private boolean checkValue() {
        String newPwdStr = newPwdEditText.getText().toString();
        String confirmPwdStr = confirmNewPwdEditText.getText().toString();

        if (TextUtils.isEmpty(newPwdStr)) {
            Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(newPwdEditText);
            return false;

        } else if (newPwdStr.length() < 6) {
            Toast.makeText(this, "密码为6位数字", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(newPwdEditText);
            return false;

        } else if (TextUtils.isEmpty(confirmPwdStr)) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(confirmNewPwdEditText);
            return false;

        } else if (confirmPwdStr.length() < 6) {
            Toast.makeText(this, "密码为6－20位", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(confirmNewPwdEditText);
            return false;

        } else if (!newPwdStr.equals(confirmPwdStr)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(confirmNewPwdEditText);
            return false;

        }

        return true;
    }

    private void requestSetPwd() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("value", newPwdEditText.getText().toString());

        String loginPassword = this.getIntent().getStringExtra("loginPassword");
        map.put("loginPassword", loginPassword == null ? "" : loginPassword);

        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_TRANSACTION_PASSWORD_SAVE, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        new SweetAlertDialog(SetTransferPWDActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(null).setContentText("交易密码设置成功！").setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();

                                SetTransferPWDActivity.this.setResult(RESULT_OK);
                                SetTransferPWDActivity.this.finish();
                            }
                        }).show();
                    } else {
                        Toast.makeText(SetTransferPWDActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在处理请稍候...");
    }

}
