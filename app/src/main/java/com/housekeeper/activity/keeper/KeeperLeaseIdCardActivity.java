package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImageAppDto;
import com.ares.house.dto.app.UserJoinAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.PacificView;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sth on 10/8/15.
 * <p/>
 * 租户信息 -- 身份证件
 */
public class KeeperLeaseIdCardActivity extends BaseActivity implements View.OnClickListener {

    private PacificView idCardFacadePacificView = null;
    private PacificView idCardObversePacificView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_idcard);

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        this.findViewById(R.id.doneTextView).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("身份证件");

        this.idCardFacadePacificView = (PacificView) this.findViewById(R.id.idCardFacadePacificView);
        this.idCardFacadePacificView.setTag(0x120);
        this.idCardFacadePacificView.setEditable(this.getIntent().getBooleanExtra("editable", true));
        this.idCardFacadePacificView.setPacificListener(facadeListener);

        this.idCardObversePacificView = (PacificView) this.findViewById(R.id.idCardObversePacificView);
        this.idCardObversePacificView.setTag(0x121);
        this.idCardObversePacificView.setEditable(this.getIntent().getBooleanExtra("editable", true));
        this.idCardObversePacificView.setPacificListener(obverseListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.idCardFacadePacificView.activityResult(requestCode, resultCode, data);
        this.idCardObversePacificView.activityResult(requestCode, resultCode, data);
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

    private PacificView.PacificListener facadeListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("leaseId", KeeperLeaseIdCardActivity.this.getIntent().getStringExtra("leaseId"));
            map.put("type", "1");

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        List<ImageAppDto> list = new ArrayList<ImageAppDto>();
                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            for (ImageAppDto tempDto : dto.getData()) {
                                if (tempDto.getName().contains("FACADE") && !StringUtils.isBlank(tempDto.getUrl())) {
                                    list.add(tempDto);
                                }
                            }

                            idCardFacadePacificView.responseDownloadImageList(1, list);
                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", KeeperLeaseIdCardActivity.this.getIntent().getStringExtra("houseId"));
            map.put("type", "ID_CARD_FACADE");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImageAppDto.class);
                        AppMessageDto<ImageAppDto> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                            idCardFacadePacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_DELETE_IMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            idCardFacadePacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };

    private PacificView.PacificListener obverseListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("leaseId", KeeperLeaseIdCardActivity.this.getIntent().getStringExtra("leaseId"));
            map.put("type", "1");

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        List<ImageAppDto> list = new ArrayList<ImageAppDto>();
                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            for (ImageAppDto tempDto : dto.getData()) {
                                if (tempDto.getName().contains("OBVERSE") && !StringUtils.isBlank(tempDto.getUrl())) {
                                    list.add(tempDto);
                                }
                            }

                            idCardObversePacificView.responseDownloadImageList(1, list);
                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", KeeperLeaseIdCardActivity.this.getIntent().getStringExtra("houseId"));
            map.put("type", "ID_CARD_OBVERSE");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ImageAppDto.class);
                        AppMessageDto<ImageAppDto> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                            idCardObversePacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);

            JSONRequest request = new JSONRequest(KeeperLeaseIdCardActivity.this, RequestEnum.LEASE_DELETE_IMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            idCardObversePacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperLeaseIdCardActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperLeaseIdCardActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };
}
