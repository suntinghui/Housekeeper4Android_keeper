package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ares.house.dto.app.UserJoinAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 10/8/15.
 * <p/>
 * <p/>
 * 租户信息
 */
public class KeeperLeaseInfoActivity extends BaseActivity implements View.OnClickListener {

    private WaitLeaseListAppDto infoDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_info);

        infoDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("身份证件");

        this.findViewById(R.id.idCardLayout).setOnClickListener(this);

        this.findViewById(R.id.addressLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.idCardLayout: {
                Intent intent = new Intent(this, KeeperLeaseIdCardActivity.class);
                intent.putExtra("leaseId", infoDto.getLeaseId() + "");
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                this.startActivity(intent);
            }
            break;

            case R.id.addressLayout: {
                Intent intent = new Intent(this, KeeperLeaseAddressActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;
        }
    }
}
