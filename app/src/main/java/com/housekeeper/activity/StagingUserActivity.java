package com.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeeper.activity.view.CreditReviceDialog;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

import java.util.Arrays;
import java.util.List;

public class StagingUserActivity extends BaseActivity implements OnClickListener {

    private CustomNetworkImageView headImageView;
    private TextView nameTextView;
    private TextView companyTextView;
    private TextView creditsTextView; // 授信额度
    private LinearLayout creditReviewLayout; // 审核授信
    private LinearLayout creditRecordsLayout; // 信用记录
    private TextView repaymentStateTextView;
    private TextView tipTextView;
    private Button addFriendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staging_user);

        initView();

        refreshView();
    }

    private void initView() {
        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("债权信息");

        headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        companyTextView = (TextView) this.findViewById(R.id.companyTextView);
        creditsTextView = (TextView) this.findViewById(R.id.creditsTextView);

        creditReviewLayout = (LinearLayout) this.findViewById(R.id.creditReviewLayout);
        creditReviewLayout.setOnClickListener(this);

        creditRecordsLayout = (LinearLayout) this.findViewById(R.id.creditRecordsLayout);
        creditRecordsLayout.setOnClickListener(this);

        repaymentStateTextView = (TextView) this.findViewById(R.id.repaymentStateTextView);
        tipTextView = (TextView) this.findViewById(R.id.tipTextView);

		/*
        String text = "对方发生延期、违约情况，『你好分期』平台自动收回分期用户“" + dto.getRealName() + "”的债务，为投资人自动退还债权本金。";

		SpannableStringBuilder style = new SpannableStringBuilder(text);
		int start = text.indexOf("『");
		int end = text.indexOf("』") + 1;
		style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tipTextView.setText(style);
		*/

        addFriendBtn = (Button) this.findViewById(R.id.addFriendBtn);
        addFriendBtn.setOnClickListener(this);
    }

    private void refreshView() {
        headImageView.setLocalImageBitmap(R.drawable.user_default_head);
        headImageView.setErrorImageResId(R.drawable.user_default_head);
        headImageView.setImageUrl(this.getIntent().getStringExtra("image"), ImageCacheManager.getInstance().getImageLoader());

        nameTextView.setText(this.getIntent().getStringExtra("realname"));
        companyTextView.setText("（" + this.getIntent().getStringExtra("org") + "）");
        creditsTextView.setText(this.getIntent().getStringExtra("credit") + "元");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.creditReviewLayout: // 信用审核
                showCreditRevew();
                break;

            case R.id.creditRecordsLayout: // 信用记录

                break;
        }

    }

    private void showCreditRevew() {
        List<String> itemList = Arrays.asList(new String[]{"房东让租期保障", "房屋代理合同", "房屋租赁合同",
                "租户押金保障", "租赁公司连带担保", "平台风险金保障"});

        CreditReviceDialog dialog = new CreditReviceDialog(this, itemList);
        dialog.show();
    }

}
