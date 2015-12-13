package com.housekeeper.activity.view;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ares.house.dto.app.DebtPackageInfoAppDto;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.housekeeper.activity.StagingUserActivity;
import com.housekeeper.activity.VoteOfRushActivity;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

/**
 * 抢投项目详情
 * 
 * @author sth
 * 
 */
public class ItemDescOfRushView extends LinearLayout implements OnClickListener {

	private LinearLayout fenqiLayout;
	private CustomNetworkImageView fenqiHeadImageView;
	private TextView nameTextView;
	private TextView companyTextView;
	private TextView fenqiDescTextView;
	private TextView totalAmountTextView;
	private ProgressBar progressBar;
	private TextView expectedRateTextView; // 预期年化利率
	private TextView periodTextView; // 理财周期

	private int progressInit = 0;
	private Runnable progressRunnable = null;

	private TextView hasSoldTextView = null; // 已售百分比
	private TextView availableSellAmountTextView = null; // 可售金额

	private LinearLayout itemDescLayout; // 产品详情
	private TextView repaymentTypeTextView; // 还款方式

	private LinearLayout investmentRecordsLayout; // 投资记录
	private TextView recordsCountTextView; // 投资人数

	private Button investmentBtn; // 立即投资
	private TextView rewardRateTextView; // 加息奖励

	private LinearLayout rankingLayout; // 排名

	private TextView transferDayTextView; // 多少天后可以转让

	private VoteOfRushActivity context;

	private DebtPackageInfoAppDto dto;

	public ItemDescOfRushView(VoteOfRushActivity context) {
		super(context);

		initView(context);
	}

	public ItemDescOfRushView(VoteOfRushActivity context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
	}

	private void initView(VoteOfRushActivity context) {
		this.context = context;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_rush_item_desp, this);

		fenqiLayout = (LinearLayout) this.findViewById(R.id.fenqiLayout);
		fenqiLayout.setOnClickListener(this);

		fenqiHeadImageView = (CustomNetworkImageView) this.findViewById(R.id.fenqiHeadImageView);
		nameTextView = (TextView) this.findViewById(R.id.nameTextView);
		companyTextView = (TextView) this.findViewById(R.id.companyTextView);
		fenqiDescTextView = (TextView) this.findViewById(R.id.fenqiDescTextView);

		totalAmountTextView = (TextView) this.findViewById(R.id.totalAmountTextView);

		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		progressBar.setIndeterminate(false);
		progressBar.incrementProgressBy(1);
		progressBar.setMax(100);
		progressBar.setProgress(0);

		hasSoldTextView = (TextView) this.findViewById(R.id.hasSoldTextView);
		availableSellAmountTextView = (TextView) this.findViewById(R.id.availableSellAmountTextView);

		expectedRateTextView = (TextView) this.findViewById(R.id.expectedRateTextView);
		periodTextView = (TextView) this.findViewById(R.id.periodTextView);

		itemDescLayout = (LinearLayout) this.findViewById(R.id.itemDescLayout);
		itemDescLayout.setOnClickListener(this);

		repaymentTypeTextView = (TextView) this.findViewById(R.id.repaymentTypeTextView);

		investmentRecordsLayout = (LinearLayout) this.findViewById(R.id.investmentRecordsLayout);
		investmentRecordsLayout.setOnClickListener(this);

		recordsCountTextView = (TextView) this.findViewById(R.id.recordsCountTextView);

		investmentBtn = (Button) this.findViewById(R.id.investmentBtn);
		investmentBtn.setOnClickListener(this);
		
		rewardRateTextView = (TextView) this.findViewById(R.id.rewardRateTextView);

		transferDayTextView = (TextView) this.findViewById(R.id.transferDayTextView);

		rankingLayout = (LinearLayout) this.findViewById(R.id.rankingLayout);
	}

	public void setValue(DebtPackageInfoAppDto dto) {
		if (dto == null)
			return;

		this.dto = dto;

		// 是否可以购买
		investmentBtn.setEnabled(dto.isCanBuy());

		fenqiHeadImageView.setDefaultImageResId(R.drawable.fenqi_head_default);
		fenqiHeadImageView.setErrorImageResId(R.drawable.fenqi_head_default);
		fenqiHeadImageView.setLocalImageBitmap(R.drawable.fenqi_head_default);
		fenqiHeadImageView.setImageUrl(dto.getLogoImg(), ImageCacheManager.getInstance().getImageLoader());

		nameTextView.setText(dto.getRealName());

		companyTextView.setText("(" + dto.getOrganization() + ")");

		fenqiDescTextView.setText("分期消费：" + dto.getGoodsName() + " ￥" + dto.getGoodsPrice());

		// 设置进度值
		int progress = this.calcProgress();
		setProgressbar(progress);

		totalAmountTextView.setText(dto.getTotalMoney());
		hasSoldTextView.setText(progress + "%");
		availableSellAmountTextView.setText(dto.getSurplusMoney() + "元");
		expectedRateTextView.setText(dto.getMaxRate() + "%");
		periodTextView.setText(dto.getMinPeriod() + "");
		repaymentTypeTextView.setText("按天付息，到期还本");
		recordsCountTextView.setText(dto.getPeople() + "");
		transferDayTextView.setText(dto.getTransfer() + "");

		int index = 1;
		VoteRushRankingLayout rankLayout = null;
		for (Map<String, String> map : dto.getRates()) {
			rankLayout = new VoteRushRankingLayout(context);
			rankLayout.setValue(index++, map.get("day"), map.get("rate"));
			rankingLayout.addView(rankLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}

		// 判断是否已经售完
		checkSoldOut();
		
		if (null != dto.getRewardRate()) {
			rewardRateTextView.setText("加息" + dto.getRewardRate() + "%");
			handler.postDelayed(runnable,1000); // 开始Timer
		}
	}
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			YoYo.with(Techniques.Bounce).duration(800).playOn(rewardRateTextView);
			
			handler.postDelayed(this, 1000); // postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中
		}
	};

	// 判断是否已经售完。如果已经售完，盖章
	private void checkSoldOut() {
		if (this.dto.getTotalMoney().equals(this.dto.getSoldMoney())) {
			ImageView sealImageView = (ImageView) context.findViewById(R.id.sealImageView);
			sealImageView.setVisibility(View.VISIBLE);

			// 这种方式效果很好，但是有一个致命的BUG，它会重绘整个界面的排版结构。遗憾！
			// new PuffInAnimation(sealImageView).animate();

			YoYo.with(Techniques.Landing).duration(800).playOn(sealImageView);

		}
	}

	private int calcProgress() {
		double total = Double.parseDouble(dto.getTotalMoney());
		double sold = Double.parseDouble(dto.getSoldMoney());
		int progress = (int) (100 * sold / total);
		return progress;
	}

	private void setProgressbar(final int progress) {
		final Handler handler = new Handler();
		progressRunnable = new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(++progressInit);

				if (progressInit >= progress) {
					handler.removeCallbacks(progressRunnable);
					progressInit = 0;

				} else {
					handler.postDelayed(this, 2);
				}
			}
		};
		handler.postDelayed(progressRunnable, 400);
	}

	@Override
	public void onClick(View v) {
		if (dto == null)
			return;

		switch (v.getId()) {
		case R.id.fenqiLayout: {// 分期用户信息
			Intent intent = new Intent(this.context, StagingUserActivity.class);
			intent.putExtra("DTO", dto);
			this.context.startActivity(intent);
		}
			break;

		/*
		case R.id.itemDescLayout: {// 产品详情
			Intent intent = new Intent(this.context, ItemDetailActivity.class);
			intent.putExtra("DTO", dto);
			this.context.startActivity(intent);
		}
			break;

		case R.id.investmentRecordsLayout: {// 投资记录
			Intent intent = new Intent(this.context, InvestmentRecordsActivity.class);
			intent.putExtra("id", dto.getId());
			this.context.startActivity(intent);

		}
			break;

		case R.id.investmentBtn: {
			Intent intent = new Intent(this.context, RushInvestmentActivity.class);
			intent.putExtra("DTO", dto);
			this.context.startActivityForResult(intent, 0);
		}
			break;

			*/
		}

	}

}
