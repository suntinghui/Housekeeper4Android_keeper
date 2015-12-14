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
import com.ares.house.dto.app.HouseInfoAppDto;
import com.ares.house.dto.app.TreeNodeAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.IdentityActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.angmarch.views.NiceSpinner;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sth on 9/25/15.
 * <p>
 * 房屋信息
 */
public class KeeperAddHouseDetailActivity extends BaseActivity implements View.OnClickListener {

    private final static List<String> DECORATE_LIST = Arrays.asList("毛坏房", "简单装修", "精品装修", "豪华装修");
    private final static List<String> ORIENTATION_LIST = Arrays.asList("东", "南", "西", "北", "东南", "西南", "东北", "西北");
    private final static List<String> LEASETYPE_LIST = Arrays.asList("整租", "合租", "单间", "床位");

    private TextView areaTextView = null; // 地区
    private EditText communityEditText = null; // 小区名称
    private EditText addressEditText = null; // 街道位置
    private EditText houseNumEditText = null; // 楼号/单元/⻔门牌
    private EditText houseTypeEditText = null; // 房型
    private EditText areaSizeEditText = null; // ⾯面积
    private EditText floorEditText = null; // 楼层
    private NiceSpinner decorateTextView = null; // 装修
    private NiceSpinner orientationTextView = null; // 朝向
    private NiceSpinner leaseTypeTextView = null; // 住宅类型

    private Button commitBtn = null;

    private TreeNodeAppDto cityDTO = null;
    private String areaId = "";

    private HouseAddListAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_detail);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();

        requestHouseInfo();

        requestCityArea();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("房源基本信息");

        areaTextView = (TextView) this.findViewById(R.id.areaTextView);
        areaTextView.setOnClickListener(this);

        communityEditText = (EditText) this.findViewById(R.id.communityEditText);
        addressEditText = (EditText) this.findViewById(R.id.addressEditText);
        houseNumEditText = (EditText) this.findViewById(R.id.houseNumEditText);
        houseTypeEditText = (EditText) this.findViewById(R.id.houseTypeEditText);
        areaSizeEditText = (EditText) this.findViewById(R.id.areaSizeEditText);
        floorEditText = (EditText) this.findViewById(R.id.floorEditText);

        decorateTextView = (NiceSpinner) this.findViewById(R.id.decorateTextView);
        List<String> decorateDataset = new LinkedList<>(DECORATE_LIST);
        decorateTextView.attachDataSource(decorateDataset);

        orientationTextView = (NiceSpinner) this.findViewById(R.id.orientationTextView);
        List<String> orientationDataset = new LinkedList<>(ORIENTATION_LIST);
        orientationTextView.attachDataSource(orientationDataset);

        leaseTypeTextView = (NiceSpinner) this.findViewById(R.id.leaseTypeTextView);
        List<String> houseTypeDataset = new LinkedList<>(LEASETYPE_LIST);
        leaseTypeTextView.attachDataSource(houseTypeDataset);

        commitBtn = (Button) this.findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);
    }

    private void requestCityArea() {
        JSONRequest request = new JSONRequest(this, RequestEnum.CITYAREA, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, TreeNodeAppDto.class);
                    AppMessageDto<TreeNodeAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        cityDTO = dto.getData();

                    } else {
                        Toast.makeText(KeeperAddHouseDetailActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestHouseInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_INFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, HouseInfoAppDto.class);
                    AppMessageDto<HouseInfoAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        responseHouseInfo(dto.getData());

                    } else {
                        Toast.makeText(KeeperAddHouseDetailActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseHouseInfo(HouseInfoAppDto dto) {
        areaId = (dto.getAreaId() == 0 ? "" : dto.getAreaId() + "");
        areaTextView.setText(dto.getProvinceStr() == null ? "" : (dto.getProvinceStr() + " " + dto.getCityStr() + " " + dto.getAreaStr()));

        try {
            communityEditText.setText(dto.getCommunity());
            communityEditText.setSelection(communityEditText.getText().toString().trim().length());
        } catch (Exception e) {
        }

        addressEditText.setText(dto.getAddress());
        houseNumEditText.setText(dto.getHouseNum());
        houseTypeEditText.setText(dto.getHouseType());
        areaSizeEditText.setText(dto.getAreaSize() == 0 ? "" : (dto.getAreaSize() + ""));
        floorEditText.setText(dto.getFloor());

        try {
            decorateTextView.setSelectedIndex(DECORATE_LIST.indexOf(dto.getDecorate()));
        } catch (Exception e) {
            decorateTextView.setSelectedIndex(0);
        }

        try {
            orientationTextView.setSelectedIndex(ORIENTATION_LIST.indexOf(dto.getOrientation()));
        } catch (Exception e) {
            orientationTextView.setSelectedIndex(0);
        }

        try {
            leaseTypeTextView.setSelectedIndex(LEASETYPE_LIST.indexOf(dto.getType()));
        } catch (Exception e) {
            leaseTypeTextView.setSelectedIndex(0);
        }

    }

    private void requestSetHouseInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");
        map.put("areaId", areaId);
        map.put("community", communityEditText.getText().toString().trim());
        map.put("address", addressEditText.getText().toString().trim());
        map.put("houseNum", houseNumEditText.getText().toString().trim());
        map.put("houseType", houseTypeEditText.getText().toString().trim());
        map.put("areaSize", areaSizeEditText.getText().toString().trim());
        map.put("floor", floorEditText.getText().toString().trim());
        map.put("decorate", DECORATE_LIST.get(decorateTextView.getSelectedIndex()));
        map.put("orientation", ORIENTATION_LIST.get(orientationTextView.getSelectedIndex()));
        map.put("leaseType", LEASETYPE_LIST.get(leaseTypeTextView.getSelectedIndex()));

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_SETINFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Integer.class);
                    AppMessageDto<Integer> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperAddHouseDetailActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        KeeperAddHouseDetailActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperAddHouseDetailActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        if (areaId.equals("")) {
            Toast.makeText(this, "请选择地区", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(communityEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入小区名称", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(addressEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入街道位置", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(houseNumEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入楼号/单元/门牌号", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(houseTypeEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入户型", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(areaSizeEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入面积", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(floorEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入楼层", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {// 城市
            try {
                areaId = data.getStringExtra("identityCode");
                String identityValue = data.getStringExtra("identityValue");
                areaTextView.setText(identityValue);

            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.areaTextView: {
                if (cityDTO == null) {
                    requestCityArea();
                    Toast.makeText(this, "正在下载城市数据...", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.showProgress("正在加载...");

                Intent intent = new Intent(this, IdentityActivity.class);
                intent.putExtra("dto", cityDTO);
                intent.putExtra("code", areaId);
                startActivityForResult(intent, 100);
            }
            break;

            case R.id.commitBtn:
                if (checkValue()) {
                    requestSetHouseInfo();
                }
                break;
        }
    }
}
