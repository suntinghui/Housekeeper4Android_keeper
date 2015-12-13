package com.housekeeper.activity.keeper;

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
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sth on 10/8/15.
 * <p/>
 * 工作地点
 */
public class KeeperLeaseAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText addressEditText = null;

    private Button commitBtn = null;

    private WaitLeaseListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_address);

        infoDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();

        requestAddressInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("工作地点");

        addressEditText = (EditText) this.findViewById(R.id.addressEditText);

        commitBtn = (Button) this.findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);
    }

    private void requestAddressInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("leaseId", infoDto.getLeaseId() + "");
        map.put("type", "WORK_ADDRESS");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_GETPROPERTY, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        addressEditText.setText(dto.getData());

                        addressEditText.setSelection(addressEditText.getText().toString().trim().length());

                    } else {
                        Toast.makeText(KeeperLeaseAddressActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestSetAddressInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getHouseId() + "");
        map.put("type", "WORK_ADDRESS");
        map.put("value", addressEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_SETPROPERTY, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperLeaseAddressActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        KeeperLeaseAddressActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperLeaseAddressActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        if (TextUtils.isEmpty(addressEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入租户工作地点", Toast.LENGTH_SHORT).show();
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
                    requestSetAddressInfo();
                }
                break;
        }
    }
}
