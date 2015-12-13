package com.housekeeper.activity.view;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.TransferClient;
import com.housekeeper.client.TransferInfo;
import com.wufriends.housekeeper.keeper.R;

public class ConfirmPayDialog extends Dialog implements OnCheckedChangeListener {

    private BaseActivity context;

    private LinearLayout useBalanceLayout = null;
    private TextView totalMoneyTextView = null;
    private TextView balanceMoneyTextView = null;
    private CheckBox useBalanceCheckBox = null;
    private TextView surplusMoneyTextView = null;
    private Button cancelBtn = null;
    private Button confirmBtn = null;

    private OnConfirmListener confirmListener = null;

    private TransferInfo transferInfo = null;

    public ConfirmPayDialog(BaseActivity context) {
        this(context, R.style.ProgressHUD);
    }

    public ConfirmPayDialog(BaseActivity context, int theme) {
        super(context, theme);

        this.initView(context);
    }

    private void initView(BaseActivity ctx) {
        this.context = ctx;

        this.setContentView(R.layout.layout_confirm_pay);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        this.getWindow().setAttributes(lp);

        totalMoneyTextView = (TextView) this.findViewById(R.id.totalMoneyTextView);
        balanceMoneyTextView = (TextView) this.findViewById(R.id.balanceMoneyTextView);
        surplusMoneyTextView = (TextView) this.findViewById(R.id.surplusMoneyTextView);

        useBalanceLayout = (LinearLayout) this.findViewById(R.id.useBalanceLayout);
        useBalanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useBalanceCheckBox.performClick();
            }
        });

        useBalanceCheckBox = (CheckBox) this.findViewById(R.id.useBalanceCheckBox);
        useBalanceCheckBox.setChecked(true);
        useBalanceCheckBox.setOnCheckedChangeListener(this);

        cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferClient.getInstance().transfer(context, transferInfo);

                dismiss();

                if (confirmListener != null) {
                }
            }
        });

    }

    public void setTransferInfo(TransferInfo info) {
        this.transferInfo = info;

        totalMoneyTextView.setText(info.getTransferMoney());
        balanceMoneyTextView.setText(info.getBalanceMoney());
        calcSurplusMoney(useBalanceCheckBox.isChecked());
        transferInfo.setUseBalance(useBalanceCheckBox.isChecked());
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
    }

    public interface OnConfirmListener {
        public void onConfirm(String pwdStr);
    }

    private void calcSurplusMoney(boolean useBalance) {
        surplusMoneyTextView.setText(transferInfo.getSurplusMoneyStr(useBalance));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        calcSurplusMoney(isChecked);
        transferInfo.setUseBalance(isChecked);
    }

}
