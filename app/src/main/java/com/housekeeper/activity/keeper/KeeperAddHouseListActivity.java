package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.AdapterUtil;
import com.housekeeper.utils.DateUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sth on 9/24/15.
 * <p>
 * 添加房源
 */
public class KeeperAddHouseListActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView houseInfoView = null; // 房屋信息
    private DavinciView houseDeedView = null; // 房产证件
    private DavinciView agencyContractView = null; // 代理租赁合同
    private DavinciView rentalCostView = null; // 房屋租赁费用

    // 详情
    private LinearLayout infoLayout = null;
    private TextView begingDateTextView = null;
    private TextView endDateTextView = null;
    private TextView letLeaseDayTextView = null;
    private TextView yearMoneyTextView = null;
    private TextView heatingFeesMoneyTextView = null;
    private TextView heatingFeesTextView = null;
    private TextView propertyFeesMoneyTextView = null;
    private TextView propertyFeesTextView = null;
    private LinearLayout dateLayout = null;
    private TextView violateMonthTextView = null;

    private TextView deleteHouseTextView = null;

    private VerifyTransferPWDDialog verifyTransferPwdDialog = null;

    private HouseAddListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_list);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestHouseInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("房源信息");

        // 房屋信息
        houseInfoView = (DavinciView) this.findViewById(R.id.houseInfoView);
        houseInfoView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_01);
        houseInfoView.getTitleTextView().setText("房屋信息");
        houseInfoView.getTipTextView().setText("");
        houseInfoView.setOnClickListener(this);

        // 房产证件
        houseDeedView = (DavinciView) this.findViewById(R.id.houseDeedView);
        houseDeedView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_02);
        houseDeedView.getTitleTextView().setText("房产证件");
        houseDeedView.getTipTextView().setText("");
        houseDeedView.setOnClickListener(this);

        // 代理租赁合同
        agencyContractView = (DavinciView) this.findViewById(R.id.agencyContractView);
        agencyContractView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_03);
        agencyContractView.getTitleTextView().setText("代理租赁合同");
        agencyContractView.getTipTextView().setText("");
        agencyContractView.setOnClickListener(this);

        // 房屋租赁费用
        rentalCostView = (DavinciView) this.findViewById(R.id.rentalCostView);
        rentalCostView.getLogoImageView().setBackgroundResource(R.drawable.keeper_house_add_04);
        rentalCostView.getTitleTextView().setText("房屋租赁费用");
        rentalCostView.getTipTextView().setText("");
        rentalCostView.setOnClickListener(this);

        deleteHouseTextView = (TextView) this.findViewById(R.id.deleteHouseTextView);
        deleteHouseTextView.setOnClickListener(this);

        this.infoLayout = (LinearLayout) this.findViewById(R.id.infoLayout);

        this.begingDateTextView = (TextView) this.findViewById(R.id.begingDateTextView);
        this.endDateTextView = (TextView) this.findViewById(R.id.endDateTextView);
        this.letLeaseDayTextView = (TextView) this.findViewById(R.id.letLeaseDayTextView);
        this.yearMoneyTextView = (TextView) this.findViewById(R.id.yearMoneyTextView);
        this.heatingFeesMoneyTextView = (TextView) this.findViewById(R.id.heatingFeesMoneyTextView);
        this.heatingFeesTextView = (TextView) this.findViewById(R.id.heatingFeesTextView);
        this.propertyFeesMoneyTextView = (TextView) this.findViewById(R.id.propertyFeesMoneyTextView);
        this.propertyFeesTextView = (TextView) this.findViewById(R.id.propertyFeesTextView);
        this.dateLayout = (LinearLayout) this.findViewById(R.id.dateLayout);
        this.violateMonthTextView = (TextView) this.findViewById(R.id.violateMonthTextView);

    }

    private void requestHouseInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_ADDINFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, HouseAddListAppDto.class);
                    AppMessageDto<HouseAddListAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        infoDto = dto.getData();

                        responseHouseInfo();

                    } else {
                        Toast.makeText(KeeperAddHouseListActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseHouseInfo() {
        // 房屋信息
        if (infoDto.isInfoComplete()) {
            houseInfoView.getTipTextView().setText("完成");
            houseInfoView.getTipTextView().setTextColor(getResources().getColor(R.color.blueme));
        } else {
            houseInfoView.getTipTextView().setText("未完成");
            houseInfoView.getTipTextView().setTextColor(getResources().getColor(R.color.gray_1));
        }

        // 房产证件
        if (infoDto.isEstateComplete()) {
            houseDeedView.getTipTextView().setText("完成");
            houseDeedView.getTipTextView().setTextColor(getResources().getColor(R.color.blueme));
        } else {
            houseDeedView.getTipTextView().setText("未完成");
            houseDeedView.getTipTextView().setTextColor(getResources().getColor(R.color.gray_1));
        }

        // 代理合同
        if (infoDto.isAgentComplete()) {
            agencyContractView.getTipTextView().setText("完成");
            agencyContractView.getTipTextView().setTextColor(getResources().getColor(R.color.blueme));
        } else {
            agencyContractView.getTipTextView().setText("未完成");
            agencyContractView.getTipTextView().setTextColor(getResources().getColor(R.color.gray_1));
        }

        // 租赁费用
        if (infoDto.isRentalComplete()) {
            rentalCostView.getTipTextView().setText("完成");
            rentalCostView.getTipTextView().setTextColor(getResources().getColor(R.color.blueme));

            infoLayout.setVisibility(View.VISIBLE);
        } else {
            rentalCostView.getTipTextView().setText("未完成");
            rentalCostView.getTipTextView().setTextColor(getResources().getColor(R.color.gray_1));

            infoLayout.setVisibility(View.GONE);
        }

        // infoLayout
        if (infoDto.isRentalComplete()) {
            this.begingDateTextView.setText(infoDto.getBeginTimeStr());
            this.endDateTextView.setText(infoDto.getEndTimeStr());
            this.letLeaseDayTextView.setText(infoDto.getLetLeaseDay() + " 天");
            this.yearMoneyTextView.setText(infoDto.getYearMoney() + " 元");
            this.heatingFeesMoneyTextView.setText(infoDto.getHeatingFeesMoney() + " 元");
            this.heatingFeesTextView.setText(infoDto.isHeatingFees() ? "（租户承担）" : "（房东承担）");
            this.propertyFeesMoneyTextView.setText(infoDto.getPropertyFeesMoney() + " 元");
            this.propertyFeesTextView.setText(infoDto.isPropertyFees() ? "（租户承担）" : "（房东承担）");
            this.violateMonthTextView.setText("扣除" + infoDto.getViolateMonth() + "个月租金");

            dateLayout.removeAllViews();

            ArrayList<String> list = DateUtil.getYaoYAO(infoDto.getBeginTimeStr(), infoDto.getYearCount(), infoDto.getLetLeaseDay());
            for (String str : list) {
                TextView textView = new TextView(this);
                textView.setText(str);
                textView.setTextSize(13);
                textView.setTextColor(Color.parseColor("#666666"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, AdapterUtil.dip2px(this, 10), 0, 0);
                dateLayout.addView(textView, params);
            }
        }
    }

    private void deleteHouse() {
        verifyTransferPwdDialog = new VerifyTransferPWDDialog(this);
        verifyTransferPwdDialog.setTitle("确定要删除该房源吗？");
        verifyTransferPwdDialog.setTip("一旦删除不可恢复，请慎重操作。");
        verifyTransferPwdDialog.setOnConfirmListener(new VerifyTransferPWDDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String pwdStr) {
                requestDeleteHouse(pwdStr);
            }
        });
        verifyTransferPwdDialog.show();
    }

    private void requestDeleteHouse(String pwdStr) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");
        map.put("password", pwdStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_DELETE, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperAddHouseListActivity.this, "房源已删除", Toast.LENGTH_SHORT).show();

                        verifyTransferPwdDialog.dismiss();

                        KeeperAddHouseListActivity.this.finish();

                    } else {
                        verifyTransferPwdDialog.setError(dto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.houseInfoView: {
                Intent intent = new Intent(this, KeeperAddHouseInfoActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.houseDeedView: {// 房产证件
                Intent intent = new Intent(this, KeeperAddHouseDeedActivity.class);
                intent.putExtra("houseId", infoDto.getId() + "");
                this.startActivity(intent);
            }
            break;

            case R.id.agencyContractView: {// 代理租赁合同
                Intent intent = new Intent(this, KeeperAddHouseAgencyContractActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.rentalCostView: {// 房屋租赁费用
                Intent intent = new Intent(this, KeeperAddHouseRentalCostActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.deleteHouseTextView:
                this.deleteHouse();
                break;
        }
    }
}
