package com.housekeeper.activity.view;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
import com.housekeeper.activity.keeper.KeeperHomeActivity;
import com.housekeeper.activity.keeper.KeeperHouseInfoPublishActivity;
import com.housekeeper.activity.keeper.KeeperHousePublishActivity;
import com.housekeeper.activity.keeper.KeeperLeaseRelationActivity;
import com.housekeeper.activity.keeper.KeeperReserveListActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sth on 10/7/15.
 */
public class KeeperUnLeaseAdapter extends BaseAdapter {

    private BaseActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<WaitLeaseListAppDto> list = new ArrayList<WaitLeaseListAppDto>();

    public KeeperUnLeaseAdapter(BaseActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<WaitLeaseListAppDto> list) {
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

            convertView = layoutInflater.inflate(R.layout.layout_keeper_unlease, parent, false);

            holder.infoLayout = (LinearLayout) convertView.findViewById(R.id.infoLayout);
            holder.addLayout = (LinearLayout) convertView.findViewById(R.id.addLayout);
            holder.publicLayout = (LinearLayout) convertView.findViewById(R.id.publicLayout);
            holder.publicStatusTextView = (TextView) convertView.findViewById(R.id.publicStatusTextView);

            holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
            holder.addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
            holder.cityTextView = (TextView) convertView.findViewById(R.id.cityTextView);
            holder.letLeaseDayTextView = (TextView) convertView.findViewById(R.id.letLeaseDayTextView);

            holder.middleLayout = (LinearLayout) convertView.findViewById(R.id.middleLayout);

            holder.reserveLayout = (LinearLayout) convertView.findViewById(R.id.reserveLayout);
            holder.reserveCountTextView = (TextView) convertView.findViewById(R.id.reserveCountTextView);

            holder.tipTextView1 = (TextView) convertView.findViewById(R.id.tipTextView1);
            holder.tipTextView2 = (TextView) convertView.findViewById(R.id.tipTextView2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WaitLeaseListAppDto infoDto = list.get(position);

        holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        holder.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
        holder.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
        holder.letLeaseDayTextView.setText(infoDto.getLetLeaseDay() + "");
        holder.reserveCountTextView.setText(infoDto.getReserveCount() + "");

        holder.tipTextView1.setText(Html.fromHtml("用于<font color=#24B0F1>租金</font>和<font color=#24B0F1>租赁合同</font>"));

        holder.tipTextView2.setText(Html.fromHtml("发布房源<font color=#F5933C>让更多租户可见</font>"));

        if (infoDto.isRelease()) {
            holder.middleLayout.setBackgroundColor(Color.parseColor("#F0FFFB"));
            holder.publicStatusTextView.setText(infoDto.getLeaseMonthMoney() + " 元/月");
            holder.publicStatusTextView.setTextColor(context.getResources().getColor(R.color.orange));

        } else {
            holder.middleLayout.setBackgroundColor(Color.parseColor("#FEEEC8"));
            holder.publicStatusTextView.setText("未发布");
            holder.publicStatusTextView.setTextColor(Color.parseColor("#999999"));
        }

        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivityForResult(intent, KeeperHomeActivity.NOT_REFRESH);
            }
        });

        holder.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperLeaseRelationActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivityForResult(intent, KeeperHomeActivity.NEED_REFRESH);
            }
        });

        holder.publicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoDto.isRelease()) {
                    Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
                    intent.putExtra("houseId", infoDto.getHouseId() + "");
                    context.startActivityForResult(intent, KeeperHomeActivity.NOT_REFRESH);

                } else {
                    Intent intent = new Intent(context, KeeperHousePublishActivity.class);
                    intent.putExtra("houseId", infoDto.getHouseId() + "");
                    context.startActivityForResult(intent, KeeperHomeActivity.NEED_REFRESH);
                }
            }
        });

        holder.reserveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperReserveListActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivityForResult(intent, KeeperHomeActivity.NOT_REFRESH);
            }
        });

        return convertView;
    }

    public static final class ViewHolder {
        private LinearLayout infoLayout;
        private LinearLayout addLayout;
        private LinearLayout publicLayout;
        private TextView publicStatusTextView;

        private CustomNetworkImageView headImageView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView letLeaseDayTextView;

        private LinearLayout middleLayout;

        private LinearLayout reserveLayout;
        private TextView reserveCountTextView;

        private TextView tipTextView1;
        private TextView tipTextView2;
    }
}
