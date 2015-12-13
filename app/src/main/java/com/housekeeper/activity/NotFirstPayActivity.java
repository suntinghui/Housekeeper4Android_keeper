package com.housekeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.TransferInfo;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.model.BankEntityEx;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.BankUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 非首次转账交易
 *
 * @author sth
 */
public class NotFirstPayActivity extends BaseActivity implements OnClickListener {

    private TextView totalMoneyTextView; // 投资总金额
    private TextView balanceMoneyTextView; // 余额支付金额
    private TextView surplusMoneyTextView; // 银行卡支付金额

    private TextView realnameTextView;
    private ImageView bankLogoImageView;
    private TextView cardTextView;
    private TextView bankLimitTextView;
    private Button confirmBtn;
    private EditText codeEditText;
    private Button timeBtn;

    private TransferInfo transferInfo = null;
    private HashMap<String, String> transferMap = null;

    // 请求验证码返回的id值作为支付的债权包标识。
    private String reponseId = "";

    // 银行短信验证码时间为60s
    private int currentTime = Constants.SMS_MAX_TIME;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_not_first_pay);

        transferInfo = (TransferInfo) this.getIntent().getSerializableExtra("INFO");
        transferMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("MAP");

        initView();

        setValue();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("投资");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);

        totalMoneyTextView = (TextView) this.findViewById(R.id.totalMoneyTextView);
        balanceMoneyTextView = (TextView) this.findViewById(R.id.balanceMoneyTextView);
        surplusMoneyTextView = (TextView) this.findViewById(R.id.surplusMoneyTextView);

        realnameTextView = (TextView) this.findViewById(R.id.realnameTextView);
        bankLogoImageView = (ImageView) this.findViewById(R.id.bankLogoImageView);
        cardTextView = (TextView) this.findViewById(R.id.cardTextView);
        bankLimitTextView = (TextView) this.findViewById(R.id.bankLimitTextView);

        codeEditText = (EditText) this.findViewById(R.id.codeEditText);

        timeBtn = (Button) this.findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(this);
    }

    private void setValue() {
        totalMoneyTextView.setText(transferInfo.getTransferMoney());
        balanceMoneyTextView.setText(transferInfo.getNeedBalanceStr(transferInfo.isUseBalance()));
        surplusMoneyTextView.setText(transferInfo.getSurplusMoneyStr(transferInfo.isUseBalance()));

        if (transferMap == null)
            return;

        // 身份信息
        String realName = transferMap.get("BANK_REALNAME");
        String telphone = transferMap.get("BANK_PHONE");

        BankEntityEx bank = null;

        try {
            bank = BankUtil.getBankFromCode(transferMap.get("BANK_ID"), this);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "努力加载数据中，请稍候", Toast.LENGTH_SHORT).show();

            this.finish();
        }

        String tailNum = transferMap.get("BANK_CARD").substring(transferMap.get("BANK_CARD").length() - 4);

        realnameTextView.setText(realName + " (" + telphone + ")");
        cardTextView.setText(bank.getName() + " (尾号 " + tailNum + ")");
        bankLogoImageView.setBackgroundResource(bank.getLogoId());
        bankLimitTextView.setText(bank.getLimitStr());
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
            case R.id.backBtn:
                this.setResult(RESULT_CANCELED);
                this.finish();
                break;

            case R.id.timeBtn: // 发送短信验证码
                requestSendCode();
                break;

            case R.id.confirmBtn: // 支付
                if (checkValueForRequestPay()) {
                    requestPay();
                }
                break;
        }
    }

    private boolean checkValueForRequestPay() {
        String code = codeEditText.getText().toString().trim();
        if (TextUtils.isEmpty(this.reponseId)) {
            Toast.makeText(this, "请发送验证码", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(this.timeBtn);
            return false;

        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入收到的短信验证码", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(codeEditText);
            return false;

        } else if (code.length() < 6) {
            Toast.makeText(this, "请输入6位短信验证码", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(codeEditText);
            return false;
        }

        return true;
    }

    private HashMap<String, String> getRequestMap() {

        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", transferInfo.getId());
        //tempMap.put("money", transferInfo.getTransferMoney());
        tempMap.put("isSurplus", String.valueOf(transferInfo.isUseBalance()));
        //tempMap.put("password", "");

        return tempMap;
    }

    private void requestSendCode() {
        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_PAY_RENT_SENDVCODE, getRequestMap(), false, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Integer.class);
                    AppMessageDto<Integer> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(NotFirstPayActivity.this, "验证码已发送至您的手机", Toast.LENGTH_SHORT).show();

                        reponseId = String.valueOf(dto.getData());

                        // 启动定时器
                        startTimer();

                    } else {
                        showMessage(dto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new ResponseErrorListener(this));

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestPay() {
        final HashMap<String, String> tempMap = this.getRequestMap();
        tempMap.put("houseId", transferInfo.getId() + "");
        tempMap.put("isSurplus", String.valueOf(transferInfo.isUseBalance()));
        tempMap.put("vcode", codeEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_PAY_RENT, tempMap, false, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        // 交易成功，跳转到成功界面
                        String cardNum = transferMap.get("BANK_CARD");
                        String bankName = BankUtil.getBankFromCode(transferMap.get("BANK_ID"), NotFirstPayActivity.this).getName();
                        transferInfo.setTailNum(cardNum.substring(cardNum.length() - 4));
                        transferInfo.setBankName(bankName);

                        Intent intent = new Intent(NotFirstPayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("TYPE", PaySuccessActivity.TYPE_BANKCARD);
                        intent.putExtra("INFO", transferInfo);
                        intent.putExtra("SHAKE", dto.getData());
                        NotFirstPayActivity.this.startActivityForResult(intent, 0);

                    } else {
                        showMessage(dto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new ResponseErrorListener(this));

        this.addToRequestQueue(request, "正在支付，请稍候...");
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

    private void showMessage(String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(null).setContentText("\n" + msg + "\n").setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.setResult(RESULT_OK);
            this.finish();
        }
    }

}
