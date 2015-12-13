package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/30/15.
 */
public class XuRadioView extends LinearLayout {

    private Context context;

    private LinearLayout rootLayout;
    private TextView titleTextView;
    private CheckBox checkbox;

    private boolean checked = false;

    public XuRadioView(Context context) {
        super(context);

        this.initView(context);
    }

    public XuRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_xuradio, this);

        this.rootLayout = (LinearLayout) this.findViewById(R.id.rootLayout);

        this.titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        this.checkbox = (CheckBox) this.findViewById(R.id.checkbox);
    }

    public void setTitle(String title) {
        this.titleTextView.setText(title);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        this.rootLayout.setSelected(this.checked);
        this.checkbox.setChecked(this.checked);
    }

    public boolean isChecked() {
        return checked;
    }
}
