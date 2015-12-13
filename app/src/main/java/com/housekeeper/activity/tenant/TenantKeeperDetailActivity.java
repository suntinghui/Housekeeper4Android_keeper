package com.housekeeper.activity.tenant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ares.house.dto.app.AgentUserInfoAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 10/30/15.
 * <p/>
 * 验证资料，中介信息
 */
public class TenantKeeperDetailActivity extends BaseActivity implements View.OnClickListener {

    private CustomNetworkImageView logoImageView;
    private TextView nameTextView;
    private TextView addressTextView;

    private AgentUserInfoAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_keeper_detail);

        infoDto = (AgentUserInfoAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("验证资料");

        this.logoImageView = (CustomNetworkImageView) this.findViewById(R.id.logoImageView);
        this.nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        this.addressTextView = (TextView) this.findViewById(R.id.addressTextView);

        this.logoImageView.setImageUrl(Constants.HOST_IP + infoDto.getLogoUrl(), ImageCacheManager.getInstance().getImageLoader());
        this.nameTextView.setText(infoDto.getCompanyName());
        this.addressTextView.setText(infoDto.getCompanyAddress());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;
        }

    }
}
