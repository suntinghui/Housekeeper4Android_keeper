package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.TenantryInfoStatusAppDto;
import com.ares.house.dto.app.UserJoinAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * Created by sth on 10/8/15.
 * <p>
 * <p>
 * 租户信息
 */
public class KeeperLeaseInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView idCardStatusTextView = null;
    private TextView addressStatusTextView = null;

    private WaitLeaseListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_info);

        infoDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestStatus();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("身份证件");

        this.findViewById(R.id.idCardLayout).setOnClickListener(this);
        this.findViewById(R.id.addressLayout).setOnClickListener(this);

        this.idCardStatusTextView = (TextView) this.findViewById(R.id.idCardStatusTextView);
        this.addressStatusTextView = (TextView) this.findViewById(R.id.addressStatusTextView);
    }

    private void requestStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getHouseId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_TENANTRY_INFO_STATUS, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, TenantryInfoStatusAppDto.class);
                    AppMessageDto<TenantryInfoStatusAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        idCardStatusTextView.setText(dto.getData().isIdcard() ? "已完成" : "未完成");

                        addressStatusTextView.setText(dto.getData().isWorkAddress() ? "已完成" : "未完成");

                    } else {
                        Toast.makeText(KeeperLeaseInfoActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
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
