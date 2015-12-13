package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ares.house.dto.app.ContactAppDto;
import com.ares.house.dto.app.OpenCityAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.Constants;
import com.wufriends.housekeeper.keeper.R;


/**
 * Created by sth on 10/31/15.
 */
public class ContactCityLayout extends LinearLayout implements View.OnClickListener {

    private BaseActivity context;

    private LinearLayout contentLayout;
    private TextView nameTextView;
    private TextView telphoneTextView;
    private Button dialButton;

    private OpenCityAppDto dto;

    public ContactCityLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public ContactCityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_contact_city, this);

        this.contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
        this.nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        this.telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);
        this.dialButton = (Button) this.findViewById(R.id.dialButton);

        this.contentLayout.setOnClickListener(this);
        this.dialButton.setOnClickListener(this);
    }

    public void setData(OpenCityAppDto dto){
        this.dto = dto;

        this.nameTextView.setText(dto.getName());
        this.telphoneTextView.setText(dto.getTelphone());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dto.getTelphone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
