package com.housekeeper.activity.view;

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
import com.housekeeper.activity.StagingUserActivityEx;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

/**
 * 定投项目详情
 *
 * @author sth
 */
public class ItemDescOfScheduledView extends LinearLayout implements OnClickListener {

    private LinearLayout fenqiLayout;
    private CustomNetworkImageView fenqiHeadImageView;
    private TextView sourceTypeTextView;
    private TextView sourceInfoTextView;
    private TextView companyTextView;
    private TextView sourceTextView;
    private TextView numTextView;
    private TextView totalAmountTextView;
    private ProgressBar progressBar;
    private TextView expectedRateTextView; // 预期年化利率
    private TextView periodTextView; // 理财周期

    private int progressInit = 0;
    private Runnable progressRunnable = null;

    private TextView hasSoldTextView = null; // 已售百分比
    private TextView availableSellAmountTextView = null; // 可售金额

    private TextView repaymentTypeTextView; // 还款方式

    private LinearLayout investmentRecordsLayout; // 投资记录
    private TextView recordsCountTextView; // 投资人数

    private Button investmentBtn; // 立即投资
    private TextView rewardRateTextView; // 加息奖励

    private Activity context;

    private DebtPackageInfoAppDto dto;

    public ItemDescOfScheduledView(Activity context) {
        super(context);

        initView(context);
    }

    public ItemDescOfScheduledView(Activity context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    private void initView(Activity context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_scheduled_item_desp, this);

        fenqiLayout = (LinearLayout) this.findViewById(R.id.fenqiLayout);
        fenqiLayout.setOnClickListener(this);

        fenqiHeadImageView = (CustomNetworkImageView) this.findViewById(R.id.fenqiHeadImageView);
        sourceTypeTextView = (TextView) this.findViewById(R.id.sourceTypeTextView);
        sourceInfoTextView = (TextView) this.findViewById(R.id.sourceInfoTextView);
        companyTextView = (TextView) this.findViewById(R.id.companyTextView);

        sourceTextView = (TextView) this.findViewById(R.id.sourceTextView);
        numTextView = (TextView) this.findViewById(R.id.numTextView);
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

        repaymentTypeTextView = (TextView) this.findViewById(R.id.repaymentTypeTextView);

        investmentRecordsLayout = (LinearLayout) this.findViewById(R.id.investmentRecordsLayout);
        investmentRecordsLayout.setOnClickListener(this);

        recordsCountTextView = (TextView) this.findViewById(R.id.recordsCountTextView);

        investmentBtn = (Button) this.findViewById(R.id.investmentBtn);
        investmentBtn.setOnClickListener(this);

        rewardRateTextView = (TextView) this.findViewById(R.id.rewardRateTextView);
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

        sourceTypeTextView.setText(dto.getSourceTypeStr() + "：");
        sourceInfoTextView.setText(dto.getSourceTypeInfo());

        companyTextView.setText(dto.getOrganization());

        sourceTextView.setText("【" + dto.getSourceTypeStr() + "】");
        numTextView.setText(dto.getNum());

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

        // 判断是否已经售完
        checkSoldOut();

        if (null != dto.getRewardRate()) {
            rewardRateTextView.setText("加息" + dto.getRewardRate() + "%");
            handler.postDelayed(runnable, 1000); // 开始Timer
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
                if (this.dto.isNew()) {
                    Intent intent = new Intent(this.context, StagingUserActivityEx.class);
                    intent.putExtra("sourceNum", dto.getSourceNum());
                    this.context.startActivity(intent);

                } else {
                    Intent intent = new Intent(this.context, StagingUserActivity.class);
                    intent.putExtra("image", dto.getLogoImg());
                    intent.putExtra("realname", dto.getRealName());
                    intent.putExtra("org", dto.getOrganization());
                    intent.putExtra("credit", dto.getCredit());
                    this.context.startActivity(intent);
                }

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
                Intent intent = new Intent(this.context, ScheduledInvestmentActivity.class);
                intent.putExtra("DTO", dto);
                this.context.startActivityForResult(intent, 0);
            }

            break;

            */
        }

    }

}
