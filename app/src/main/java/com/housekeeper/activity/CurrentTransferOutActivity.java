package com.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/2/15.
 * <p/>
 * 活期转出
 */
public class CurrentTransferOutActivity extends BaseActivity implements View.OnClickListener {

    private TextView balanceTextView = null;
    private EditText moneyEditText = null;
    private Button investmentBtn = null;

    private double balance = 0.00;

    private VerifyTransferPWDDialog verifyTransferPwdDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_transfer_out);

        initView();

        requestBalance();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("转出");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        balanceTextView = (TextView) this.findViewById(R.id.balanceTextView);
        moneyEditText = (EditText) this.findViewById(R.id.moneyEditText);

        investmentBtn = (Button) this.findViewById(R.id.investmentBtn);
        investmentBtn.setOnClickListener(this);
    }

    private void requestBalance() {
        JSONRequest request = new JSONRequest(this, RequestEnum.USER_HQ_MONEY, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        balanceTextView.setText(dto.getData());

                        balance = Double.parseDouble(dto.getData());

                    } else {
                        Toast.makeText(CurrentTransferOutActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据请稍候...");
    }

    private boolean checkValue() {
        String inputStr = moneyEditText.getText().toString().trim();
        if (StringUtils.isEmpty(inputStr)) {
            Toast.makeText(this, "请输入转出金额", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(inputStr) > balance) {
            Toast.makeText(this, "转出金额不能大于余额", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // 活期转出
    private void requestTransferOut(String pwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("money", moneyEditText.getText().toString().trim());
        map.put("password", pwd);

        JSONRequest request = new JSONRequest(this, RequestEnum.USER_DEBTPACKAGE_HQ_RANSOM, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        verifyTransferPwdDialog.dismiss();

                        Toast.makeText(CurrentTransferOutActivity.this, "转出成功，请到账户余额中查看", Toast.LENGTH_SHORT).show();

                        CurrentTransferOutActivity.this.finish();

                    } else {
                        verifyTransferPwdDialog.setError(dto.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据请稍候...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.investmentBtn: // 转出
                if (checkValue()) {
                    verifyTransferPwdDialog = new VerifyTransferPWDDialog(this);
                    verifyTransferPwdDialog.setTitle("提示");
                    verifyTransferPwdDialog.setTip("转出成功后资金将到达您的账户余额。");
                    verifyTransferPwdDialog.setOnConfirmListener(new VerifyTransferPWDDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm(String pwdStr) {
                            requestTransferOut(pwdStr);
                        }
                    });
                    verifyTransferPwdDialog.show();
                }
                break;
        }
    }
}
