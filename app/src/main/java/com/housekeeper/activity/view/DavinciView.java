package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/12/15.
 * <p>
 * 左边图片，然后是标题式的文字，然后是解释类的小文字，然后是箭头
 */
public class DavinciView extends LinearLayout {

    private Context context;
    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView tipTextView;
    private ImageView rightArrowImageView;

    public DavinciView(Context context) {
        super(context);

        this.initView(context);
    }

    public DavinciView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_davinci, this);

        logoImageView = (ImageView) this.findViewById(R.id.logoImageView);
        titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        tipTextView = (TextView) this.findViewById(R.id.tipTextView);
        rightArrowImageView = (ImageView) this.findViewById(R.id.rightArrowImageView);
    }

    public ImageView getLogoImageView() {
        return logoImageView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getTipTextView() {
        return tipTextView;
    }

    public ImageView getRightArrowImageView() {
        return rightArrowImageView;
    }
}
