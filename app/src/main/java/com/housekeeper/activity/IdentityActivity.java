package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ares.house.dto.app.TreeNodeAppDto;
import com.housekeeper.activity.view.IdentityPicker;
import com.wufriends.housekeeper.keeper.R;

public class IdentityActivity extends BaseActivity implements OnClickListener {

	private IdentityPicker identityPicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.showProgress("正在加载...");
		
		setContentView(R.layout.activity_identity);

		identityPicker = (IdentityPicker) findViewById(R.id.identityPicker);
		
		identityPicker.setData((TreeNodeAppDto) this.getIntent().getSerializableExtra("dto"));
		if (this.getIntent().getStringExtra("code") != null) {
			try{
				identityPicker.setSelect(Integer.parseInt(this.getIntent().getStringExtra("code")));
			} catch(Exception e){
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
			intent_c.putExtra("identityCode", identityPicker.getCity_code_string() + "");
			intent_c.putExtra("identityValue", identityPicker.getCity_string());

			this.setResult(RESULT_OK, intent_c);
			this.finish();
			break;
		default:
			break;
		}
	}

}
