package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.ares.house.dto.app.WithdrawalAppDto;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.BankEntityEx;
import com.housekeeper.utils.BankUtil;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 我要提现
 *
 * @author sth
 */
public class WithdrawalsActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private TextView moneyTextView; // 账户余额
    private TextView balanceTextView; // 可提现余额
    private ImageView bankLogoImageView;
    private TextView bankNameTextView;
    private TextView tailNumTextView;
    private EditText amountEditText;
    private TextView surplusCountTextView;
    private TextView tipTextView;
    private Button applyBtn;

    private HashMap<String, String> bankInfoMap = null;

    private double balanceMoney = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_withdrawal);

        initView();

        requestBankInfo();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("我要提现");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        TextView branchInfoTextView = (TextView) this.findViewById(R.id.branchInfoTextView);
        branchInfoTextView.setOnClickListener(this);

        moneyTextView = (TextView) this.findViewById(R.id.moneyTextView);
        balanceTextView = (TextView) this.findViewById(R.id.balanceTextView);
        bankLogoImageView = (ImageView) this.findViewById(R.id.bankLogoImageView);
        bankNameTextView = (TextView) this.findViewById(R.id.bankNameTextView);
        tailNumTextView = (TextView) this.findViewById(R.id.tailNumTextView);
        amountEditText = (EditText) this.findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(this);
        surplusCountTextView = (TextView) this.findViewById(R.id.surplusCountTextView);
        tipTextView = (TextView) this.findViewById(R.id.tipTextView);

        applyBtn = (Button) this.findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);
        applyBtn.setEnabled(false);
    }

    // 查询银行卡信息
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
                        bankInfoMap = (HashMap<String, String>) dto.getData();

                        responseBankInfo();

                        checkTransferPwd();

                    } else {
                        Toast.makeText(WithdrawalsActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在处理请稍候...");
    }

    private boolean checkTransferPwd() {
        String TRANSACTION_PASSWORD = bankInfoMap.get("TRANSACTION_PASSWORD");
        if (TRANSACTION_PASSWORD == null) {
            // 没有设置交易密码，去设置。设置成功后，需要再手动触发一下。
            Intent intent = new Intent(WithdrawalsActivity.this, SetTransferPWDActivity.class);
            intent.putExtra("TYPE", SetTransferPWDActivity.TYPE_SET);
            WithdrawalsActivity.this.startActivity(intent);
            return false;
        }

        return true;
    }

    private void responseBankInfo() {
        String BANK_ID = bankInfoMap.get("BANK_ID");
        if (BANK_ID == null || TextUtils.isEmpty(BANK_ID)) {

            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("\n您还没有绑定银行卡信息").setContentText("").setConfirmText("立即绑定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.cancel();

                    Intent intent = new Intent(WithdrawalsActivity.this, BindingBankActivity.class);
                    intent.putExtra("MAP", bankInfoMap);
                    WithdrawalsActivity.this.startActivity(intent);

                    WithdrawalsActivity.this.finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();

        } else {
            requestBalance();

            BankEntityEx bank = BankUtil.getBankFromCode(BANK_ID, this);
            bankLogoImageView.setBackgroundResource(bank.getLogoId());
            bankNameTextView.setText(bank.getName());

            String bankCard = bankInfoMap.get("BANK_CARD");
            tailNumTextView.setText("（尾号：" + bankCard.substring(bankCard.length() - 4) + "）");
        }
    }

    // 查询余额
    private void requestBalance() {
        JSONRequest request = new JSONRequest(this, RequestEnum.WITHDRAWAL_SURPLUS, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, WithdrawalAppDto.class);
                    AppMessageDto<WithdrawalAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        moneyTextView.setText(dto.getData().getTotalMoney() + "");

                        balanceMoney = Double.parseDouble(dto.getData().getMoney());
                        balanceTextView.setText(dto.getData().getMoney());

                        String text1 = "<font color=#999999>本月免费提现剩 </font><font color=#1caff6>" + dto.getData().getNoServiceMoneyCount() + "</font> <font color=#999999> 次</font>";
                        surplusCountTextView.setText(Html.fromHtml(text1));

                        tipTextView.setText("提现收取2元手续费，每月前" + dto.getData().getNoServiceMoneyTotal() + "笔免费。\n15:00前提现当日到账，15:00后提现次日到账，节假日顺延。");

                    } else {
                        Toast.makeText(WithdrawalsActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

                        balanceTextView.setText("未知");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    // 提现
    private void requestTransfer(String pwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("money", amountEditText.getText().toString());
        map.put("password", pwd);

        JSONRequest request = new JSONRequest(this, RequestEnum.WITHDRAWAL_APPLY, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        SweetAlertDialog dialog = new SweetAlertDialog(WithdrawalsActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("\n提现申请成功").setContentText("提现进度请至\"提现记录\"中查询").setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();

                                WithdrawalsActivity.this.finish();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();

                    } else {
                        Toast.makeText(WithdrawalsActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.branchInfoTextView:
                gotoBranchActivity();
                break;

            case R.id.applyBtn:
                if (StringUtils.isBlank(bankInfoMap.get("BANK_AREA_ID"))) {
                    Toast.makeText(this, "请先设置开户行信息", Toast.LENGTH_SHORT).show();

                    gotoBranchActivity();

                } else {
                    if (this.checkTransferPwd()) {
                        showTransferPwd();
                    }
                }

                break;
        }
    }

    private void gotoBranchActivity() {
        Intent intent = new Intent(this, SetBranchActivity.class);
        intent.putExtra("BANK_INFO", bankInfoMap);
        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.requestBankInfo();
        }
    }

    private void showTransferPwd() {
        final VerifyTransferPWDDialog verifyTransferPwdDialog = new VerifyTransferPWDDialog(this);
        verifyTransferPwdDialog.setTitle("提示");
        verifyTransferPwdDialog.setTip(null);
        verifyTransferPwdDialog.setOnConfirmListener(new VerifyTransferPWDDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String pwdStr) {
                requestTransfer(pwdStr);

                verifyTransferPwdDialog.dismiss();
            }
        });
        verifyTransferPwdDialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            double temp = Double.parseDouble(s.toString());
            applyBtn.setEnabled(temp <= balanceMoney);

        } catch (Exception e) {
            e.printStackTrace();
            applyBtn.setEnabled(false);
        }
    }

}
