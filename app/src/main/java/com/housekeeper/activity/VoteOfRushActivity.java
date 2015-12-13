package com.housekeeper.activity;

import org.codehaus.jackson.map.DeserializationConfig;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.DebtPackageInfoAppDto;
import com.housekeeper.activity.view.ClaimsView;
import com.housekeeper.activity.view.ItemDescOfRushView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

/**
 * 抢投项目详情
 * 
 * @author sth
 * 
 */
public class VoteOfRushActivity extends BaseActivity implements OnClickListener {

	private Button backBtn = null;
	private TextView itemDescriptionTextView = null; // 项目说明
	private TextView claimsTextView = null; // 债权包
	private LinearLayout contentLayout = null;

	private ItemDescOfRushView itemDescOfRushView = null; // 项目说明
	private ClaimsView claimsview = null; // 债权包

	private DebtPackageInfoAppDto infoDto = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_vote_rush);

		initView();

		requestDebtPackageInfo();
	}

	private void initView() {
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);

		itemDescriptionTextView = (TextView) this.findViewById(R.id.itemDescriptionTextView);
		itemDescriptionTextView.setSelected(true);
		itemDescriptionTextView.setOnClickListener(this);

		claimsTextView = (TextView) this.findViewById(R.id.claimsTextView);
		claimsTextView.setSelected(false);
		claimsTextView.setOnClickListener(this);

		contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);

		itemDescOfRushView = new ItemDescOfRushView(this);
		itemDescOfRushView.setVisibility(View.VISIBLE);

		claimsview = new ClaimsView(this);
		claimsview.setVisibility(View.GONE);

		contentLayout.addView(itemDescOfRushView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		contentLayout.addView(claimsview, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	private void requestDebtPackageInfo() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("telphone", ActivityUtil.getSharedPreferences().getString(Constants.UserName, ""));
		tempMap.put("id", this.getIntent().getStringExtra("id"));

		JSONRequest request = new JSONRequest(this, RequestEnum.DEBTPACKAGE_INFO, tempMap, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
					JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, DebtPackageInfoAppDto.class);
					AppMessageDto<DebtPackageInfoAppDto> dto = objectMapper.readValue(jsonObject, type);

					if (dto.getStatus() == AppResponseStatus.SUCCESS) {
						infoDto = dto.getData();

						refreshView();

					} else {
						Toast.makeText(VoteOfRushActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		this.addToRequestQueue(request, "正在请求数据...");
	}

	private void refreshView() {
		itemDescOfRushView.setValue(infoDto);
		claimsview.setValue(infoDto);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			this.finish();
			break;

		case R.id.itemDescriptionTextView:
			itemDescriptionTextView.setSelected(true);
			claimsTextView.setSelected(false);
			itemDescOfRushView.setVisibility(View.VISIBLE);
			claimsview.setVisibility(View.GONE);
			break;

		case R.id.claimsTextView:
			itemDescriptionTextView.setSelected(false);
			claimsTextView.setSelected(true);
			itemDescOfRushView.setVisibility(View.GONE);
			claimsview.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.setResult(RESULT_OK);
			this.finish();
		}
	}

}
