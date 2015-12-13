package com.housekeeper.activity.view;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.housekeeper.activity.BaseActivity;
import com.wufriends.housekeeper.keeper.R;

public class CreditReviceDialog extends Dialog {

    private BaseActivity context;

    private LinearLayout contentLayout = null;
    private Button confirmBtn = null;

    private List<String> itemList = Arrays.asList(new String[]{"房东让租期保障", "房屋代理合同", "房屋租赁合同",
            "租户押金保障", "租赁公司连带担保", "平台风险金保障"});

    public CreditReviceDialog(BaseActivity context, List<String> itemList) {
        this(context, R.style.ProgressHUD, itemList);
    }

    public CreditReviceDialog(BaseActivity context, int theme, List<String> itemList) {
        super(context, theme);

        if (itemList != null) {
            this.itemList = itemList;
        }

        this.initView(context);
    }

    private void initView(BaseActivity ctx) {
        this.context = ctx;

        this.setContentView(R.layout.layout_credit_review_dialog);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        this.getWindow().setAttributes(lp);

        contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);

        int i = 0;
        for (String title : itemList) {
            final CreditReviewItem item = new CreditReviewItem(context);
            item.setTitle(title);
            contentLayout.addView(item, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    item.playAnimation();
                }
            }, i++ * 450);
        }

        confirmBtn = (Button) this.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
