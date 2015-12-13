package com.housekeeper.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信认证
 *
 * @author sth
 */
public class VerifyWechatActivity extends BaseActivity implements OnClickListener {

    private EditText wechatEditText;
    private TextView topTipTextView;
    private TextView bottomTipTextView;
    private TextView copyTextView;
    private Button confirmBtn;

    /**
     * a未提交 b已提交待审核 c审核失败 d审核中 e审核通过
     */
    private char status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_wechat);

        status = this.getIntent().getCharExtra("status", 'a');

        initView();

        requestWechatValue();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("微信认证");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);

        topTipTextView = (TextView) this.findViewById(R.id.topTipTextView);
        bottomTipTextView = (TextView) this.findViewById(R.id.bottomTipTextView);

        copyTextView = (TextView) this.findViewById(R.id.copyTextView);
        copyTextView.setOnClickListener(this);

        wechatEditText = (EditText) this.findViewById(R.id.wechatEditText);

        if (status < 'd') {
            wechatEditText.setEnabled(true);

            wechatEditText.setTextColor(getResources().getColor(R.color.gray_333333));

            confirmBtn.setVisibility(View.VISIBLE);

            topTipTextView.setVisibility(View.VISIBLE);
            bottomTipTextView.setVisibility(View.GONE);

        } else {
            wechatEditText.setEnabled(false);

            wechatEditText.setTextColor(getResources().getColor(R.color.gray));

            confirmBtn.setVisibility(View.INVISIBLE);

            topTipTextView.setVisibility(View.GONE);
            bottomTipTextView.setVisibility(View.VISIBLE);

            this.setClickableSpan();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;

            case R.id.copyTextView:
                copy2Clipboard();
                break;

            case R.id.confirmBtn:
                if (checkValue()) {
                    requestSaveWechat();
                }
                break;
        }

    }

    private void copy2Clipboard() {
        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("wechat", "gugulicai"));

        Toast.makeText(this, "公众号已复制到剪贴板，请到微信中添加朋友，粘贴后搜索即可。", Toast.LENGTH_LONG).show();
    }

    private boolean checkValue() {
        String wechat = wechatEditText.getText().toString().trim();

        if (TextUtils.isEmpty(wechat)) {
            Toast.makeText(this, "请输入微信号", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(wechatEditText);
            return false;

        }

        return true;
    }

    // 查询数据
    private void requestWechatValue() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", "WEBCHAT");

        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_ITEM_INFO, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Map<String, String> map = dto.getData();
                        if (status < 'd') {
                            wechatEditText.setText(map.get("WEBCHAT"));
                            wechatEditText.setSelection(wechatEditText.getText().toString().length());
                        } else {
                            wechatEditText.setText(map.get("WEBCHAT"));
                        }

                    } else {
                        Toast.makeText(VerifyWechatActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestSaveWechat() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", "WEBCHAT");
        tempMap.put("value", wechatEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_ITEM_SAVE, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    Toast.makeText(VerifyWechatActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        VerifyWechatActivity.this.finish();

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    // ///////////////////////

    private void setClickableSpan() {
        String htmlLinkText = null;
        if (status == 'd') {
            htmlLinkText = "您提交的信息已经在审核中，如果需要修改请致电客服。";
        } else {
            htmlLinkText = "您提交的信息已经通过审核，如果需要修改请致电客服。";
        }

        SpannableString spStr = new SpannableString(htmlLinkText);

        TouchableSpan clickSpan = new TouchableSpan();
        spStr.setSpan(clickSpan, 20, 24, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        bottomTipTextView.setText(spStr);
        bottomTipTextView.setMovementMethod(new LinkTouchMovementMethod());
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
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.PHONE_SERVICE));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
