package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ares.house.dto.app.HouseAddListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.DavinciView;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/25/15.
 */
public class KeeperAddHouseInfoActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView houseInfoView = null; // 房源基本信息
    private DavinciView houseImageView = null; // 房屋照片
    private DavinciView equipmentView = null; // 配套设施
    private DavinciView trafficInfoView = null; // 交通信息

    private HouseAddListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_info);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("房屋信息");

        // 房屋信息
        houseInfoView = (DavinciView) this.findViewById(R.id.houseInfoView);
        houseInfoView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_05);
        houseInfoView.getTitleTextView().setText("房源基本信息");
        houseInfoView.getTipTextView().setText("");
        houseInfoView.setOnClickListener(this);

        // 房屋照片
        houseImageView = (DavinciView) this.findViewById(R.id.houseImageView);
        houseImageView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_06);
        houseImageView.getTitleTextView().setText("房屋照片");
        houseImageView.getTipTextView().setText("");
        houseImageView.setOnClickListener(this);

        // 配套设施
        equipmentView = (DavinciView) this.findViewById(R.id.equipmentView);
        equipmentView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_07);
        equipmentView.getTitleTextView().setText("配套设施");
        equipmentView.getTipTextView().setText("");
        equipmentView.setOnClickListener(this);

        // 交通信息
        trafficInfoView = (DavinciView) this.findViewById(R.id.trafficInfoView);
        trafficInfoView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_08);
        trafficInfoView.getTitleTextView().setText("交通信息");
        trafficInfoView.getTipTextView().setText("");
        trafficInfoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.houseInfoView: {
                Intent intent = new Intent(this, KeeperAddHouseDetailActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.houseImageView: {
                Intent intent = new Intent(this, KeeperAddHouseImageActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.equipmentView: {
                Intent intent = new Intent(this, KeeperAddHouseEquipmentActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.trafficInfoView: {
                Intent intent = new Intent(this, KeeperAddHouseTrafficActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;
        }
    }
}
