package com.housekeeper.activity.view;

import android.content.Intent;
import android.net.Uri;
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
import com.housekeeper.activity.keeper.KeeperHouseInfoPublishActivity;
import com.housekeeper.activity.keeper.KeeperIDCardActivity;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.housekeeper.activity.keeper.KeeperRentRecordDetailActivity;
import com.housekeeper.activity.keeper.KeeperRentRecordListActivity;
import com.housekeeper.activity.keeper.KeeperReturnActivity;
import com.housekeeper.activity.keeper.KeeperSystemSettingActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RoleTypeEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 10/7/15.
 */
public class KeeperLeasedAdapter extends BaseAdapter {

    private BaseActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<LeasedListAppDto> list = new ArrayList<LeasedListAppDto>();

    private boolean cancel = false; // 是否是退租

    public KeeperLeasedAdapter(BaseActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<LeasedListAppDto> list, boolean cancel) {
        if (list == null)
            return;

        this.list = list;
        this.cancel = cancel;

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
            holder.surplusDayTextView = (TextView) convertView.findViewById(R.id.surplusDayTextView);

            holder.landlordInfoLayout = (LinearLayout) convertView.findViewById(R.id.landlordInfoLayout);
            holder.landlordLogoImageView = (CircleImageView) convertView.findViewById(R.id.landlordLogoImageView);
            holder.landlordNameTextView = (TextView) convertView.findViewById(R.id.landlordNameTextView);

            holder.landlordBtn = (Button) convertView.findViewById(R.id.landlordBtn);
            holder.tenantBtn = (Button) convertView.findViewById(R.id.tenantBtn);

            holder.returnBtn = (Button) convertView.findViewById(R.id.returnBtn);
            if (this.cancel) {
                holder.returnBtn.setBackgroundResource(R.drawable.keeper_img_33);
            } else {
                holder.returnBtn.setBackgroundResource(R.drawable.keeper_img_32);
            }

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
        holder.tenantAddressTextView.setText(infoDto.getWorkAddress());
        holder.surplusDayTextView.setText(infoDto.getSurplusDay() + "");

        holder.houseInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
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

        holder.landlordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + infoDto.getLandlordTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tenantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + infoDto.getUserTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancel) {
                    showCancelDialog(infoDto);
                } else {
                    Intent intent = new Intent(context, KeeperReturnActivity.class);
                    intent.putExtra("DTO", infoDto);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    private void showCancelDialog(LeasedListAppDto dto) {
        String content = "退租日期：" + dto.getTakeBackTime() + "\n应退金额：" + dto.getTakeBackMortgageMoney() + "元";

        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE).setTitleText("该房源已退租").setContentText(content).setConfirmText("确定").showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        }).show();
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
        private TextView surplusDayTextView;

        private LinearLayout landlordInfoLayout;
        private CircleImageView landlordLogoImageView;
        private TextView landlordNameTextView;

        private Button landlordBtn;
        private Button tenantBtn;
        private Button returnBtn;
    }
}
