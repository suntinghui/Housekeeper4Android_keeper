package com.housekeeper.activity.tenant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.BindedBankActivity;
import com.housekeeper.activity.BindingBankActivity;
import com.housekeeper.activity.WithdrawalRecordsActivity;
import com.housekeeper.activity.view.DavinciView;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sth on 12/11/15.
 */
public class TenantCardSettingActivity extends BaseActivity implements View.OnClickListener {

    private DavinciView bankCardView;
    private DavinciView withdrawRecordView;

    private HashMap<String, String> bankInfoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_card_setting);

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.requestBankInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("设置");

        bankCardView = (DavinciView) this.findViewById(R.id.bankCardView);
        bankCardView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_02);
        bankCardView.getTitleTextView().setText("银行卡");
        bankCardView.getTipTextView().setText("未绑定");
        bankCardView.setOnClickListener(this);

        withdrawRecordView = (DavinciView) this.findViewById(R.id.withdrawRecordView);
        withdrawRecordView.getLogoImageView().setBackgroundResource(R.drawable.landlord_setting_09);
        withdrawRecordView.getTitleTextView().setText("提现记录");
        withdrawRecordView.getTipTextView().setText("");
        withdrawRecordView.setOnClickListener(this);
    }

    private boolean gotoBankCard() {
        if (bankInfoMap == null) {
            this.requestBankInfo();
            return false;
        }

        String bank_id = bankInfoMap.get("BANK_ID");
        if (null == bank_id || TextUtils.isEmpty(bank_id) || TextUtils.equals(bank_id, "null")) {
            Intent intent = new Intent(TenantCardSettingActivity.this, BindingBankActivity.class);
            intent.putExtra("MAP", bankInfoMap);
            TenantCardSettingActivity.this.startActivityForResult(intent, 0);

        } else {
            Intent intent = new Intent(TenantCardSettingActivity.this, BindedBankActivity.class);
            intent.putExtra("MAP", bankInfoMap);
            TenantCardSettingActivity.this.startActivityForResult(intent, 0);
        }

        return true;
    }

    private void requestBankInfo() {
        JSONRequest request = new JSONRequest(this, RequestEnum.SECURITY_CENTER_BANK_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        bankInfoMap = (HashMap<String, String>) dto.getData();

                        responseBankInfo();

                    } else {
                        Toast.makeText(TenantCardSettingActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseBankInfo() {
        // 绑定银行卡状态 a未绑定 c绑定失败 d确认中 e已绑定
        String status = "";

        char c = bankInfoMap.get("BANK_CARD_STATUS").charAt(0);
        switch (c) {
            case 'a':
                status = "未绑定";
                break;

            case 'c':
                status = "绑定失败";
                break;

            case 'd':
                status = "确认中";
                break;

            case 'e':
                status = "已绑定";
                break;
        }
        bankCardView.getTipTextView().setText(status);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.bankCardView:
                if (!this.gotoBankCard()) {
                    Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.withdrawRecordView: {
                Intent intent = new Intent(this, WithdrawalRecordsActivity.class);
                this.startActivity(intent);
            }
            break;
        }
    }
}
