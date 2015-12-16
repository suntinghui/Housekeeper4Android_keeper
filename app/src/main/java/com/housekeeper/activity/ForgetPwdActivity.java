package com.housekeeper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {

	private EditText telphoneEditText = null;
	private EditText codeEditText = null;
	private EditText newPwdEditText = null;
	private EditText confirmNewPwdEditText = null;
	private Button timeBtn = null;
	private Button nextBtn = null;

	private int currentTime = Constants.SMS_MAX_TIME;
	private Timer timer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_forget_pwd);

		initView();
	}

	private void initView() {
		((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
		((TextView) this.findViewById(R.id.titleTextView)).setText("找回密码");

		telphoneEditText = (EditText) this.findViewById(R.id.telphoneEditText);
		telphoneEditText.setText(ActivityUtil.getSharedPreferences().getString(Constants.UserName, ""));
		telphoneEditText.setSelection(telphoneEditText.getText().toString().trim().length());

		codeEditText = (EditText) this.findViewById(R.id.codeEditText);
		newPwdEditText = (EditText) this.findViewById(R.id.newPwdEditText);
		confirmNewPwdEditText = (EditText) this.findViewById(R.id.confirmNewPwdEditText);

		timeBtn = (Button) this.findViewById(R.id.timeBtn);
		timeBtn.setOnClickListener(this);

		nextBtn = (Button) this.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextBtn:
			if (checkValue()) {
				requestUpdatePwd();
			}
			break;

		case R.id.timeBtn:
			String telphone = telphoneEditText.getText().toString().trim();
			if (telphone.length() < 11) {
				Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
				ViewUtil.shakeView(telphoneEditText);

			} else {
				this.requestSendSMS();
			}

			break;

		case R.id.backBtn:
			this.setResult(RESULT_CANCELED);
			this.finish();
			break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (null != timer) {
			timer.cancel();
			timer = null;
		}
	}

	private boolean checkValue() {
		if (telphoneEditText.getText().toString().trim().length() < 11) {
			Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(telphoneEditText);
			return false;

		} else if (TextUtils.isEmpty(codeEditText.getText().toString().trim())) {
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(codeEditText);
			return false;

		} else if (codeEditText.getText().toString().trim().length() < 4) {
			Toast.makeText(this, "请输入4位验证码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(codeEditText);
			return false;

		} else if (TextUtils.isEmpty(newPwdEditText.getText().toString().trim())) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(newPwdEditText);
			return false;

		} else if (newPwdEditText.getText().toString().trim().length() < 6) {
			Toast.makeText(this, "密码为6－20位", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(newPwdEditText);
			return false;

		} else if (TextUtils.isEmpty(confirmNewPwdEditText.getText().toString().trim())) {
			Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;

		} else if (confirmNewPwdEditText.getText().toString().trim().length() < 6) {
			Toast.makeText(this, "密码为6－20位", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;

		} else if (!newPwdEditText.getText().toString().trim().equals(confirmNewPwdEditText.getText().toString().trim())) {
			Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;
		}

		return true;
	}

	private void requestUpdatePwd() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("telphone", telphoneEditText.getText().toString().trim());
		tempMap.put("vcode", codeEditText.getText().toString());
		tempMap.put("password", newPwdEditText.getText().toString());
		tempMap.put("userType", Constants.ROLE);

		JSONRequest request = new JSONRequest(this, RequestEnum.USER_NOLOGIN_PASSWORD_UPDATE, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImSubAccountsAppDto.class);
					AppMessageDto<ImSubAccountsAppDto> dto = objectMapper.readValue(jsonObject, type);

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {

						showMessage("密码修改成功，请牢记!");

					} else {
						Toast.makeText(ForgetPwdActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在发送请稍候...");
	}

	private void showMessage(String msg) {
		new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(null).setContentText("\n" + msg + "\n").setConfirmText("好的").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sDialog.cancel();

				ForgetPwdActivity.this.finish();
			}
		}).show();
	}

	private void requestSendSMS() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("telphone", telphoneEditText.getText().toString().trim());

		JSONRequest request = new JSONRequest(this, RequestEnum.USER_NOLOGIN_PASSWORD_UPDATE, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
					AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

					Toast.makeText(ForgetPwdActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

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

}
