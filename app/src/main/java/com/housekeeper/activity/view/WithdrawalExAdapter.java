package com.housekeeper.activity.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.WithdrawalMoneyAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.utils.BankUtil;
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

public class WithdrawalExAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

	private BaseActivity context;
	private List<WithdrawalMoneyAppDto> withdrawalList = new ArrayList<WithdrawalMoneyAppDto>();

	public WithdrawalExAdapter(BaseActivity context) {
		this.context = context;
	}

	public void setData(List<WithdrawalMoneyAppDto> tempList) {
		if (tempList == null)
			return;

		this.withdrawalList = tempList;
	}

	private static class GroupHolder {
		private LinearLayout topLayout;
		private ImageView indicatorImageView;
		private TextView withdrawalMoneyTextView; // 提现 0.00元
		private TextView withdrawalTimeTextView; // 提现申请时间
		private TextView withdrawalStateTextView; // 提现状态
		private View bottomLineView;

	}

	private static class ChildHolder {
		private LinearLayout detailLayout;
		private TextView bankNameTextView;
		private TextView tailNumTextView;
		private TextView transferNumTextView;
		private TextView remarkTextView;

		private ImageView applyImageView;
		private ImageView progressImageView_1;
		private ImageView bankProcessImageView;
		private ImageView progressImageView_2;
		private ImageView successImageView;

		private TextView applyStateTextView;
		private TextView applyTimeTextView;
		private TextView bankProcessStateTextView;
		private TextView bankProcessTimeTextView;
		private TextView successStateTextView;
		private TextView successTimeTextView;
		private View bottomLineView;
	}

	@Override
	public int getGroupCount() {
		return this.withdrawalList.size();
	}

	@Override
	public WithdrawalMoneyAppDto getGroup(int groupPosition) {
		return this.withdrawalList.get(groupPosition);
	}

	@Override
	public WithdrawalMoneyAppDto getChild(int groupPosition, int childPosition) {
		return this.withdrawalList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder holder;
		if (convertView == null) {
			holder = new GroupHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_withdrawal_group, parent, false);

			holder.topLayout = (LinearLayout) convertView.findViewById(R.id.topLayout);
			holder.indicatorImageView = (ImageView) convertView.findViewById(R.id.indicatorImageView);
			holder.withdrawalMoneyTextView = (TextView) convertView.findViewById(R.id.withdrawalMoneyTextView);
			holder.withdrawalTimeTextView = (TextView) convertView.findViewById(R.id.withdrawalTimeTextView);
			holder.withdrawalStateTextView = (TextView) convertView.findViewById(R.id.withdrawalStateTextView);
			holder.bottomLineView = convertView.findViewById(R.id.bottomLineView);

			convertView.setTag(holder);

		} else {
			holder = (GroupHolder) convertView.getTag();
		}

		WithdrawalMoneyAppDto dto = getGroup(groupPosition);
		holder.withdrawalMoneyTextView.setText("提现 ￥" + dto.getMoney() + " 到银行卡");
		holder.withdrawalTimeTextView.setText(dto.getBeginTime());
		// 提现状态 b提现失败 c提现确认中 d提现成功
		switch (dto.getStatus()) {
			case 'a':
			holder.withdrawalStateTextView.setText("审核中");
			holder.withdrawalStateTextView.setTextColor(context.getResources().getColor(R.color.orange));
			break;

			case 'b':
			holder.withdrawalStateTextView.setText("提现失败");
			holder.withdrawalStateTextView.setTextColor(context.getResources().getColor(R.color.redme));
			break;

		case 'c':
			holder.withdrawalStateTextView.setText("银行处理中");
			holder.withdrawalStateTextView.setTextColor(context.getResources().getColor(R.color.orange));
			break;

		case 'd':
			holder.withdrawalStateTextView.setText("提现成功");
			holder.withdrawalStateTextView.setTextColor(context.getResources().getColor(R.color.blueme));
			break;
		}

		if (isExpanded) {
			holder.indicatorImageView.setBackgroundResource(R.drawable.withdrawal_indicator_down);
			holder.bottomLineView.setVisibility(View.GONE);
		} else {
			holder.indicatorImageView.setBackgroundResource(R.drawable.withdrawal_indicator_right);
			holder.bottomLineView.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder;
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_withdrawal_child, parent, false);

			holder.detailLayout = (LinearLayout) convertView.findViewById(R.id.detailLayout);

			holder.bankNameTextView = (TextView) convertView.findViewById(R.id.bankNameTextView);
			holder.tailNumTextView = (TextView) convertView.findViewById(R.id.tailNumTextView);
			holder.transferNumTextView = (TextView) convertView.findViewById(R.id.transferNumTextView);
			holder.remarkTextView = (TextView) convertView.findViewById(R.id.remarkTextView);
			holder.remarkTextView.setVisibility(View.GONE);

			holder.applyImageView = (ImageView) convertView.findViewById(R.id.applyImageView);
			holder.progressImageView_1 = (ImageView) convertView.findViewById(R.id.progressImageView_1);
			holder.bankProcessImageView = (ImageView) convertView.findViewById(R.id.bankProcessImageView);
			holder.progressImageView_2 = (ImageView) convertView.findViewById(R.id.progressImageView_2);
			holder.successImageView = (ImageView) convertView.findViewById(R.id.successImageView);

			holder.applyStateTextView = (TextView) convertView.findViewById(R.id.applyStateTextView);
			holder.applyTimeTextView = (TextView) convertView.findViewById(R.id.applyTimeTextView);
			holder.bankProcessStateTextView = (TextView) convertView.findViewById(R.id.bankProcessStateTextView);
			holder.bankProcessTimeTextView = (TextView) convertView.findViewById(R.id.bankProcessTimeTextView);
			holder.successStateTextView = (TextView) convertView.findViewById(R.id.successStateTextView);
			holder.successTimeTextView = (TextView) convertView.findViewById(R.id.successTimeTextView);

			holder.bottomLineView = convertView.findViewById(R.id.bottomLineView);

			convertView.setTag(holder);

		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		WithdrawalMoneyAppDto dto = getGroup(groupPosition);

		holder.bankNameTextView.setText(BankUtil.getBankFromCode(dto.getBankId(), this.context).getName());
		holder.transferNumTextView.setText("提现流水号：" + dto.getOrderNum());
		holder.tailNumTextView.setText(dto.getTailNumber());
		if (dto.getRemark() == null || TextUtils.isEmpty(dto.getRemark()) || TextUtils.equals("null", dto.getRemark())) {
			holder.remarkTextView.setVisibility(View.GONE);
		} else {
			holder.remarkTextView.setVisibility(View.VISIBLE);
			holder.remarkTextView.setText(dto.getRemark());
		}

		// 从业务逻辑上，一开始的状态就到银行处理中
		holder.applyImageView.setBackgroundResource(R.drawable.withdrawal_apply);
		holder.progressImageView_1.setBackgroundResource(R.drawable.withdrawal_progess_done);
		holder.bankProcessImageView.setBackgroundResource(R.drawable.withdrawal_bankprocess);

		holder.applyStateTextView.setText("转出申请成功");
		holder.applyTimeTextView.setText(dto.getBeginTime());
		holder.bankProcessStateTextView.setText("银行处理中");
		holder.bankProcessTimeTextView.setText(dto.getSendTime());
		holder.successStateTextView.setText("转出到卡");
		holder.successTimeTextView.setText(dto.getEndTime());

		// 提现状态 b提现失败 c提现确认中 d提现成功
		switch (dto.getStatus()) {
		case 'a':
			holder.progressImageView_2.setBackgroundResource(R.drawable.withdrawal_progess_watting);
			holder.successImageView.setBackgroundResource(R.drawable.withdrawal_waitting);
			holder.bankProcessImageView.setBackgroundResource(R.drawable.withdrawal_waitting);
			holder.bankProcessStateTextView.setTextColor(context.getResources().getColor(R.color.gray_10));
			holder.successStateTextView.setTextColor(context.getResources().getColor(R.color.gray_10));
			break;
		case 'b':
			holder.progressImageView_2.setBackgroundResource(R.drawable.withdrawal_progess_done);
			holder.successImageView.setBackgroundResource(R.drawable.withdrawal_failure);
			holder.bankProcessStateTextView.setTextColor(context.getResources().getColor(R.color.gray_10));
			holder.successStateTextView.setTextColor(context.getResources().getColor(R.color.redme));
			break;

		case 'c':
			holder.progressImageView_2.setBackgroundResource(R.drawable.withdrawal_progess_watting);
			holder.successImageView.setBackgroundResource(R.drawable.withdrawal_waitting);
			holder.bankProcessStateTextView.setTextColor(context.getResources().getColor(R.color.orange));
			holder.successStateTextView.setTextColor(context.getResources().getColor(R.color.gray_10));
			break;

		case 'd':
			holder.progressImageView_2.setBackgroundResource(R.drawable.withdrawal_progess_done);
			holder.successImageView.setBackgroundResource(R.drawable.withdrawal_success);
			holder.bankProcessStateTextView.setTextColor(context.getResources().getColor(R.color.gray_10));
			holder.successStateTextView.setTextColor(context.getResources().getColor(R.color.blueme));
			break;
		}

		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return 1;
	}

}
