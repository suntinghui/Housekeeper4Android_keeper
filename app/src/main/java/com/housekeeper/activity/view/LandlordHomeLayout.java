package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.LandlordHouseListAppDto;
import com.ares.house.dto.app.LeasedListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
import com.housekeeper.activity.keeper.KeeperLeaseIdCardActivity;
import com.housekeeper.activity.tenant.TenantAgentInfoActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 10/7/15.
 */
public class LandlordHomeLayout extends LinearLayout {

    private BaseActivity context = null;

    private LinearLayout houseInfoLayout;
    private CustomNetworkImageView headImageView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView begingDateTextView;
    private TextView endDateTextView;

    private LinearLayout tenantInfoLayout;
    private CircleImageView tenantLogoImageView;
    private TextView tenantNameTextView;
    private TextView tenantTelphoneTextView;
    private TextView tenantAddressTextView;

    private LinearLayout keeperInfoLayout;
    private CircleImageView agentLogoImageView;
    private CustomNetworkImageView companyLogoImageView;
    private TextView agentUserNameTextView;
    private TextView companyNameTextView;

    public LandlordHomeLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public LandlordHomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_landlord_home, this);

        this.houseInfoLayout = (LinearLayout) this.findViewById(R.id.houseInfoLayout);
        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.addressTextView = (TextView) this.findViewById(R.id.addressTextView);
        this.cityTextView = (TextView) this.findViewById(R.id.cityTextView);
        this.begingDateTextView = (TextView) this.findViewById(R.id.begingDateTextView);
        this.endDateTextView = (TextView) this.findViewById(R.id.endDateTextView);

        this.tenantInfoLayout = (LinearLayout) this.findViewById(R.id.tenantInfoLayout);
        this.tenantLogoImageView = (CircleImageView) this.findViewById(R.id.tenantLogoImageView);
        this.tenantNameTextView = (TextView) this.findViewById(R.id.tenantNameTextView);
        this.tenantTelphoneTextView = (TextView) this.findViewById(R.id.tenantTelphoneTextView);
        this.tenantAddressTextView = (TextView) this.findViewById(R.id.tenantAddressTextView);

        this.keeperInfoLayout = (LinearLayout) this.findViewById(R.id.keeperInfoLayout);
        this.agentLogoImageView = (CircleImageView) this.findViewById(R.id.agentLogoImageView);
        this.companyLogoImageView = (CustomNetworkImageView) this.findViewById(R.id.companyLogoImageView);
        this.agentUserNameTextView = (TextView) this.findViewById(R.id.agentUserNameTextView);
        this.companyNameTextView = (TextView) this.findViewById(R.id.companyNameTextView);
    }

    public void setData(final LandlordHouseListAppDto infoDto) {
        this.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        this.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        this.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        this.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        this.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
        this.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
        this.begingDateTextView.setText(infoDto.getBeginTimeStr());
        this.endDateTextView.setText(infoDto.getEndTimeStr());

        this.tenantLogoImageView.setImageURL(Constants.HOST_IP + infoDto.getUserLogo());
        this.tenantNameTextView.setText(infoDto.getUserName());
        this.tenantTelphoneTextView.setText(infoDto.getUserBankCard());
        this.tenantAddressTextView.setText(infoDto.getWorkAddress());

        agentLogoImageView.setImageURL(Constants.HOST_IP + infoDto.getAgentLogo());

        companyLogoImageView.setDefaultImageResId(R.drawable.head_keeper_default);
        companyLogoImageView.setErrorImageResId(R.drawable.head_keeper_default);
        companyLogoImageView.setLocalImageBitmap(R.drawable.head_keeper_default);
        companyLogoImageView.setImageUrl(Constants.HOST_IP + infoDto.getCompanyLogo(), ImageCacheManager.getInstance().getImageLoader());

        agentUserNameTextView.setText(infoDto.getAgentName() + "（您的房管家）");
        companyNameTextView.setText(infoDto.getCompanyName());

        this.houseInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HouseInfoActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivity(intent);
            }
        });

        this.tenantInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperLeaseIdCardActivity.class);
                intent.putExtra("leaseId", infoDto.getLeaseId() + "");
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                intent.putExtra("editable", false);
                context.startActivity(intent);
            }
        });

        this.keeperInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TenantAgentInfoActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivity(intent);
            }
        });

    }

}
