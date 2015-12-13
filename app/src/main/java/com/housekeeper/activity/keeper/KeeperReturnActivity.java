package com.housekeeper.activity.keeper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.LeasedListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.SublimePickerDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.DateUtil;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * Created by sth on 10/31/15.
 */
public class KeeperReturnActivity extends BaseActivity implements View.OnClickListener {

    private EditText returnTimeTextView = null;
    private EditText returnMoneyTextView = null;
    private EditText pwdTextView = null;
    private Button commitBtn = null;

    private LeasedListAppDto appDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_return);

        this.appDto = (LeasedListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("退租");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        this.returnTimeTextView = (EditText) this.findViewById(R.id.returnTimeTextView);
        this.returnTimeTextView.setOnClickListener(this);
        this.returnTimeTextView.setText(DateUtil.getCurrentDate2());

        this.returnMoneyTextView = (EditText) this.findViewById(R.id.returnMoneyTextView);
        this.pwdTextView = (EditText) this.findViewById(R.id.pwdTextView);

        this.commitBtn = (Button) this.findViewById(R.id.commitBtn);
        this.commitBtn.setOnClickListener(this);
    }

    private boolean checkValue() {
        if (StringUtils.isBlank(returnMoneyTextView.getText().toString().trim())) {
            Toast.makeText(this, "请输入退款金额", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwdTextView.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void requestReturn() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("leaseId", this.appDto.getLeaseId() + "");
        tempMap.put("withdrawTime", String.valueOf(DateUtil.string2MilliSec(returnTimeTextView.getText().toString())));
        tempMap.put("mortgageMoney", returnMoneyTextView.getText().toString().trim());
        tempMap.put("password", pwdTextView.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_WITHDRAW, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperReturnActivity.this, "退租成功", Toast.LENGTH_SHORT).show();

                        KeeperReturnActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperReturnActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.returnTimeTextView: {
                SublimeOptions options = new SublimeOptions();
                options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);

                final SublimePickerDialog dialog = new SublimePickerDialog(this, options);
                dialog.setCallback(new SublimePickerDialog.Callback() {
                    @Override
                    public void onCancelled() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onDateTimeRecurrenceSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                        returnTimeTextView.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                });
                dialog.show();
            }
            break;

            case R.id.commitBtn: {
                if (checkValue()) {
                    requestReturn();
                }
            }
            break;
        }
    }
}
