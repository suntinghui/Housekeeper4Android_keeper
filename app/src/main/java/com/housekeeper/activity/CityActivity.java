package com.housekeeper.activity;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.housekeeper.activity.view.CityPicker;
import com.wufriends.housekeeper.keeper.R;

public class CityActivity extends BaseActivity implements OnClickListener {

	private CityPicker cityPicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_city);
		
		cityPicker = (CityPicker) findViewById(R.id.citypicker);

		if (!StringUtils.isBlank(this.getIntent().getStringExtra("code"))) {
			try {
				cityPicker.setSelect(this.getIntent().getStringExtra("code"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		View view_bg = findViewById(R.id.view_bg);
		view_bg.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
	}
	
	public void onResume(){
		super.onResume();
		
		this.hideProgress();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_bg:
			this.finish();
			break;

		case R.id.btn_confirm:
			Intent intent_c = new Intent();
			intent_c.putExtra("cityCode", cityPicker.getCity_code_string());
			intent_c.putExtra("cityValue", cityPicker.getCity_string());
			this.setResult(RESULT_OK, intent_c);
			this.finish();
			break;
		default:
			break;
		}

	}
}
