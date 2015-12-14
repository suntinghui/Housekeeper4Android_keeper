package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.housekeeper.activity.BaseActivity;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 12/14/15.
 */
public class EmptyLayout extends LinearLayout {

    private BaseActivity context = null;

    private ImageView noDataImageView = null;

    public EmptyLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_empty, this);

        noDataImageView = (ImageView) this.findViewById(R.id.noDataImageView);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        noDataImageView.setPadding(0, height / 5, 0, 0);

    }

    public ImageView getEmptyImageView() {
        return noDataImageView;
    }
}
