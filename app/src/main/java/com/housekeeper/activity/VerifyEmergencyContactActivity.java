package com.housekeeper.activity;

import org.codehaus.jackson.map.DeserializationConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.gesture.GestureLockSetupActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.Util;
import com.wufriends.housekeeper.keeper.R;

/**
 * 紧急联系人
 * 
 * @author sth
 * 
 */
public class VerifyEmergencyContactActivity extends BaseActivity implements OnClickListener {

	private List<String> shipList = Arrays.asList(new String[] { "父母", "配偶", "兄弟姐妹", "朋友", "好基友", "闺蜜", "其他" });

	private TextView nameTextView;
	private TextView telphoneTextView;
	private Spinner contactSpinner;
	private ImageView addContactImageView;
	private Spinner relationshipSpinner;
	private TextView relationshipTextView;
	private TextView topTipTextView;
	private Button confirmBtn;

	private int relationshipIndex = 0;

	private static final int REQUEST_CONTACT = 0x104;

	// 手机通讯录
	private String phoneBook = "";
	
	// 从注册后进入
	public static final int FROM_LOGIN = 0x001;
	// 从个人中心进入
	public static final int FROM_PERSIONCENTER = 0x002;
	private int from = FROM_PERSIONCENTER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_verify_emergency_contact);
		
		from = this.getIntent().getIntExtra("FROM", FROM_PERSIONCENTER);

		initView();

		requestContactValue();
	}

	private void initView() {
		TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleTextView.setText("紧急联系人");

		Button backButton = (Button) this.findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);
		
		TextView skipTextView = (TextView) this.findViewById(R.id.skipTextView);
		skipTextView.setOnClickListener(this);
		
		if (this.from == FROM_LOGIN) {
			backButton.setVisibility(View.GONE);
			skipTextView.setVisibility(View.VISIBLE);
		} else {
			backButton.setVisibility(View.VISIBLE);
			skipTextView.setVisibility(View.GONE);
		}

		confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
		confirmBtn.setOnClickListener(this);

		topTipTextView = (TextView) this.findViewById(R.id.topTipTextView);

		nameTextView = (TextView) this.findViewById(R.id.nameTextView);

		contactSpinner = (Spinner) this.findViewById(R.id.contactSpinner);
		telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);

		addContactImageView = (ImageView) this.findViewById(R.id.addContactImageView);
		addContactImageView.setOnClickListener(this);

		relationshipSpinner = (Spinner) this.findViewById(R.id.relationshipSpinner);

		relationshipTextView = (TextView) this.findViewById(R.id.relationshipTextView);
		relationshipTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			this.setResult(RESULT_CANCELED);
			this.finish();
			break;
			
		case R.id.skipTextView:
			Intent intent = new Intent(this, GestureLockSetupActivity.class);
			intent.putExtra("TYPE", GestureLockSetupActivity.TYPE_REGISTER);
			this.startActivity(intent);
			
			this.finish();
			break;

		case R.id.addContactImageView: {
			Intent intent_address = new Intent();
			intent_address.setAction(Intent.ACTION_PICK);
			intent_address.setData(ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent_address, REQUEST_CONTACT);
			break;
		}

		case R.id.relationshipTextView:
			chooseRelationship();
			break;

		case R.id.confirmBtn:
			if (checkValue()) {
				// 由于获取电话本的过程比较长，根据联系人多少的不同大约在2－5s之间，所以单独开一线程去处理。
				if (TextUtils.isEmpty(phoneBook)) {
					new processContactBookTask().execute();
				} else {
					requestSaveConstact();
				}
			}
			break;
		}

	}

	private boolean checkValue() {
		String name = nameTextView.getText().toString().trim();
		String telphone = telphoneTextView.getText().toString();
		String relationship = relationshipTextView.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			ActivityUtil.shakeView(nameTextView);
			return false;

		} else if (TextUtils.isEmpty(telphone)) {
			Toast.makeText(this, "请通过通讯录选择电话", Toast.LENGTH_SHORT).show();
			ActivityUtil.shakeView(addContactImageView);
			return false;

		} else if (TextUtils.isEmpty(relationship)) {
			Toast.makeText(this, "请选择关系", Toast.LENGTH_SHORT).show();
			ActivityUtil.shakeView(relationshipTextView);
			return false;

		}

		return true;
	}

	// 查询数据
	private void requestContactValue() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("type", "EMERGENCY_CONTACT");

		JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_ITEM_INFO, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
					AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {
						Map<String, String> map = dto.getData();
						
						String realName = map.get("EMERGENCY_CONTACT_REALNAME");
						if (realName != null && !realName.equals("")) {
							confirmBtn.setText("修改");
						}

						nameTextView.setText(map.get("EMERGENCY_CONTACT_REALNAME"));
						telphoneTextView.setText(map.get("EMERGENCY_CONTACT_TELPHONE"));
						relationshipTextView.setText(map.get("EMERGENCY_CONTACT_RELATIONSHIP"));

						// 关系
						relationshipIndex = shipList.indexOf(map.get("EMERGENCY_CONTACT_RELATIONSHIP"));

					} else {
						Toast.makeText(VerifyEmergencyContactActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在请求数据...");
	}

	// 添加联系人
	private void requestSaveConstact() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("realname", nameTextView.getText().toString());
		tempMap.put("relationship", relationshipTextView.getText().toString());
		tempMap.put("telphone", telphoneTextView.getText().toString());
		tempMap.put("phoneBook", phoneBook);

		JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_EMERGENCY_CONTACT_SAVE, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
					AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

					Toast.makeText(VerifyEmergencyContactActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {
						
						// 发送短信
						SmsManager smsManager = SmsManager.getDefault();
						List<String> divideContents = smsManager.divideMessage("我在使用‘我有房’，添加你为紧急联系人。");
						for (String text : divideContents) {
							 smsManager.sendTextMessage(telphoneTextView.getText().toString(), null, text, null, null);
						}

						if (from == FROM_LOGIN) {
							Intent intent = new Intent(VerifyEmergencyContactActivity.this, GestureLockSetupActivity.class);
							intent.putExtra("TYPE", GestureLockSetupActivity.TYPE_REGISTER);
							VerifyEmergencyContactActivity.this.startActivity(intent);
							
							VerifyEmergencyContactActivity.this.finish();
							
						} else {
							VerifyEmergencyContactActivity.this.finish();							
						}
						

					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在请求数据...");
	}

	private class processContactBookTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			VerifyEmergencyContactActivity.this.showProgress("正在处理数据...");
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				phoneBook = new ObjectMapper().writeValueAsString(Util.getContactList(VerifyEmergencyContactActivity.this));
			} catch (Exception e) {
				e.printStackTrace();
				phoneBook = "";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			VerifyEmergencyContactActivity.this.requestSaveConstact();
		}

	}

	// 选择手机号
	private void chooseTelphone(String name, final ArrayList<String> list) {
		if (list.size() == 0)
			return;

		nameTextView.setText(name);

		if (list.size() == 1) {
			telphoneTextView.setText(list.get(0));
			return;
		}

		final SpinnerAdapter adapter = new SpinnerAdapter(this, list);
		contactSpinner.setPrompt(name);
		contactSpinner.setAdapter(adapter);
		contactSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				telphoneTextView.setText(list.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		contactSpinner.performClick();
	}

	// 选择关系
	private void chooseRelationship() {
		relationshipSpinner.setPrompt("你们的关系是");
		final SpinnerAdapter adapter = new SpinnerAdapter(this, shipList);
		relationshipSpinner.setAdapter(adapter);
		adapter.setSelectedIndex(0);
		relationshipSpinner.setSelection(relationshipIndex);
		relationshipSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				relationshipTextView.setText(shipList.get(position));
				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();

				relationshipIndex = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		relationshipSpinner.performClick();
	}

	private class ViewHolder {
		private TextView textView;
		private ImageView imageView;
	}

	public class SpinnerAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private List<String> mList;
		private Context mContext;

		private int selectedIndex = 0;

		public SpinnerAdapter(Context pContext, List<String> pList) {
			this.mContext = pContext;
			this.mList = pList;

			this.mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (null == convertView) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.spinner_item, null);

				holder.textView = (TextView) convertView.findViewById(R.id.textView);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(mList.get(position));
			holder.imageView.setSelected(selectedIndex == position);
			holder.imageView.setPressed(selectedIndex == position);

			return convertView;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}
	}

	// 电话本返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
			if (data == null) {
				return;
			}

			String phoneNumber = null;
			String name = null;
			Uri contactData = data.getData();
			if (contactData == null) {
				return;
			}

			Cursor cursor = managedQuery(contactData, null, null, null, null);
			if (cursor.moveToFirst()) {
				String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				if (hasPhone.equalsIgnoreCase("1")) {
					hasPhone = "true";
				} else {
					hasPhone = "false";
				}

				if (Boolean.parseBoolean(hasPhone)) {
					ArrayList<String> phoneNumList = new ArrayList<String>();

					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
					while (phones.moveToNext()) {
						phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
						name = phones.getString(phones.getColumnIndex(PhoneLookup.DISPLAY_NAME)).replace(" ", "");

						phoneNumList.add(new String(phoneNumber));
					}

					phones.close();

					chooseTelphone(name, phoneNumList);
				}
			}

		}
	}

}
