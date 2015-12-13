package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ares.house.dto.app.UserHouseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
import com.housekeeper.activity.tenant.TenantAgentInfoActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.TransferInfo;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 10/6/15.
 */
public class TenantMeItemLayout extends LinearLayout implements View.OnClickListener {

    private BaseActivity context;

    private LinearLayout infoLayout;
    private CustomNetworkImageView headImageView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView begingDateTextView;
    private TextView endDateTextView;

    private TextView rentTextView; // 租金
    private TextView propertyTextView; // 物业费
    private TextView depositTextView;//  押金
    private TextView heatingfeeTextView;// 取暖费

    private CircleProgress circleProgress;
    private TextView statusTextView; // 交租时间
    private TextView monthTextView; // 当前月份
    private TextView totalMonthTextView; // 总月份

    private LinearLayout firstMonthLayout = null;
    private TextView firstPayMoneyTextView; // 首付
    private TextView agencyFeeTextView; // 中介费用
    private TextView mortgageMoneyTextView; // 押金

    private LinearLayout otherMonthLayout;
    private TextView monthMoneyTextView;
    private ProgressBar progressBar;
    private TextView surplusDayTextView; // 距离交租日还剩几天

    private TextView totalPayTextView; // 总支付金额
    private Button payBtn;

    private LinearLayout keeperInfoLayout;
    private CircleImageView agentLogoImageView;
    private CustomNetworkImageView companyLogoImageView;
    private TextView agentUserNameTextView;
    private TextView companyNameTextView;

    private UserHouseListAppDto appDto;
    private String surplusMoney = "";

    public TenantMeItemLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public TenantMeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_tenant_me, this);

        this.infoLayout = (LinearLayout) this.findViewById(R.id.infoLayout);
        this.infoLayout.setOnClickListener(this);

        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.addressTextView = (TextView) this.findViewById(R.id.addressTextView);
        this.cityTextView = (TextView) this.findViewById(R.id.cityTextView);
        this.begingDateTextView = (TextView) this.findViewById(R.id.begingDateTextView);
        this.endDateTextView = (TextView) this.findViewById(R.id.endDateTextView);

        this.rentTextView = (TextView) this.findViewById(R.id.rentTextView);
        this.propertyTextView = (TextView) this.findViewById(R.id.propertyTextView);
        this.depositTextView = (TextView) this.findViewById(R.id.depositTextView);
        this.heatingfeeTextView = (TextView) this.findViewById(R.id.heatingfeeTextView);

        this.circleProgress = (CircleProgress) this.findViewById(R.id.circleProgress);
        this.circleProgress.setType(CircleProgress.ARC);
        this.circleProgress.setPaintWidth(30);
        this.circleProgress.setSubPaintColor(Color.parseColor("#B4E6FF"));
        this.circleProgress.setSecondPaintColor(Color.parseColor("#26B7FF"));

        this.circleProgress.setmMaxProgress(100);
        this.circleProgress.setmSubCurProgress(0);
        this.circleProgress.setmSecondCurProgress(0);

        this.statusTextView = (TextView) this.findViewById(R.id.statusTextView);
        this.monthTextView = (TextView) this.findViewById(R.id.monthTextView);
        this.totalMonthTextView = (TextView) this.findViewById(R.id.totalMonthTextView);

        this.firstMonthLayout = (LinearLayout) this.findViewById(R.id.firstMonthLayout);
        this.firstPayMoneyTextView = (TextView) this.findViewById(R.id.firstPayMoneyTextView);
        this.agencyFeeTextView = (TextView) this.findViewById(R.id.agencyFeeTextView);
        this.mortgageMoneyTextView = (TextView) this.findViewById(R.id.mortgageMoneyTextView);

        this.otherMonthLayout = (LinearLayout) this.findViewById(R.id.otherMonthLayout);
        this.monthMoneyTextView = (TextView) this.findViewById(R.id.monthMoneyTextView);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.surplusDayTextView = (TextView) this.findViewById(R.id.surplusDayTextView);

        this.totalPayTextView = (TextView) this.findViewById(R.id.totalPayTextView);
        this.payBtn = (Button) this.findViewById(R.id.payBtn);
        this.payBtn.setOnClickListener(this);

        this.keeperInfoLayout = (LinearLayout) this.findViewById(R.id.keeperInfoLayout);
        this.keeperInfoLayout.setOnClickListener(this);
        this.agentLogoImageView = (CircleImageView) this.findViewById(R.id.agentLogoImageView);
        this.companyLogoImageView = (CustomNetworkImageView) this.findViewById(R.id.companyLogoImageView);
        this.agentUserNameTextView = (TextView) this.findViewById(R.id.agentUserNameTextView);
        this.companyNameTextView = (TextView) this.findViewById(R.id.companyNameTextView);
    }

    public void setData(UserHouseListAppDto dto) {
        this.appDto = dto;

        headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        headImageView.setImageUrl(Constants.HOST_IP + appDto.getIndexImg(), ImageCacheManager.getInstance().getImageLoader());

        addressTextView.setText(appDto.getCommunity() + " " + appDto.getHouseNum());
        cityTextView.setText(appDto.getCityStr() + " " + appDto.getAreaStr() + " " + appDto.getAddress());
        begingDateTextView.setText(appDto.getBeginTimeStr());
        endDateTextView.setText(appDto.getEndTimeStr());

        // 租金 及 押金
        rentTextView.setText("租金：" + appDto.getMonthMoney() + " 元");
        depositTextView.setText("押金：" + appDto.getMortgageMoney() + " 元");
        propertyTextView.setText("物业费：" + (appDto.isPropertyFees() ? "租户承担" : "房东承担"));
        heatingfeeTextView.setText("取暖费：" + (appDto.isHeatingFees() ? "租户承担" : "房东承担"));

        this.circleProgress.setmMaxProgress(appDto.getTotalMonth());
        if (appDto.getPaymentStatus() == 'd') {
            this.circleProgress.setmSubCurProgress(appDto.getMonth());
            this.circleProgress.setmSecondCurProgress(appDto.getMonth());
        } else {
            this.circleProgress.setmSubCurProgress(appDto.getMonth());
            this.circleProgress.setmSecondCurProgress(appDto.getMonth() - 1);
        }

        if (appDto.getPaymentStatus() == 'd') {
            this.statusTextView.setText("本月已付");
        } else {
            if (appDto.isAtOncePay()) {
                this.statusTextView.setText("立即支付");
            } else {
                this.statusTextView.setText(appDto.getPayEndTimeStr());
            }
        }

        this.monthTextView.setText(appDto.getMonth() + "");
        this.totalMonthTextView.setText(appDto.getTotalMonth() + "");

        if (appDto.getPaymentStatus() != 'd' && dto.getMonth() == 1) {
            firstMonthLayout.setVisibility(View.VISIBLE);
            otherMonthLayout.setVisibility(View.GONE);

            firstPayMoneyTextView.setText(appDto.getTotalPay() + " 元");
            agencyFeeTextView.setText(appDto.getAgencyFee() + " 元");
            mortgageMoneyTextView.setText(appDto.getMortgageMoney() + " 元");

        } else {
            firstMonthLayout.setVisibility(View.GONE);
            otherMonthLayout.setVisibility(View.VISIBLE);

            monthMoneyTextView.setText(appDto.getMonthMoney() + " 元");
            progressBar.setProgress(100 - appDto.getSurplusDayPercentage());
            surplusDayTextView.setText(appDto.getSurplusDay() + "");
        }

        totalPayTextView.setText(appDto.getTotalPay() + " 元");

        if (dto.getPaymentStatus() == 'd') {
            payBtn.setText("已支付");
            payBtn.setEnabled(false);

        } else {
            payBtn.setText("立即付款");
            payBtn.setEnabled(true);
        }

        agentLogoImageView.setImageURL(Constants.HOST_IP + appDto.getAgentLogo());

        companyLogoImageView.setDefaultImageResId(R.drawable.head_keeper_default);
        companyLogoImageView.setErrorImageResId(R.drawable.head_keeper_default);
        companyLogoImageView.setLocalImageBitmap(R.drawable.head_keeper_default);
        companyLogoImageView.setImageUrl(Constants.HOST_IP + appDto.getCompanyLogo(), ImageCacheManager.getInstance().getImageLoader());

        agentUserNameTextView.setText(appDto.getAgentUserName() + "（您的房管家）");
        companyNameTextView.setText(appDto.getCompanyName());
    }

    public void setSurplusMoney(String surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoLayout: {
                Intent intent = new Intent(context, HouseInfoActivity.class);
                intent.putExtra("houseId", appDto.getHouseId() + "");
                context.startActivity(intent);
            }
            break;

            case R.id.payBtn: {
                try {
                    ConfirmPayDialog dialog = new ConfirmPayDialog(context);
                    dialog.setTransferInfo(new TransferInfo(appDto.getHouseId(), Double.parseDouble(appDto.getTotalPay()), Double.parseDouble(this.surplusMoney)));
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(context, "系统异常，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case R.id.keeperInfoLayout: {
                Intent intent = new Intent(context, TenantAgentInfoActivity.class);
                intent.putExtra("houseId", appDto.getHouseId() + "");
                context.startActivity(intent);
            }
            break;
        }
    }
}
