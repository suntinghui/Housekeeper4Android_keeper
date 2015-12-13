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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.DebtPackageInfoAppDto;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.housekeeper.activity.StagingUserActivity;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

/**
 * 定投项目详情
 *
 * @author sth
 */
public class ItemDescOfTransferView extends LinearLayout implements OnClickListener {

    private LinearLayout fenqiLayout;
    private CustomNetworkImageView fenqiHeadImageView;
    private TextView nameTextView;
    private TextView companyTextView;
    private TextView fenqiDescTextView;

    private TextView totalAmountTextView;
    private TextView transferMoneyTextView; // 转让债权
    private TextView surplusTextView; // 剩余期数
    private TextView purchaseIncentivesTextView; // 收购奖励
    private TextView expectedRateTextView; // 预期年化利率

    private LinearLayout itemDescLayout; // 产品详情
    private TextView repaymentTypeTextView; // 还款方式

    private LinearLayout investmentRecordsLayout; // 投资记录
    private TextView recordsCountTextView; // 投资人数

    private Button purchaseBtn; // 收购债权
    private TextView rewardRateTextView; // 加息奖励

    private Activity context;

    private DebtPackageInfoAppDto dto;

    public ItemDescOfTransferView(Activity context) {
        super(context);

        initView(context);
    }

    public ItemDescOfTransferView(Activity context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    private void initView(Activity context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_transfer_item_desp, this);

        fenqiLayout = (LinearLayout) this.findViewById(R.id.fenqiLayout);
        fenqiLayout.setOnClickListener(this);

        fenqiHeadImageView = (CustomNetworkImageView) this.findViewById(R.id.fenqiHeadImageView);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        companyTextView = (TextView) this.findViewById(R.id.companyTextView);
        fenqiDescTextView = (TextView) this.findViewById(R.id.fenqiDescTextView);

        totalAmountTextView = (TextView) this.findViewById(R.id.totalAmountTextView);
        transferMoneyTextView = (TextView) this.findViewById(R.id.transferMoneyTextView);
        purchaseIncentivesTextView = (TextView) this.findViewById(R.id.purchaseIncentivesTextView);
        surplusTextView = (TextView) this.findViewById(R.id.surplusTextView);
        expectedRateTextView = (TextView) this.findViewById(R.id.expectedRateTextView);

        itemDescLayout = (LinearLayout) this.findViewById(R.id.itemDescLayout);
        itemDescLayout.setOnClickListener(this);

        repaymentTypeTextView = (TextView) this.findViewById(R.id.repaymentTypeTextView);

        investmentRecordsLayout = (LinearLayout) this.findViewById(R.id.investmentRecordsLayout);
        investmentRecordsLayout.setOnClickListener(this);

        recordsCountTextView = (TextView) this.findViewById(R.id.recordsCountTextView);

        purchaseBtn = (Button) this.findViewById(R.id.purchaseBtn);
        purchaseBtn.setOnClickListener(this);

        rewardRateTextView = (TextView) this.findViewById(R.id.rewardRateTextView);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            YoYo.with(Techniques.Bounce).duration(800).playOn(rewardRateTextView);

            handler.postDelayed(this, 1000); // postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中
        }
    };

    public void setValue(DebtPackageInfoAppDto dto) {
        if (dto == null)
            return;

        this.dto = dto;

        fenqiHeadImageView.setDefaultImageResId(R.drawable.fenqi_head_default);
        fenqiHeadImageView.setErrorImageResId(R.drawable.fenqi_head_default);
        fenqiHeadImageView.setLocalImageBitmap(R.drawable.fenqi_head_default);
        fenqiHeadImageView.setImageUrl(dto.getLogoImg(), ImageCacheManager.getInstance().getImageLoader());

        nameTextView.setText(dto.getRealName());

        companyTextView.setText("(" + dto.getOrganization() + ")");

        fenqiDescTextView.setText("分期消费：" + dto.getGoodsName() + " ￥" + dto.getGoodsPrice());

        totalAmountTextView.setText(this.dto.getTotalMoney());
        transferMoneyTextView.setText(this.dto.getMaxMoney());
        surplusTextView.setText(this.dto.getMaxPeriod() + "");
        purchaseIncentivesTextView.setText(this.dto.getReward());
        expectedRateTextView.setText(this.dto.getMinRate() + "%");

        repaymentTypeTextView.setText("按天付息，到期还本");
        recordsCountTextView.setText(this.dto.getPeople() + "");

        if (null != dto.getRewardRate()) {
            rewardRateTextView.setText("加息" + dto.getRewardRate() + "%");
            handler.postDelayed(runnable, 1000); // 开始Timer
        }
    }

    @Override
    public void onClick(View v) {
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

		case R.id.purchaseBtn: { // 收购债权
			Intent intent = new Intent(this.context, TransferInvestmentActivity.class);
			intent.putExtra("DTO", dto);
			this.context.startActivityForResult(intent, 0);
		}

			break;

			*/
        }

    }

}
