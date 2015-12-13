package com.housekeeper.activity.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.HouseReleaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HouseInfoActivity;
import com.housekeeper.activity.keeper.KeeperHouseInfoPublishActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sth on 10/29/15.
 */
public class PublishRoommateAdapter extends BaseAdapter {

    private BaseActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<HouseReleaseListAppDto> list = new ArrayList<HouseReleaseListAppDto>();

    public PublishRoommateAdapter(BaseActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<HouseReleaseListAppDto> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.layout_publish_roommate, parent, false);

            holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.rootLayout);
            holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
            holder.communityTextView = (TextView) convertView.findViewById(R.id.communityTextView);
            holder.areaExTextView = (TextView) convertView.findViewById(R.id.areaExTextView);
            holder.monthMoneyTextView = (TextView) convertView.findViewById(R.id.monthMoneyTextView);
            holder.flowlayout = (TagFlowLayout) convertView.findViewById(R.id.flowlayout);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HouseReleaseListAppDto infoDto = list.get(position);

        holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        holder.communityTextView.setText(infoDto.getCommunity() + "   " + infoDto.getHouseType());
        holder.areaExTextView.setText(infoDto.getAreaStr() + " • " + infoDto.getLeaseType() + " • " + infoDto.getSize());
        holder.monthMoneyTextView.setText(infoDto.getMonthMoney());
        holder.flowlayout.setEnabled(false);
        holder.flowlayout.setAdapter(new TagAdapter<String>(infoDto.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_layout, parent, false);
                tv.setText(s);
                tv.setTextSize(11);
                return tv;
            }
        });

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public static final class ViewHolder {
        private LinearLayout rootLayout;
        private CustomNetworkImageView headImageView;
        private TextView communityTextView;
        private TextView areaExTextView;
        private TextView monthMoneyTextView;
        private TagFlowLayout flowlayout;

    }
}
