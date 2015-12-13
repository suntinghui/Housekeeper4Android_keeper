package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImageAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.PacificView;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/8/15.
 * <p/>
 * 租户信息
 */
public class KeeperLeaseContractActivity extends BaseActivity implements View.OnClickListener {

    private PacificView contractPacificView = null;

    private WaitLeaseListAppDto infoDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_contract);

        infoDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        this.findViewById(R.id.doneTextView).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("租房合同");

        this.contractPacificView = (PacificView) this.findViewById(R.id.contractPacificView);
        this.contractPacificView.setTag(0x130);
        this.contractPacificView.setPacificListener(contractListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.contractPacificView.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
            case R.id.doneTextView:
                this.finish();
                break;
        }
    }

    private PacificView.PacificListener contractListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("leaseId", infoDto.getLeaseId() + "");
            map.put("type", "2");

            JSONRequest request = new JSONRequest(KeeperLeaseContractActivity.this, RequestEnum.LEASE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                            contractPacificView.responseDownloadImageList(10, dto.getData());

                        } else {
                            Toast.makeText(KeeperLeaseContractActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseContractActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", infoDto.getHouseId() + "");
            map.put("type", "CONTRACT");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperLeaseContractActivity.this, RequestEnum.LEASE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImageAppDto.class);
                        AppMessageDto<ImageAppDto> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                            contractPacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperLeaseContractActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseContractActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);

            JSONRequest request = new JSONRequest(KeeperLeaseContractActivity.this, RequestEnum.LEASE_DELETE_IMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                            contractPacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperLeaseContractActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseContractActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };
}
