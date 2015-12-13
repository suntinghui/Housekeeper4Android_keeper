package com.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.TreeNodeAppDto;
import com.housekeeper.activity.view.SelectCityLayout;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.BankEntityEx;
import com.housekeeper.utils.BankUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;


/**
 * Created by sth on 8/16/15.
 */
public class SetBranchActivity extends BaseActivity implements View.OnClickListener {

    private ImageView bankLogoImageView;
    private TextView bankNameTextView;
    private TextView tailNumTextView;

    private EditText branchNameEditText;

    private SelectCityLayout selectCityLayout = null;

    private Button applyBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_branch);

        initView();

        requestBankArea();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("开户行信息");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        applyBtn = (Button) this.findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);

        bankLogoImageView = (ImageView) this.findViewById(R.id.bankLogoImageView);
        bankNameTextView = (TextView) this.findViewById(R.id.bankNameTextView);
        tailNumTextView = (TextView) this.findViewById(R.id.tailNumTextView);

        // 银行卡信息
        HashMap<String, String> bankInfoMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("BANK_INFO");

        BankEntityEx bank = BankUtil.getBankFromCode(bankInfoMap.get("BANK_ID"), this);
        bankLogoImageView.setBackgroundResource(bank.getLogoId());
        bankNameTextView.setText(bank.getName());
        String bankCard = bankInfoMap.get("BANK_CARD");
        tailNumTextView.setText("(尾号：" + bankCard.substring(bankCard.length() - 4) + ")");

        branchNameEditText = (EditText) this.findViewById(R.id.branchNameEditText);
        branchNameEditText.setText(bankInfoMap.get("BANK_ADDRESS"));

        selectCityLayout = (SelectCityLayout) this.findViewById(R.id.selectCity);
    }

    private void requestBankArea() {
        JSONRequest request = new JSONRequest(this, RequestEnum.BANK_AREA, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, TreeNodeAppDto.class);
                    AppMessageDto<TreeNodeAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        selectCityLayout.setData(dto.getData());

                        HashMap<String, String> bankInfoMap = (HashMap<String, String>) getIntent().getSerializableExtra("BANK_INFO");
                        selectCityLayout.setSelectedInfo(bankInfoMap.get("BANK_AREA_ID_PROVINCE"), bankInfoMap.get("BANK_AREA_ID_CITY"));

                    } else {
                        Toast.makeText(SetBranchActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void requestSetBranch() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("bankAreaId", selectCityLayout.getSelectedAreaId() + "");
        map.put("address", branchNameEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_UPDATE_ADDRESS, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(SetBranchActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();

                    } else {
                        Toast.makeText(SetBranchActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        if (branchNameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入开户行名称", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.applyBtn:
                if (checkValue()) {
                    requestSetBranch();
                }
                break;

            case R.id.backBtn:
                this.finish();
                break;
        }
    }
}
