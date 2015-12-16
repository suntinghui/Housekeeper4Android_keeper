package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * 验证登录密码
 *
 * @author sth
 */
public class VerifyLoginPWDActivity extends BaseActivity implements OnClickListener {

    private EditText pwdEditText;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_login_pwd);

        initView();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("验证登录密码");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);

        pwdEditText = (EditText) this.findViewById(R.id.pwdEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;

            case R.id.confirmBtn:
                if (checkValue()) {
                    requestVerifyLoginPwd();
                }
                break;
        }

    }

    private boolean checkValue() {
        String pwd = pwdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(pwdEditText);
            return false;
        } else if (pwd.length() < 6) {
            Toast.makeText(this, "登录密码为6－20位", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(pwdEditText);
            return false;
        }

        return true;
    }

    private void requestVerifyLoginPwd() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("password", pwdEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_PASSWORD_VALID, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Intent intent = new Intent(VerifyLoginPWDActivity.this, SetTransferPWDActivity.class);
                        intent.putExtra("TYPE", SetTransferPWDActivity.TYPE_UPDATE);
                        intent.putExtra("loginPassword", pwdEditText.getText().toString().trim());
                        VerifyLoginPWDActivity.this.startActivityForResult(intent, 0);

                    } else {
                        Toast.makeText(VerifyLoginPWDActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.setResult(RESULT_OK);
            this.finish();
        }
    }

}
