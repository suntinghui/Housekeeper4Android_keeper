package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.ares.house.dto.app.TrafficAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.EquipmentAppDtoEx;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 9/25/15.
 */
public class KeeperAddHouseTrafficActivity extends BaseActivity implements View.OnClickListener {

    private EditText busEditText = null;
    private EditText subwayEditText = null;

    private Button commitBtn = null;

    private HouseAddListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_traffic);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();

        requestTrafficInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("交通信息");

        busEditText = (EditText) this.findViewById(R.id.busEditText);
        subwayEditText = (EditText) this.findViewById(R.id.subwayEditText);

        commitBtn = (Button) this.findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);
    }

    private void requestTrafficInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_TRAFFIC, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, TrafficAppDto.class);
                    AppMessageDto<TrafficAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        try{
                            busEditText.setText(dto.getData().getBus());
                            busEditText.setSelection(busEditText.getText().length());
                        } catch (Exception e){}

                        try {
                            subwayEditText.setText(dto.getData().getSubway());
                            subwayEditText.setSelection(subwayEditText.getText().length());
                        } catch (Exception e) {}

                    } else {
                        Toast.makeText(KeeperAddHouseTrafficActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestSetTrafficInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");
        map.put("bus", busEditText.getText().toString().trim());
        map.put("subway", subwayEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_SETTRAFFIC, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Integer.class);
                    AppMessageDto<Integer> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperAddHouseTrafficActivity.this,"公交信息设置成功", Toast.LENGTH_SHORT).show();

                        KeeperAddHouseTrafficActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperAddHouseTrafficActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        if (TextUtils.isEmpty(busEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入公交信息", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.commitBtn:
                if (checkValue()) {
                    requestSetTrafficInfo();
                }
                break;
        }
    }
}
