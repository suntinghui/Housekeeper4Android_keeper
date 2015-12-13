package com.housekeeper.activity.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.EarningsListAppDto;
import com.ares.house.dto.app.MessageListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.utils.AdapterUtil;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sth on 12/10/15.
 */
public class YesterdayEarningsAdapter extends BaseAdapter {

    private BaseActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<EarningsListAppDto> list = new ArrayList<EarningsListAppDto>();

    public YesterdayEarningsAdapter(BaseActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<EarningsListAppDto> list) {
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

            convertView = layoutInflater.inflate(R.layout.item_yesterday_earnings, parent, false);

            holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.rootLayout);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.moneyTextView = (TextView) convertView.findViewById(R.id.moneyTextView);
            holder.tipTextView = (TextView) convertView.findViewById(R.id.tipTextView);
            holder.dqLayout = (LinearLayout) convertView.findViewById(R.id.dqLayout);
            holder.dqMoneyTextView = (TextView) convertView.findViewById(R.id.dqMoneyTextView);
            holder.hqLayout = (LinearLayout) convertView.findViewById(R.id.hqLayout);
            holder.hqMoneyTextView = (TextView) convertView.findViewById(R.id.hqMoneyTextView);
            holder.wybLayout = (LinearLayout) convertView.findViewById(R.id.wybLayout);
            holder.wybMoneyTextView = (TextView) convertView.findViewById(R.id.wybMoneyTextView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EarningsListAppDto dto = list.get(position);

        if (position % 2 == 0) {
            holder.rootLayout.setBackgroundResource(R.drawable.bg_orange_gray);
        } else {
            holder.rootLayout.setBackgroundResource(R.drawable.bg_white_gray);
        }

        holder.timeTextView.setText(dto.getTimeStr() + " 总收益(元)");
        holder.moneyTextView.setText(dto.getTotal());

        if (dto.isShow()) {
            holder.tipTextView.setVisibility(View.VISIBLE);
            holder.moneyTextView.setPadding(0, AdapterUtil.dip2px(context, 5), 0, 0);
        } else {
            holder.tipTextView.setVisibility(View.GONE);
            holder.moneyTextView.setPadding(0, AdapterUtil.dip2px(context, 15), 0, 0);
        }

        try {
            Float.parseFloat(dto.getDq());

            holder.dqLayout.setVisibility(View.VISIBLE);
            holder.dqMoneyTextView.setText(dto.getDq());
        } catch (Exception e) {
            holder.dqLayout.setVisibility(View.GONE);
        }

        try {
            Float.parseFloat(dto.getHq());

            holder.hqLayout.setVisibility(View.VISIBLE);
            holder.hqMoneyTextView.setText(dto.getHq());
        } catch (Exception e) {
            holder.hqLayout.setVisibility(View.GONE);
        }

        try {
            Float.parseFloat(dto.getWyb());

            holder.wybLayout.setVisibility(View.VISIBLE);
            holder.wybMoneyTextView.setText(dto.getWyb());
        } catch (Exception e) {
            holder.wybLayout.setVisibility(View.GONE);
        }


        return convertView;
    }

    public static final class ViewHolder {
        private LinearLayout rootLayout;
        private TextView timeTextView;
        private TextView moneyTextView;
        private TextView tipTextView;
        private LinearLayout dqLayout;
        private TextView dqMoneyTextView;
        private LinearLayout hqLayout;
        private TextView hqMoneyTextView;
        private LinearLayout wybLayout;
        private TextView wybMoneyTextView;
    }
}
