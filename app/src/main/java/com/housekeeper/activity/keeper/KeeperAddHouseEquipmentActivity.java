package com.housekeeper.activity.keeper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.EquipmentAdapter;
import com.housekeeper.activity.view.EquipmentEditAdapter;
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
 * Created by sth on 9/17/15.
 * <p>
 *
 * 配套设施
 */
public class KeeperAddHouseEquipmentActivity extends BaseActivity implements View.OnClickListener {

    private TextView checkAllTextView;
    private AsymmetricGridView gridView = null;
    private Button commitBtn = null;

    private HouseAddListAppDto infoDto = null;
    private EquipmentEditAdapter adapter = null;

    private List<EquipmentAppDtoEx> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_equipment);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();

        requestEquipmentList();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("配套设施");
        checkAllTextView = (TextView) this.findViewById(R.id.checkAllTextView);
        checkAllTextView.setOnClickListener(this);
        checkAllTextView.setText("");

        commitBtn = (Button) this.findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);

        gridView = (AsymmetricGridView) this.findViewById(R.id.gridView);

        gridView.setRequestedColumnCount(3);
        gridView.setRowHeight(60);
        gridView.determineColumns();
        gridView.setAllowReordering(true);
        gridView.isAllowReordering(); // true

        adapter = new EquipmentEditAdapter(this);
        AsymmetricGridViewAdapter asymmetricAdapter = new AsymmetricGridViewAdapter<>(this, gridView, adapter);
        gridView.setAdapter(asymmetricAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try{
                    adapter.setCheckItem(items.get(position).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestEquipmentList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_EQUIPMENT, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, EquipmentAppDtoEx.class);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                    AppMessageDto<List<EquipmentAppDtoEx>> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        items = dto.getData();

                        responseEquipmentList();

                    } else {
                        Toast.makeText(KeeperAddHouseEquipmentActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseEquipmentList() {
        adapter.setData(items, true);

        if (adapter.hasCheckAll()) {
            checkAllTextView.setText("取消全选");
        } else {
            checkAllTextView.setText("全选");
        }

        commitBtn.setVisibility(View.VISIBLE);
    }

    private void requestCommit() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");
        map.put("ids", adapter.getCheckIds());

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_SETEQUIPMENT, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperAddHouseEquipmentActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        KeeperAddHouseEquipmentActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperAddHouseEquipmentActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
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
        try{
            switch (view.getId()) {
                case R.id.backBtn:
                    this.finish();
                    break;

                case R.id.commitBtn:
                    requestCommit();
                    break;

                case R.id.checkAllTextView:
                    if (checkAllTextView.getText().toString().contains("取消")) {
                        adapter.setCheckAllItem(false);

                        checkAllTextView.setText("全选");

                    } else {
                        adapter.setCheckAllItem(true);

                        checkAllTextView.setText("取消全选");
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
