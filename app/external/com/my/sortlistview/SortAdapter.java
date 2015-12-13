package com.my.sortlistview;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;

		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (null == convertView) {
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_invite_contact, null);

			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
			holder.headTextView = (TextView) convertView.findViewById(R.id.headTextView);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
			holder.phoneNumTextView = (TextView) convertView.findViewById(R.id.phoneNumTextView);
			holder.statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);
			holder.rightArrowImageView = (ImageView) convertView.findViewById(R.id.rightArrowImageView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SortModel model = list.get(position);

		String name = model.getName();
		try {
			holder.headTextView.setText(name.substring(name.length() - 1));
		} catch (Exception e) {
			e.printStackTrace();
			holder.headTextView.setText("鼓");
		}

		holder.nameTextView.setText(name);
		holder.phoneNumTextView.setText(model.getCode());
		if (model.isInvite()) {
			holder.statusTextView.setText("已邀请");
			holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.greenme));
			holder.rightArrowImageView.setVisibility(View.GONE);
			holder.contentLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
		} else {
			holder.statusTextView.setText("短信邀请");
			holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.blueme));
			holder.rightArrowImageView.setVisibility(View.VISIBLE);
			holder.contentLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(model.getSortLetters());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}

		return convertView;

	}

	private class ViewHolder {
		private TextView tvLetter;

		private RelativeLayout contentLayout;
		private TextView headTextView;
		private TextView nameTextView;
		private TextView phoneNumTextView;
		private TextView statusTextView;
		private ImageView rightArrowImageView;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}