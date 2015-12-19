package com.housekeeper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImSubAccountsAppDto;
import com.housekeeper.activity.gesture.GestureLockSetupActivity;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.activity.view.LoginAfterDialog;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.UMengPushUtil;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends BaseActivity implements OnClickListener {

    private TextView phoneNumTextView = null;
    private EditText codeEditText = null;
    private EditText pwdEditText = null;
    private EditText pwdConfirmEditText = null;
    private TextView protocolTextView = null;
    private Button nextBtn = null;
    private Button timeBtn = null;

    private String telphone = null;

    private int currentTime = Constants.SMS_MAX_TIME;

    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        telphone = this.getIntent().getStringExtra("telphone");
        if (telphone == null) {
            telphone = "";
        }

        initView();

        if (!TextUtils.isEmpty(telphone)) {
            requestSendSMS();
        }
    }

    private void initView() {
        ((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("用户注册");

        phoneNumTextView = (TextView) this.findViewById(R.id.phoneNumTextView);
        phoneNumTextView.setText(telphone);

        protocolTextView = (TextView) this.findViewById(R.id.protocolTextView);
        this.setClickableSpan();

        codeEditText = (EditText) this.findViewById(R.id.codeEditText);
        pwdEditText = (EditText) this.findViewById(R.id.pwdEditText);
        pwdConfirmEditText = (EditText) this.findViewById(R.id.pwdConfirmEditText);

        timeBtn = (Button) this.findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(this);
        timeBtn.setEnabled(false);
        timeBtn.setVisibility(View.INVISIBLE);

        nextBtn = (Button) this.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != timer) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextBtn:
                if (checkValue()) {
                    requestRegister();
                }
                break;

            case R.id.timeBtn:
                this.requestSendSMS();
                break;

            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    public void onBackPressed() {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    private void showSuccessDialog() {
        LoginAfterDialog dialog = new LoginAfterDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Intent intent = new Intent(RegisterActivity.this, KeeperMainActivity.class);
                RegisterActivity.this.startActivityForResult(intent, 0);

                RegisterActivity.this.setResult(RESULT_OK);
                RegisterActivity.this.finish();
            }
        });
        dialog.show();
    }

    private boolean checkValue() {
        if (TextUtils.isEmpty(codeEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(codeEditText);
            return false;

        } else if (codeEditText.getText().toString().trim().length() < 4) {
            Toast.makeText(this, "请输入4位验证码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(codeEditText);
            return false;

        } else if (TextUtils.isEmpty(pwdEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(pwdEditText);
            return false;

        } else if (pwdEditText.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "密码为6－20位", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(pwdEditText);
            return false;

        } else if (TextUtils.isEmpty(pwdConfirmEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(pwdConfirmEditText);
            return false;

        } else if (pwdConfirmEditText.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "密码为6－20位", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(pwdConfirmEditText);
            return false;

        } else if (!pwdEditText.getText().toString().trim().equals(pwdConfirmEditText.getText().toString().trim())) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            ViewUtil.shakeView(pwdConfirmEditText);
            return false;

        }

        return true;
    }

    private void requestRegister() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("telphone", telphone);
        tempMap.put("userType", Constants.ROLE);
        tempMap.put("password", pwdEditText.getText().toString());
        tempMap.put("vcode", codeEditText.getText().toString());
        tempMap.put("deviceToken", ActivityUtil.getSharedPreferences().getString(Constants.DEVICETOKEN, ""));

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_REGIST_PASSWORD_SET, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImSubAccountsAppDto.class);
                    AppMessageDto<ImSubAccountsAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        new UMengPushUtil().new AddAliasTask(RegisterActivity.this, telphone).execute();

                        ActivityUtil.getSharedPreferences().edit().putString(Constants.UserName, telphone).commit();

                        ActivityUtil.getSharedPreferences().edit().putString(Constants.USERID, dto.getData().getUserId()).commit();

                        showSuccessDialog();

                    } else {
                        Toast.makeText(RegisterActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void requestSendSMS() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("telphone", this.getIntent().getStringExtra("telphone"));
        tempMap.put("userType", Constants.ROLE);

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_REGIST_SMS_SEND, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    Toast.makeText(RegisterActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        // 启动定时器
                        startTimer();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void startTimer() {
        currentTime = Constants.SMS_MAX_TIME;
        timeBtn.setText(currentTime + " 秒后重发");
        timeBtn.setVisibility(View.VISIBLE);

        timeBtn.setEnabled(false);
        timeBtn.setTextColor(Color.parseColor("#999999"));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }

        }, 1000, 1000);
    }

    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    if (currentTime > 0) {
                        timeBtn.setText(--currentTime + " 秒后重发");

                    } else {
                        timer.cancel();
                        timer = null;

                        timeBtn.setEnabled(true);
                        timeBtn.setTextColor(Color.parseColor("#ffffff"));
                        timeBtn.setText("重新发送");
                    }

                    break;

                default:
                    break;
            }
        }
    };

    private void setClickableSpan() {
        String htmlLinkText = "注册即代表您同意《我有房租赁协议》";
        SpannableString spStr = new SpannableString(htmlLinkText);

        TouchableSpan clickSpan = new TouchableSpan();
        spStr.setSpan(clickSpan, 9, 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        protocolTextView.setText(spStr);
        protocolTextView.setMovementMethod(new LinkTouchMovementMethod());
    }

    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    public class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private int mNormalBackgroundColor;
        private int mPressedBackgroundColor;
        private int mNormalTextColor;
        private int mPressedTextColor;

        public TouchableSpan() {
            this(Color.parseColor("#1caff6"), Color.parseColor("#8dd9fd"), Color.parseColor("#ffffff"), Color.parseColor("#999999"));
        }

        public TouchableSpan(int normalTextColor, int pressedTextColor, int mNormalBackgroundColor, int pressedBackgroundColor) {
            mNormalTextColor = normalTextColor;
            mPressedTextColor = pressedTextColor;
            mPressedBackgroundColor = pressedBackgroundColor;
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : mNormalBackgroundColor;
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(RegisterActivity.this, ShowWebViewActivity.class);
            intent.putExtra("title", "我有房租赁协议");
            intent.putExtra("url", Constants.PROTOCOL_IP);
            RegisterActivity.this.startActivity(intent);
        }
    }

}
