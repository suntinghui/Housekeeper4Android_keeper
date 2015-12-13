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
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.PacificView;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 9/25/15.
 *
 * 房产证件
 */
public class KeeperAddHouseDeedActivity extends BaseActivity implements View.OnClickListener {

    private PacificView deedPacificView = null;
    private PacificView feePacificView = null;

    private String houseId = "";
    private boolean editable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_deed);

        houseId = this.getIntent().getStringExtra("houseId");
        editable = this.getIntent().getBooleanExtra("editable", true);

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        this.findViewById(R.id.doneTextView).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("房产证件");

        this.deedPacificView = (PacificView) this.findViewById(R.id.deedPacificView);
        this.deedPacificView.setTag(0x100);
        this.deedPacificView.setEditable(editable);
        this.deedPacificView.setPacificListener(deedListener);

        this.feePacificView = (PacificView) this.findViewById(R.id.feePacificView);
        this.feePacificView.setTag(0x101);
        this.feePacificView.setEditable(editable);
        this.feePacificView.setPacificListener(feeListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.deedPacificView.activityResult(requestCode, resultCode, data);
        this.feePacificView.activityResult(requestCode, resultCode, data);
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

    private PacificView.PacificListener deedListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", houseId);
            map.put("type", "REAL_ESTATE_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            deedPacificView.responseDownloadImageList(10, dto.getData());
                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", houseId);
            map.put("type", "REAL_ESTATE_IMG");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            deedPacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);
            map.put("type", "REAL_ESTATE_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_IMG_DELETE, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            deedPacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };

    private PacificView.PacificListener feeListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", houseId);
            map.put("type", "FEES_BILLS_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            feePacificView.responseDownloadImageList(10, dto.getData());
                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", houseId);
            map.put("type", "FEES_BILLS_IMG");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            feePacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);
            map.put("type", "FEES_BILLS_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseDeedActivity.this, RequestEnum.HOUSE_IMG_DELETE, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            feePacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperAddHouseDeedActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseDeedActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };
}
