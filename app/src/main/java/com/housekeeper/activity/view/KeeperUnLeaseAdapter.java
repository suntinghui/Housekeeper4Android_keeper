package com.housekeeper.activity.view;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
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

            holder.reserveLayout = (LinearLayout) convertView.findViewById(R.id.reserveLayout);
            holder.reserveCountTextView = (TextView) convertView.findViewById(R.id.reserveCountTextView);

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

        if (infoDto.isRelease()) {
            holder.publicStatusTextView.setText(infoDto.getLeaseMonthMoney() + " 元/月");
            holder.publicStatusTextView.setTextColor(context.getResources().getColor(R.color.orange));

        } else {
            holder.publicStatusTextView.setText("未发布");
            holder.publicStatusTextView.setTextColor(Color.parseColor("#999999"));
        }

        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HouseInfoActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivity(intent);
            }
        });

        holder.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperLeaseRelationActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivityForResult(intent, 0);
            }
        });

        holder.publicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoDto.isRelease()) {
                    Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
                    intent.putExtra("houseId", infoDto.getHouseId() + "");
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, KeeperHousePublishActivity.class);
                    intent.putExtra("houseId", infoDto.getHouseId() + "");
                    context.startActivityForResult(intent, 0);
                }
            }
        });

        holder.reserveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperReserveListActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivity(intent);
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

        private LinearLayout reserveLayout;
        private TextView reserveCountTextView;
    }
}
