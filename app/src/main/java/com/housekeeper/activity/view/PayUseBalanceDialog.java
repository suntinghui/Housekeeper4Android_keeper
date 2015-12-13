package com.housekeeper.activity.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.housekeeper.utils.ViewUtil;
import com.jungly.gridpasswordview.GridPasswordView;
import com.wufriends.housekeeper.keeper.R;

public class PayUseBalanceDialog extends Dialog {

	private GridPasswordView transferPwdView = null;
	private TextView titleTextView = null;
	private TextView totalMoneyTextView = null;
	private TextView balanceMoneyTextView = null;
	private TextView errorTextView = null;
	private Button closeBtn = null;
	private Button confirmBtn = null;

	private OnConfirmListener confirmListener = null;

	public PayUseBalanceDialog(Context context) {
		this(context, R.style.ProgressHUD);
	}

	public PayUseBalanceDialog(Context context, int theme) {
		super(context, theme);

		this.initView(context);
	}

	private void initView(Context context) {
		this.setContentView(R.layout.layout_pay_use_balance);

		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount = 0.5f;
		this.getWindow().setAttributes(lp);

		transferPwdView = (GridPasswordView) this.findViewById(R.id.transferPwdView);
		transferPwdView.setPasswordVisibility(false);
		transferPwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {

			@Override
			public void onMaxLength(String psw) {
			}

			@Override
			public void onChanged(String psw) {
				errorTextView.setText("");
			}
		});

		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		totalMoneyTextView = (TextView) this.findViewById(R.id.totalMoneyTextView);
		balanceMoneyTextView = (TextView) this.findViewById(R.id.balanceMoneyTextView);
		errorTextView = (TextView) this.findViewById(R.id.errorTextView);
		errorTextView.setText("");

		closeBtn = (Button) this.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkValue()) {
					if (confirmListener != null) {
						confirmListener.onConfirm(transferPwdView.getPassWord());
					}
				}
			}
		});

	}

	private boolean checkValue() {
		if (transferPwdView.getPassWord().length() != 6) {
			errorTextView.setText("请输入6位交易密码");
			ViewUtil.shakeView(transferPwdView);
			return false;
		}

		return true;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoneyTextView.setText(totalMoney);
	}

	public void setBalanceMoney(String balanceMoney) {
		this.balanceMoneyTextView.setText(balanceMoney);
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void setError(String error) {
		errorTextView.setText(error);
	}

	public String getPassword() {
		return transferPwdView.getPassWord();
	}

	public void setOnConfirmListener(OnConfirmListener listener) {
		this.confirmListener = listener;
	}

	public interface OnConfirmListener {
		public void onConfirm(String pwdStr);
	}

}
