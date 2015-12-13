package com.housekeeper.activity;

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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.BankEntityEx;
import com.housekeeper.utils.BankUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * 已绑定银行卡
 *
 * @author sth
 */
public class BindedBankActivity extends BaseActivity implements OnClickListener {

    private ImageView bankLogoImageView;
    private TextView bankNameTextView;
    private TextView statusTextView;
    private TextView bankNumTextView;
    private TextView unbindTextView;
    private TextView bottomTipTextView;

    private VerifyTransferPWDDialog verifyTransferPwdDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_binded_bank);

        initView();
    }

    private void initView() {
        try {
            TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
            titleTextView.setText("银行卡");

            Button backButton = (Button) this.findViewById(R.id.backBtn);
            backButton.setOnClickListener(this);

            bankLogoImageView = (ImageView) this.findViewById(R.id.bankLogoImageView);
            bankNameTextView = (TextView) this.findViewById(R.id.bankNameTextView);
            statusTextView = (TextView) this.findViewById(R.id.statusTextView);
            bankNumTextView = (TextView) this.findViewById(R.id.bankNumTextView);
            unbindTextView = (TextView) this.findViewById(R.id.unbindTextView);
            unbindTextView.setOnClickListener(this);
            bottomTipTextView = (TextView) this.findViewById(R.id.bottomTipTextView);

            HashMap<String, String> map = (HashMap<String, String>) this.getIntent().getSerializableExtra("MAP");

            BankEntityEx bank = BankUtil.getBankFromCode(map.get("BANK_ID"), this);
            bankLogoImageView.setBackgroundResource(bank.getLogoId());
            bankNameTextView.setText(bank.getName());
            char BANK_CARD_STATUS = map.get("BANK_CARD_STATUS").charAt(0);
            statusTextView.setText(BANK_CARD_STATUS == 'e' ? "已绑定" : "确定中");
            bankNumTextView.setText("尾号：" + map.get("BANK_CARD").substring(map.get("BANK_CARD").length() - 4));

            this.setClickableSpan();
        } catch(Exception e){
            e.printStackTrace();

            Toast.makeText(this, "努力加载数据中，请稍候", Toast.LENGTH_SHORT).show();

            this.finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;

            case R.id.unbindTextView:
                unbindAction();
                break;
        }

    }

    private void unbindAction() {
        verifyTransferPwdDialog = new VerifyTransferPWDDialog(this);
        verifyTransferPwdDialog.setTitle("确定解绑该银行卡吗？");
        verifyTransferPwdDialog.setTip("我们不会保留您的银行卡信息。");
        verifyTransferPwdDialog.setOnConfirmListener(new VerifyTransferPWDDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String pwdStr) {
                requestUnbind(pwdStr);
            }
        });
        verifyTransferPwdDialog.show();
    }

    // 解绑
    private void requestUnbind(String pwdStr) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("password", pwdStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_UNBIND, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(BindedBankActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();

                        verifyTransferPwdDialog.dismiss();

                        BindedBankActivity.this.finish();

                    } else {

                        verifyTransferPwdDialog.setError(dto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void setClickableSpan() {
        String htmlLinkText = "为了您的资金安全，如果需要修改银行卡信息请致电客服。";

        SpannableString spStr = new SpannableString(htmlLinkText);

        TouchableSpan clickSpan = new TouchableSpan();
        spStr.setSpan(clickSpan, 21, 25, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

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
