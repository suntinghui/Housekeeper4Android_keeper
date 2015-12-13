package com.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.housekeeper.utils.BankUtil;
import com.wufriends.housekeeper.keeper.R;

import java.util.HashMap;

public class BindingSuccessActivity extends BaseActivity implements OnClickListener {

	private TextView nameTextView;
	private TextView idCardTextView;
	private TextView bankNameTextView;
	private TextView bankNumTextView;
	private Button doneBtn;

	private HashMap<String, String> map = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_binding_success);

		map = (HashMap<String, String>) this.getIntent().getSerializableExtra("MAP");

		initView();
	}

	private void initView() {
		((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
		((TextView) this.findViewById(R.id.titleTextView)).setText("绑定成功");

		nameTextView = (TextView) this.findViewById(R.id.nameTextView);
		idCardTextView = (TextView) this.findViewById(R.id.idCardTextView);
		bankNameTextView = (TextView) this.findViewById(R.id.bankNameTextView);
		bankNumTextView = (TextView) this.findViewById(R.id.bankNumTextView);

		nameTextView.setText(map.get("realName"));
		idCardTextView.setText(map.get("idCard"));
		bankNameTextView.setText(BankUtil.getBankFromCode(map.get("bankId"), this).getName());
		bankNumTextView.setText(map.get("bankCardNum"));

		doneBtn = (Button) this.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(this);
	}

	public void onBackPressed() {
		this.setResult(RESULT_OK);
		this.finish();
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
