package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

public class VoteRushRankingLayout extends LinearLayout {

	private TextView stagingRankingTextView;
	private TextView stagingTimeTextView;
	private TextView stagingRateTextView;

	public VoteRushRankingLayout(Context context) {
		super(context);

		initView(context);
	}

	public VoteRushRankingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_rush_ranking, this);

		stagingRankingTextView = (TextView) this.findViewById(R.id.stagingRankingTextView);
		stagingTimeTextView = (TextView) this.findViewById(R.id.stagingTimeTextView);
		stagingRateTextView = (TextView) this.findViewById(R.id.stagingRateTextView);
	}

	public void setValue(int index, String day, String rate) {
		stagingRankingTextView.setText("第" + index + "名");
		stagingTimeTextView.setText(day + "天");
		stagingRateTextView.setText(rate + "%");
	}

}
