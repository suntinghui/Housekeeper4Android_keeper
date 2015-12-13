package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.RoleTypeEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

public class RegisterAndLoginActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener, TextWatcher {

    private LinearLayout phoneLayout = null;
    private EditText phoneEditText = null;
    private TextView tipTextView = null;
    private Button clearBtn = null;
    private Button nextBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_login);

        initView();
    }

    private void initView() {
        ((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("登录/注册");

        phoneLayout = (LinearLayout) this.findViewById(R.id.phoneLayout);

        phoneEditText = (EditText) this.findViewById(R.id.phoneEditText);
        phoneEditText.setOnFocusChangeListener(this);
        phoneEditText.addTextChangedListener(this);

        tipTextView = (TextView) this.findViewById(R.id.tipTextView);

        clearBtn = (Button) this.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);

        nextBtn = (Button) this.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearBtn:
                phoneEditText.setText("");
                break;

            case R.id.nextBtn:
                if (checkValue()) {
                    requestCheckRegi();
                }
                break;

            case R.id.backBtn:
                backAction();
                break;
        }

    }

    public void onBackPressed() {
        backAction();
    }

    private void backAction() {
        // 切到首页
//		Intent intent1 = new Intent(MainActivity.ACTION_CHECK_RELATION);
//		intent1.putExtra("INDEX", 0);
//		sendBroadcast(intent1);

        this.finish();
    }

    private boolean checkValue() {
        String telphone = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(telphone.trim())) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(phoneEditText);
            return false;

        } else if (telphone.trim().length() != 11) {
            Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(phoneEditText);
            return false;
        }

        return true;
    }

    private void requestCheckRegi() {
        final String telphone = phoneEditText.getText().toString();

        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("telphone", telphone);
        tempMap.put("userType", ActivityUtil.getSharedPreferences().getString(Constants.kCURRENT_TYPE, RoleTypeEnum.KEEPER));

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_REGIST_CHECK_TELPHONE, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    ActivityUtil.getSharedPreferences().edit().putString(Constants.UserName, telphone).commit();

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        // 未注册
                        Intent intent = new Intent(RegisterAndLoginActivity.this, RegisterActivity.class);
                        intent.putExtra("telphone", telphone);
                        RegisterAndLoginActivity.this.startActivityForResult(intent, 0);

                    } else {
                        // 已经注册
                        Intent intent = new Intent(RegisterAndLoginActivity.this, LoginActivity.class);
                        RegisterAndLoginActivity.this.startActivityForResult(intent, 0);
                        RegisterAndLoginActivity.this.finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.phoneEditText) {
            phoneLayout.setSelected(hasFocus);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        if (str.length() > 0) {
            tipTextView.setVisibility(View.VISIBLE);
            clearBtn.setVisibility(View.VISIBLE);
            tipTextView.setText(getTipText(str));
        } else {
            tipTextView.setVisibility(View.GONE);
            clearBtn.setVisibility(View.INVISIBLE);
        }

    }

    private String getTipText(String text) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i));
            if (i == 2 || i == 6) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.finish();
        }
    }

}
