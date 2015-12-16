package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImSubAccountsAppDto;
import com.housekeeper.activity.gesture.GestureLockSetupActivity;
import com.housekeeper.activity.gesture.GestureLockUtil;
import com.housekeeper.activity.keeper.KeeperMainActivity;
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

public class LoginActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener, TextWatcher {

	public int from = FROM_NONE;
	public static final int FROM_NONE = 0x110;
	public static final int FROM_FORGET_GESTURE = 0x120;
	public static final int FROM_TOKEN_EXPIRED = 0x911;

	private TextView changeUserTextView = null;
	private TextView phoneNumTextView = null;
	private LinearLayout phoneLayout = null;
	private EditText pwdEditText = null;
	private TextView forgetPwdTextView = null;
	private Button clearBtn = null;
	private Button loginBtn = null;

	private String telphone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		from = this.getIntent().getIntExtra("FROM", FROM_NONE);

		telphone = ActivityUtil.getSharedPreferences().getString(Constants.UserName, "");

		initView();
	}

	private void initView() {
		((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
		((TextView) this.findViewById(R.id.titleTextView)).setText("房管家登录");

		changeUserTextView = (TextView) this.findViewById(R.id.changeUserTextView);
		changeUserTextView.setOnClickListener(this);

		phoneNumTextView = (TextView) this.findViewById(R.id.phoneNumTextView);
		phoneNumTextView.setText(telphone);

		forgetPwdTextView = (TextView) this.findViewById(R.id.forgetPwdTextView);
		forgetPwdTextView.setOnClickListener(this);

		phoneLayout = (LinearLayout) this.findViewById(R.id.phoneLayout);

		pwdEditText = (EditText) this.findViewById(R.id.pwdEditText);
		pwdEditText.setOnFocusChangeListener(this);
		pwdEditText.addTextChangedListener(this);

		clearBtn = (Button) this.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(this);

		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearBtn:
			pwdEditText.setText("");
			break;

		case R.id.changeUserTextView: {
			changeUser();
			break;
		}

		case R.id.forgetPwdTextView: {
			Intent intent = new Intent(this, ForgetPwdActivity.class);
			this.startActivityForResult(intent, 0);
			break;
		}

		case R.id.loginBtn:
			if (checkValue()) {
				requestLogin();
			}
			break;

		case R.id.backBtn:
			this.backAction();
			break;
		}
	}

	public void onBackPressed() {
		backAction();
	}

	private void changeUser() {
		/*
		boolean flag = false;
		for (Activity act : ActivityManager.getInstance().getAllActivity()) {
			if (act instanceof RegisterAndLoginActivity) {
				flag = true;
				break;
			}
		}

		if (flag) {
			this.setResult(RESULT_CANCELED);
			this.finish();

		} else {
			Intent intent = new Intent(this, RegisterAndLoginActivity.class);
			this.startActivity(intent);
			this.finish();
		}

		*/

		Intent intent = new Intent(this, RegisterAndLoginActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	private void backAction() {
		if (from == FROM_FORGET_GESTURE) {
			this.finish();
			
		} else if (from == FROM_TOKEN_EXPIRED) {
			/*
			Log.e("===", "------------------------------------&&&&&&&&");
			
			Intent mainIntent = new Intent(this, SelectIdentityActivity.class);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(mainIntent);
			
			// 切到首页
			Intent intent1 = new Intent(MainActivity.ACTION_CHECK_RELATION);
			intent1.putExtra("INDEX", 0);
			sendBroadcast(intent1);

			this.finish();
			*/

			Intent intent = new Intent(this, KeeperMainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			
		} else {
			changeUser();
		}
	}

	private boolean checkValue() {
		String pwd = pwdEditText.getText().toString();
		if (TextUtils.isEmpty(pwd.trim())) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(pwdEditText);
			return false;

		} else if (pwd.trim().length() < 6) {
			Toast.makeText(this, "密码应为6－20位数字和字母", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(pwdEditText);
			return false;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.finish();
		}
	}

	private void requestLogin() {
		final String pwd = pwdEditText.getText().toString();

		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("username", telphone);
		tempMap.put("userpass", pwd);
		tempMap.put("userType", Constants.ROLE);
		tempMap.put("deviceToken", ActivityUtil.getSharedPreferences().getString(Constants.DEVICETOKEN, ""));

		JSONRequest request = new JSONRequest(this, RequestEnum.USER_LOGIN, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImSubAccountsAppDto.class);
					AppMessageDto<ImSubAccountsAppDto> dto = objectMapper.readValue(jsonObject, type);

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {
						new UMengPushUtil().new AddAliasTask(LoginActivity.this, telphone).execute();

						ActivityUtil.getSharedPreferences().edit().putString(Constants.HEAD_RANDOM, System.currentTimeMillis() + "").commit();

						ActivityUtil.getSharedPreferences().edit().putString(Constants.UserName, telphone).commit();

						ActivityUtil.getSharedPreferences().edit().putString(Constants.USERID, dto.getData().getUserId()).commit();

						if (from == FROM_FORGET_GESTURE) {
							GestureLockUtil.clearGestureLock();

							Intent intent = new Intent(LoginActivity.this, GestureLockSetupActivity.class);
							LoginActivity.this.startActivity(intent);
							LoginActivity.this.setResult(RESULT_OK);
							LoginActivity.this.finish();

						} else if (from == FROM_TOKEN_EXPIRED) {
							LoginActivity.this.setResult(RESULT_OK);
							LoginActivity.this.finish();

						} else if (from == FROM_NONE) {
//							Intent intent = new Intent(LoginActivity.this, SelectIdentityActivity.class);
//							LoginActivity.this.startActivity(intent);

							LoginActivity.this.setResult(RESULT_OK);
							LoginActivity.this.finish();
						}

					} else {
						Toast.makeText(LoginActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在登录...");
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.pwdEditText) {
			phoneLayout.setSelected(hasFocus);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		String str = s.toString();
		if (str.length() > 0) {
			clearBtn.setVisibility(View.VISIBLE);
		} else {
			clearBtn.setVisibility(View.INVISIBLE);
		}

	}

}
