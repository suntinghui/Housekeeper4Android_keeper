package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeeper.activity.BaseActivity;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 12/14/15.
 */
public class HomeTopLayout extends LinearLayout implements View.OnClickListener {

    public static final int TYPE_LEASED = 0x01;
    public static final int TYPE_UNLEASE = 0x02;
    public static final int TYPE_CANCELLEASE = 0x03;

    private int type = TYPE_UNLEASE;

    private ItemChangeListener listener = null;

    private TextView unLeaseTextView; // 未租
    private TextView leasedTextView; // 已租
    private TextView cancelLeaseTextView; // 退租

    private BaseActivity context = null;

    public HomeTopLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public HomeTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_home_top, this);

        unLeaseTextView = (TextView) this.findViewById(R.id.unLeaseTextView);
        unLeaseTextView.setOnClickListener(this);
        unLeaseTextView.setTag(TYPE_UNLEASE);

        leasedTextView = (TextView) this.findViewById(R.id.leasedTextView);
        leasedTextView.setOnClickListener(this);
        leasedTextView.setTag(TYPE_LEASED);

        cancelLeaseTextView = (TextView) this.findViewById(R.id.cancelLeaseTextView);
        cancelLeaseTextView.setOnClickListener(this);
        cancelLeaseTextView.setTag(TYPE_CANCELLEASE);

        // 默认未租
        type = TYPE_UNLEASE;
        this.unLeaseTextView.setSelected(true);
        this.leasedTextView.setSelected(false);
        this.cancelLeaseTextView.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        if (this.type == (int) view.getTag())
            return;

        switch (view.getId()) {
            case R.id.unLeaseTextView:
                this.type = TYPE_UNLEASE;

                this.unLeaseTextView.setSelected(true);
                this.leasedTextView.setSelected(false);
                this.cancelLeaseTextView.setSelected(false);
                break;

            case R.id.leasedTextView:
                this.type = TYPE_LEASED;

                this.unLeaseTextView.setSelected(false);
                this.leasedTextView.setSelected(true);
                this.cancelLeaseTextView.setSelected(false);
                break;

            case R.id.cancelLeaseTextView:
                this.type = TYPE_CANCELLEASE;

                this.unLeaseTextView.setSelected(false);
                this.leasedTextView.setSelected(false);
                this.cancelLeaseTextView.setSelected(true);
                break;
        }

        if (this.listener != null) {
            this.listener.onItemChanged(this.type);
        }
    }

    public void setOnItemChangeListener(ItemChangeListener listener) {
        this.listener = listener;
    }

    public interface ItemChangeListener {
        public void onItemChanged(int type);
    }
}
