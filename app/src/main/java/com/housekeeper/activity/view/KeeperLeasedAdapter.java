package com.housekeeper.activity.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.LeasedListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
import com.housekeeper.activity.keeper.KeeperIDCardActivity;
import com.housekeeper.activity.keeper.KeeperRentRecordDetailActivity;
import com.housekeeper.activity.keeper.KeeperRentRecordListActivity;
import com.housekeeper.activity.keeper.KeeperReturnActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 10/7/15.
 */
public class KeeperLeasedAdapter extends BaseAdapter {

    private BaseActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<LeasedListAppDto> list = new ArrayList<LeasedListAppDto>();

    public KeeperLeasedAdapter(BaseActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);

    }

    public void setData(List<LeasedListAppDto> list) {
        if (list == null)
            return;

        this.list = list;

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.layout_keeper_leased, parent, false);

            holder.houseInfoLayout = (LinearLayout) convertView.findViewById(R.id.houseInfoLayout);
            holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
            holder.addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
            holder.cityTextView = (TextView) convertView.findViewById(R.id.cityTextView);
            holder.begingDateTextView = (TextView) convertView.findViewById(R.id.begingDateTextView);
            holder.endDateTextView = (TextView) convertView.findViewById(R.id.endDateTextView);

            holder.tenantInfoLayout = (LinearLayout) convertView.findViewById(R.id.tenantInfoLayout);
            holder.tenantLogoImageView = (CircleImageView) convertView.findViewById(R.id.tenantLogoImageView);
            holder.tenantNameTextView = (TextView) convertView.findViewById(R.id.tenantNameTextView);
            holder.tenantTelphoneTextView = (TextView) convertView.findViewById(R.id.tenantTelphoneTextView);
            holder.tenantAddressTextView = (TextView) convertView.findViewById(R.id.tenantAddressTextView);

            holder.landlordInfoLayout = (LinearLayout) convertView.findViewById(R.id.landlordInfoLayout);
            holder.landlordLogoImageView = (CircleImageView) convertView.findViewById(R.id.landlordLogoImageView);
            holder.landlordNameTextView = (TextView) convertView.findViewById(R.id.landlordNameTextView);

            holder.returnTextView = (TextView) convertView.findViewById(R.id.returnTextView);
            holder.returnBtn = (Button) convertView.findViewById(R.id.returnBtn);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LeasedListAppDto infoDto = list.get(position);

        holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        holder.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
        holder.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
        holder.begingDateTextView.setText(infoDto.getBeginTimeStr());
        holder.endDateTextView.setText(infoDto.getEndTimeStr());

        holder.tenantLogoImageView.setImageURL(Constants.HOST_IP + infoDto.getUserLogo());
        holder.tenantNameTextView.setText(infoDto.getUserName());
        //holder.tenantTelphoneTextView.setText(infoDto.getUserBankCard());
        holder.tenantAddressTextView.setText(infoDto.getWorkAddress());

        //holder.landlordLogoImageView.setImageURL(Constants.HOST_IP + infoDto.getLandlordLogo());
        //holder.landlordNameTextView.setText(infoDto.getLandlordUserName());

        // b 正常 c退租中  d已完成
        if (infoDto.getStatus() == 'b') {
            holder.returnTextView.setVisibility(View.GONE);
            holder.returnBtn.setVisibility(View.VISIBLE);
            holder.returnBtn.setEnabled(true);
        } else if (infoDto.getStatus() == 'c') {
            holder.returnTextView.setVisibility(View.GONE);
            holder.returnBtn.setVisibility(View.VISIBLE);
            holder.returnBtn.setText("退租中");
            holder.returnBtn.setEnabled(false);
        } else if (infoDto.getStatus() == 'd') {
            holder.returnTextView.setVisibility(View.VISIBLE);
            holder.returnBtn.setVisibility(View.GONE);
            holder.returnBtn.setEnabled(false);
            holder.returnTextView.setText("该房源已经申请退租，退租时间是" + infoDto.getTakeBackTime() + "，需退还金额" + infoDto.getTakeBackMortgageMoney() + "元。");
        }

        holder.houseInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HouseInfoActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivity(intent);
            }
        });

        holder.tenantInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperRentRecordDetailActivity.class);
                intent.putExtra("leaseId", infoDto.getLeaseId() + "");
                context.startActivity(intent);
            }
        });

        holder.landlordInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperReturnActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public static final class ViewHolder {
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

        private LinearLayout landlordInfoLayout;
        private CircleImageView landlordLogoImageView;
        private TextView landlordNameTextView;

        private TextView returnTextView;
        private Button returnBtn;
    }
}
