package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.ares.house.dto.app.ImageAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.DiaoyuView;
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
 * <p/>
 * 房屋照片
 */
public class KeeperAddHouseImageActivity extends BaseActivity implements View.OnClickListener {

    private PacificView homePacificView = null;
    private PacificView otherPacificView = null;

    private HouseAddListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_image);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        this.findViewById(R.id.doneTextView).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("房屋照片");

        this.homePacificView = (PacificView) this.findViewById(R.id.homePacificView);
        this.homePacificView.setTag(0x110);
        this.homePacificView.setPacificListener(homeListener);

        this.otherPacificView = (PacificView) this.findViewById(R.id.otherPacificView);
        this.otherPacificView.setTag(0x111);
        this.otherPacificView.setPacificListener(otherListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.homePacificView.activityResult(requestCode, resultCode, data);
        this.otherPacificView.activityResult(requestCode, resultCode, data);
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

    private PacificView.PacificListener homeListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", infoDto.getId() + "");

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            homePacificView.responseDownloadImageList(1, dto.getData());
                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");

        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", infoDto.getId() + "");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            homePacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_IMG_DELETE, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            KeeperAddHouseImageActivity.this.homePacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };

    private PacificView.PacificListener otherListener = new PacificView.PacificListener() {
        @Override
        public void downloadImageList() {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", infoDto.getId() + "");
            map.put("type", "HOUSE_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_GETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            otherPacificView.responseDownloadImageList(10, dto.getData());
                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void uploadImage(String imageBase64) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("houseId", infoDto.getId() + "");
            map.put("type", "HOUSE_IMG");
            map.put("data", imageBase64);

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_SETIMG, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                        AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            otherPacificView.responseUploadImage();

                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");
        }

        @Override
        public void deleteImage(String imageId) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", imageId);
            map.put("type", "HOUSE_IMG");

            JSONRequest request = new JSONRequest(KeeperAddHouseImageActivity.this, RequestEnum.HOUSE_IMG_DELETE, map, new Response.Listener<String>() {

                @Override
                public void onResponse(String jsonObject) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                        AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                        if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                            KeeperAddHouseImageActivity.this.otherPacificView.responseDeleteImage();

                        } else {
                            Toast.makeText(KeeperAddHouseImageActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            KeeperAddHouseImageActivity.this.addToRequestQueue(request, "正在提交数据...");
        }
    };
}
