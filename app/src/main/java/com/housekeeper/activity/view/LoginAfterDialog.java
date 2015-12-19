package com.housekeeper.activity.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeeper.activity.BaseActivity;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 12/19/15.
 */
public class LoginAfterDialog extends Dialog implements View.OnClickListener {

    private BaseActivity context = null;

    private LinearLayout rootLayout = null;
    private TextView tenantTextView = null;
    private TextView landlordTextView = null;
    private TextView guguTextView = null;

    public LoginAfterDialog(Context context) {
        this(context, R.style.ProgressHUD);
    }

    public LoginAfterDialog(Context context, int theme) {
        super(context, theme);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        this.setContentView(R.layout.layout_login_after);

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        lp.width = (int) (this.context.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        this.getWindow().setAttributes(lp);

        this.rootLayout = (LinearLayout) this.findViewById(R.id.rootLayout);
        this.rootLayout.setOnClickListener(this);

        this.tenantTextView = (TextView) this.findViewById(R.id.tenantTextView);
        this.tenantTextView.setOnClickListener(this);

        this.landlordTextView = (TextView) this.findViewById(R.id.landlordTextView);
        this.landlordTextView.setOnClickListener(this);

        this.guguTextView = (TextView) this.findViewById(R.id.guguTextView);
        this.guguTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rootLayout:
                this.dismiss();
                break;

            case R.id.tenantTextView: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.wufriends.gugu"));
                context.startActivity(intent);
            }
            break;

            case R.id.landlordTextView: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.wufriends.gugu"));
                context.startActivity(intent);
            }
            break;

            case R.id.guguTextView: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.wufriends.gugu"));
                context.startActivity(intent);
            }
            break;

        }
    }

}
