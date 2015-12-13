package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.DebtPackageInfoAppDto;
import com.wufriends.housekeeper.keeper.R;

/**
 * 债权信息
 * 
 * @author sth
 * 
 */
public class ClaimsItemInfoView extends LinearLayout {

	private TextView nameTextView;
	private TextView companyTextView;
	private TextView amountTextView;
	private TextView goodsTextView;
	private TextView mountCountTextView;
	private TextView timeTextView;

	public ClaimsItemInfoView(Context context) {
		super(context);

		this.initView(context);
	}

	public ClaimsItemInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_claims_item_info, this);

		nameTextView = (TextView) this.findViewById(R.id.nameTextView);
		companyTextView = (TextView) this.findViewById(R.id.companyTextView);
		amountTextView = (TextView) this.findViewById(R.id.amountTextView);
		goodsTextView = (TextView) this.findViewById(R.id.goodsTextView);
		mountCountTextView = (TextView) this.findViewById(R.id.mountCountTextView);
		timeTextView = (TextView) this.findViewById(R.id.timeTextView);
	}

	public void refreshView(DebtPackageInfoAppDto dto) {
		nameTextView.setText(dto.getRealName() + " （实名认证）");
		companyTextView.setText(dto.getOrganization());
		amountTextView.setText(dto.getGoodsPrice());
		mountCountTextView.setText("分" + dto.getMonth() + "期每月等额还本付息");
		timeTextView.setText(dto.getBuyTime());
	}

}
