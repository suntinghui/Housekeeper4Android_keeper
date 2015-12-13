package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ares.house.dto.app.DebtPackageInfoAppDto;
import com.wufriends.housekeeper.keeper.R;

/**
 * 债权包
 * @author sth
 *
 */
public class ClaimsView extends LinearLayout {
	
	private LinearLayout contentLayout;
	
	private ClaimsItemInfoView infoView;

	public ClaimsView(Context context) {
		super(context);

		this.initView(context);
	}

	public ClaimsView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_claims, this);
		
		contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
		
		infoView = new ClaimsItemInfoView(context);
		contentLayout.addView(infoView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
	public void setValue(DebtPackageInfoAppDto dto){
		if (dto == null)
			return;
		
		infoView.refreshView(dto);
	}

}
