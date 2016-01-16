package com.housekeeper.activity.keeper;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.ares.house.dto.app.UserJoinAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.SublimePickerDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.EquipmentAppDtoEx;
import com.housekeeper.utils.DateUtil;
import com.housekeeper.utils.StringUtil;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/8/15.
 * <p>
 * 租金详情
 */
public class KeeperLeaseFeeActivity extends BaseActivity implements View.OnClickListener {

    private EditText beginTimeTextView;
    private EditText endTimeTextView;
    private TextView landlordEndTimeTextView;
    private EditText monthMoneyEditText;
    private TextView minMonthMoneyTextView;
    private EditText agencyFeeEditText;
    private EditText mortgageMoneyEditText;
    private Button commitBtn;

    private UserJoinAppDto appDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_fee);

        this.appDto = (UserJoinAppDto) this.getIntent().getSerializableExtra("DTO");

        initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("租金详情");

        this.beginTimeTextView = (EditText) this.findViewById(R.id.beginTimeTextView);
        this.beginTimeTextView.setOnClickListener(this);
        if (StringUtils.isBlank(appDto.getBeginTimeStr())) {
            this.beginTimeTextView.setText("");
        } else {
            this.beginTimeTextView.setText(appDto.getBeginTimeStr());
        }

        this.endTimeTextView = (EditText) this.findViewById(R.id.endTimeTextView);
        this.endTimeTextView.setOnClickListener(this);
        if (StringUtils.isBlank(appDto.getEndTimeStr())) {
            this.endTimeTextView.setText("");
        } else {
            this.endTimeTextView.setText(appDto.getEndTimeStr());
        }

        this.landlordEndTimeTextView = (TextView) this.findViewById(R.id.landlordEndTimeTextView);
        if (!StringUtils.isBlank(appDto.getLandlordEndTimeStr())) {
            this.landlordEndTimeTextView.setText(appDto.getLandlordEndTimeStr());
        }

        this.monthMoneyEditText = (EditText) this.findViewById(R.id.monthMoneyEditText);

        this.minMonthMoneyTextView = (TextView) this.findViewById(R.id.minMonthMoneyTextView);
        this.minMonthMoneyTextView.setText(this.appDto.getMinMonthMoney());

        this.agencyFeeEditText = (EditText) this.findViewById(R.id.agencyFeeEditText);
        this.agencyFeeEditText.setText("0.00");

        this.mortgageMoneyEditText = (EditText) this.findViewById(R.id.mortgageMoneyEditText);

        this.commitBtn = (Button) this.findViewById(R.id.commitBtn);
        this.commitBtn.setOnClickListener(this);
    }

    private void requestInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", appDto.getHouseId() + "");
        map.put("beginTime", DateUtil.string2MilliSec(beginTimeTextView.getText().toString().trim()) + "");
        map.put("endTime", DateUtil.string2MilliSec(endTimeTextView.getText().toString().trim()) + "");
        map.put("monthMoney", monthMoneyEditText.getText().toString().trim());
        map.put("agencyFee", agencyFeeEditText.getText().toString().trim());
        map.put("mortgageMoney", mortgageMoneyEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_SETRENT, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperLeaseFeeActivity.this, "设置完成", Toast.LENGTH_SHORT).show();

                        KeeperLeaseFeeActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperLeaseFeeActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private boolean checkValue() {
        try {
            if (TextUtils.isEmpty(monthMoneyEditText.getText().toString().trim())) {
                Toast.makeText(this, "请设置月租金", Toast.LENGTH_SHORT).show();
                return false;

            } else if (TextUtils.isEmpty(agencyFeeEditText.getText().toString().trim())) {
                Toast.makeText(this, "请设置中介费", Toast.LENGTH_SHORT).show();
                return false;

            } else if (TextUtils.isEmpty(mortgageMoneyEditText.getText().toString().trim())) {
                Toast.makeText(this, "请设置押金", Toast.LENGTH_SHORT).show();
                return false;

            } else if (Double.parseDouble(mortgageMoneyEditText.getText().toString().trim()) < Double.parseDouble(monthMoneyEditText.getText().toString().trim())) {
                Toast.makeText(this, "押金需要大于月租金", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "输入信息错误，请检查输入项", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.beginTimeTextView: {
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
                        beginTimeTextView.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                });
                dialog.show();
            }
            break;

            case R.id.endTimeTextView: {
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
                        endTimeTextView.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                });
                dialog.show();
            }
            break;

            case R.id.commitBtn:
                if (checkValue()) {
                    requestInfo();
                }
                break;
        }
    }
}
