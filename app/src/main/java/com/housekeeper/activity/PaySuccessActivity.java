package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeeper.client.TransferInfo;
import com.wufriends.housekeeper.keeper.R;

public class PaySuccessActivity extends BaseActivity implements OnClickListener {

    private TextView totalMoneyTextView; // 投资总金额
    private TextView balanceMoneyTextView; // 余额支付金额
    private TextView balanceTextView; // 投资后账户余额
    private TextView surplusMoneyTextView; // 银行卡支付金额
    private TextView bankNameTextView; // 银行名称
    private TextView tailNumTextView; // 银行尾号
    private LinearLayout surplusMoneyLayout;
    private LinearLayout bankNameLayout;
    private Button doneBtn;

    private TransferInfo transferInfo = null;

    private int type = TYPE_BALANCE;
    public static final int TYPE_BALANCE = 0x001; // 余额全额支付
    public static final int TYPE_BANKCARD = 0x002; // 使用到了银行卡支付

    private boolean shouldShake = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_success);

        // -1 不摇奖
        try {
            shouldShake = !("-1".equals(this.getIntent().getStringExtra("SHAKE")));
        } catch (Exception e) {
            e.printStackTrace();

            shouldShake = false;
        }

        type = this.getIntent().getIntExtra("TYPE", TYPE_BALANCE);

        transferInfo = (TransferInfo) this.getIntent().getSerializableExtra("INFO");

        initView();

        refreshView();
    }

    private void initView() {
        ((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("支付信息");

        totalMoneyTextView = (TextView) this.findViewById(R.id.totalMoneyTextView);
        balanceMoneyTextView = (TextView) this.findViewById(R.id.balanceMoneyTextView);
        balanceTextView = (TextView) this.findViewById(R.id.balanceTextView);
        surplusMoneyTextView = (TextView) this.findViewById(R.id.surplusMoneyTextView);
        bankNameTextView = (TextView) this.findViewById(R.id.bankNameTextView);
        tailNumTextView = (TextView) this.findViewById(R.id.tailNumTextView);
        surplusMoneyLayout = (LinearLayout) this.findViewById(R.id.surplusMoneyLayout);
        bankNameLayout = (LinearLayout) this.findViewById(R.id.bankNameLayout);

        doneBtn = (Button) this.findViewById(R.id.doneBtn);
        doneBtn.setText("完    成");
        doneBtn.setOnClickListener(this);
    }

    private void refreshView() {
        if (type == TYPE_BALANCE) {
            surplusMoneyLayout.setVisibility(View.GONE);
            bankNameLayout.setVisibility(View.GONE);

        } else if (type == TYPE_BANKCARD) {
            surplusMoneyTextView.setText(transferInfo.getSurplusMoneyStr(transferInfo.isUseBalance()));
            bankNameTextView.setText(transferInfo.getBankName());
            tailNumTextView.setText(transferInfo.getTailNum());
        }

        totalMoneyTextView.setText(transferInfo.getTransferMoney());
        balanceMoneyTextView.setText(transferInfo.getNeedBalanceStr(transferInfo.isUseBalance()));
        balanceTextView.setText(transferInfo.getBalanceStr(transferInfo.isUseBalance()));
    }

    public void onBackPressed() {
        this.setResult(RESULT_OK);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.setResult(RESULT_OK);
                this.finish();
                break;

            case R.id.doneBtn:
                this.setResult(RESULT_OK);
                this.finish();
                break;
        }

    }

}
