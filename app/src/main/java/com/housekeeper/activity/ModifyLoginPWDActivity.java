package com.housekeeper.activity;

import java.util.HashMap;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.os.Bundle;
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
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ViewUtil;
import com.wufriends.housekeeper.keeper.R;

/**
 * 修改登录密码
 * 
 * @author sth
 * 
 */
public class ModifyLoginPWDActivity extends BaseActivity implements OnClickListener {

	private EditText oldPwdEditText;
	private EditText newPwdEditText;
	private EditText confirmNewPwdEditText;
	private Button nextBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_modify_login_pwd);

		initView();
	}

	private void initView() {
		TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleTextView.setText("修改登录密码");

		Button backButton = (Button) this.findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);

		oldPwdEditText = (EditText) this.findViewById(R.id.oldPwdEditText);

		newPwdEditText = (EditText) this.findViewById(R.id.newPwdEditText);

		confirmNewPwdEditText = (EditText) this.findViewById(R.id.confirmNewPwdEditText);

		nextBtn = (Button) this.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.backBtn:
			this.finish();
			break;

		case R.id.nextBtn:
			if (checkValue()) {
				requestUpdatePwd();
			}

			break;

		}

	}

	private void requestUpdatePwd() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("oldpassword", oldPwdEditText.getText().toString());
		tempMap.put("newpassword", newPwdEditText.getText().toString());

		JSONRequest request = new JSONRequest(this, RequestEnum.USER_UPDATE_LOGIN_PWD, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
					AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

					Toast.makeText(ModifyLoginPWDActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {
						oldPwdEditText.setText("");
						newPwdEditText.setText("");
						confirmNewPwdEditText.setText("");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在请求数据...");
	}

	private boolean checkValue() {
		String oldPwdStr = oldPwdEditText.getText().toString();
		String newPwdStr = newPwdEditText.getText().toString();
		String confirmPwdStr = confirmNewPwdEditText.getText().toString();

		if (TextUtils.isEmpty(oldPwdStr)) {
			Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(oldPwdEditText);
			return false;

		} else if (oldPwdStr.length() < 6 || oldPwdStr.length() > 20) {
			Toast.makeText(this, "密码为6－20数字和字母", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(oldPwdEditText);
			return false;

		} else if (TextUtils.isEmpty(newPwdStr)) {
			Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(newPwdEditText);
			return false;

		} else if (newPwdStr.length() < 6 || newPwdStr.length() > 20) {
			Toast.makeText(this, "密码为6－20数字和字母", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(newPwdEditText);
			return false;

		} else if (TextUtils.isEmpty(confirmPwdStr)) {
			Toast.makeText(this, "请再输入一次新密码", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;

		} else if (confirmPwdStr.length() < 6 || confirmPwdStr.length() > 20) {
			Toast.makeText(this, "密码为6－20数字和字母", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;

		} else if (!newPwdStr.equals(confirmPwdStr)) {
			Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
			ViewUtil.shakeView(confirmNewPwdEditText);
			return false;
		}

		return true;
	}

}
