package com.housekeeper.activity.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ares.house.dto.app.DebtPackageAppDto;
import com.housekeeper.activity.VoteOfScheduledActivity;
import com.housekeeper.utils.AdapterUtil;
import com.wufriends.housekeeper.keeper.R;

public class VoteOfScheduledAdapter extends BaseAdapter {

    private Context context;
    private List<DebtPackageAppDto> deptList = new ArrayList<DebtPackageAppDto>();

    public VoteOfScheduledAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DebtPackageAppDto> tempList) {
        if (tempList == null)
            return;

        this.deptList = tempList;
    }

    @Override
    public int getCount() {
        return deptList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_vote_scheduled, null);

            holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);
            holder.preTitleTextView = (TextView) convertView.findViewById(R.id.preTitleTextView);
            holder.numTextView = (TextView) convertView.findViewById(R.id.numTextView);
            holder.rateTextView = (TextView) convertView.findViewById(R.id.rateTextView);
            holder.periodTextView = (TextView) convertView.findViewById(R.id.periodTextView);
            holder.totalMoneyTextView = (TextView) convertView.findViewById(R.id.totalMoneyTextView);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.tipTextView = (TextView) convertView.findViewById(R.id.tipTextView);
            holder.limitAmountTextView = (TextView) convertView.findViewById(R.id.limitAmountTextView);
            holder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            holder.bottomDividerView = convertView.findViewById(R.id.bottomDividerView);

            holder.tempTextView1 = (TextView) convertView.findViewById(R.id.tempTextView1);
            holder.tempTextView2 = (TextView) convertView.findViewById(R.id.tempTextView2);
            holder.tempTextView3 = (TextView) convertView.findViewById(R.id.tempTextView3);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DebtPackageAppDto debtDto = deptList.get(position);

        holder.preTitleTextView.setText(debtDto.getSourceTypeName());
        holder.numTextView.setText(debtDto.getNum());
        holder.rateTextView.setText(debtDto.getMaxRate() + "");
        holder.periodTextView.setText(debtDto.getMaxPeriod() + "");
        holder.totalMoneyTextView.setText(debtDto.getTotalMoney2());
        holder.limitAmountTextView.setText(debtDto.getLimitMoney() + "元起 日返息");

        holder.progressBar.setProgress(this.calcProgress(debtDto.getSurplus(), debtDto.getTotalMoney()));


        // a发行中 b已满返息中 c已完成
        if (debtDto.getStatus() == 'a') {
            setEnable(holder, true);

            holder.statusImageView.setBackgroundResource(R.drawable.investment_status_sell);

        } else if (debtDto.getStatus() == 'b') {
            setEnable(holder, false);

            holder.statusImageView.setBackgroundResource(R.drawable.investment_status_repayment);

        } else if (debtDto.getStatus() == 'c') {
            setEnable(holder, false);

            holder.statusImageView.setBackgroundResource(R.drawable.investment_status_complete);
        }

        if (deptList.size() > 2 && position == deptList.size() - 1) {
            holder.bottomDividerView.setVisibility(View.VISIBLE);
        } else {
            holder.bottomDividerView.setVisibility(View.GONE);
        }

        holder.contentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoteOfScheduledActivity.class);
                intent.putExtra("id", debtDto.getId() + "");
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private int calcProgress(String surplusStr, String totalMoneyStr) {
        double surplus = Double.parseDouble(surplusStr);
        double total = Double.parseDouble(totalMoneyStr);
        int progress = 100 - (int) (100 * surplus / total);

        if (progress == 100)
            return 0;

        return progress;
    }

    private void setEnable(ViewHolder holder, boolean enable) {
        if (enable) {
            holder.preTitleTextView.setTextColor(Color.parseColor("#1caff6"));
            holder.numTextView.setTextColor(Color.parseColor("#666666"));
            holder.rateTextView.setTextColor(Color.parseColor("#FF001A"));
            holder.periodTextView.setTextColor(Color.parseColor("#666666"));
            holder.totalMoneyTextView.setTextColor(Color.parseColor("#666666"));

            holder.tipTextView.setTextColor(Color.parseColor("#666666"));
            holder.tipTextView.setCompoundDrawablePadding(AdapterUtil.dip2px(context, 5));
            holder.tipTextView.setCompoundDrawables(getDrawable(R.drawable.dt_img_01), null, null, null);

            holder.limitAmountTextView.setTextColor(Color.parseColor("#666666"));
            holder.limitAmountTextView.setCompoundDrawablePadding(AdapterUtil.dip2px(context, 5));
            holder.limitAmountTextView.setCompoundDrawables(getDrawable(R.drawable.dt_img_03), null, null, null);

            holder.tempTextView1.setTextColor(Color.parseColor("#FF001A"));
            holder.tempTextView2.setTextColor(Color.parseColor("#666666"));
            holder.tempTextView3.setTextColor(Color.parseColor("#666666"));


        } else {
            holder.preTitleTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.numTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.rateTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.periodTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.totalMoneyTextView.setTextColor(Color.parseColor("#cccccc"));

            holder.tipTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.tipTextView.setCompoundDrawablePadding(AdapterUtil.dip2px(context, 5));
            holder.tipTextView.setCompoundDrawables(getDrawable(R.drawable.dt_img_02), null, null, null);

            holder.limitAmountTextView.setTextColor(Color.parseColor("#cccccc"));
            holder.limitAmountTextView.setCompoundDrawablePadding(AdapterUtil.dip2px(context, 5));
            holder.limitAmountTextView.setCompoundDrawables(getDrawable(R.drawable.dt_img_04), null, null, null);

            holder.tempTextView1.setTextColor(Color.parseColor("#cccccc"));
            holder.tempTextView2.setTextColor(Color.parseColor("#cccccc"));
            holder.tempTextView3.setTextColor(Color.parseColor("#cccccc"));
        }
    }

    private Drawable getDrawable(int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public final class ViewHolder {
        private LinearLayout contentLayout;
        private TextView preTitleTextView;
        private TextView numTextView;
        private TextView rateTextView;
        private TextView periodTextView;
        private TextView totalMoneyTextView;
        private ProgressBar progressBar;
        private TextView tipTextView; // 保本保息 到期还本
        private TextView limitAmountTextView; // 200元起 日返息
        private ImageView statusImageView;
        private View bottomDividerView;

        private TextView tempTextView1;
        private TextView tempTextView2;
        private TextView tempTextView3;
    }

    private SpannableString getFormatValue(String text) {
        int length = text.length();
        SpannableString ss = new SpannableString(text);
        try {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(14, true), length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

}
