package com.housekeeper.activity.keeper;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.ares.house.dto.app.HouseReleaseAppDto;
import com.ares.house.dto.app.HouseTagAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.SublimePickerDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.DateUtil;
import com.wufriends.housekeeper.keeper.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sth on 10/27/15.
 */
public class KeeperHousePublishActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText beginTimeTextView;
    private TextView begingDateTipTextView;

    private EditText monthMoneyEditText;
    private TextView moneyTipTextView;

    private TextView earningTextView;

    private TagFlowLayout flowlayout = null;

    private Button publishBtn;

    private HouseReleaseAppDto infoDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_house_publish);

        this.initView();

        this.requestPublishInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("发布房源");

        this.beginTimeTextView = (EditText) this.findViewById(R.id.beginTimeTextView);
        this.beginTimeTextView.setOnClickListener(this);
        this.beginTimeTextView.setText(DateUtil.getCurrentDate2());

        this.begingDateTipTextView = (TextView) this.findViewById(R.id.begingDateTipTextView);

        this.monthMoneyEditText = (EditText) this.findViewById(R.id.monthMoneyEditText);
        this.monthMoneyEditText.addTextChangedListener(this);

        this.moneyTipTextView = (TextView) this.findViewById(R.id.moneyTipTextView);

        this.earningTextView = (TextView) this.findViewById(R.id.earningTextView);

        this.flowlayout = (TagFlowLayout) this.findViewById(R.id.flowlayout);
        this.flowlayout.setVisibility(View.GONE);

        this.publishBtn = (Button) this.findViewById(R.id.publishBtn);
        this.publishBtn.setOnClickListener(this);
    }

    private void requestPublishInfo() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", this.getIntent().getStringExtra("houseId"));

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_GETRELEASE, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, HouseReleaseAppDto.class);
                    AppMessageDto<HouseReleaseAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        infoDto = dto.getData();

                        responsePublishInfo();
                    } else {
                        Toast.makeText(KeeperHousePublishActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void responsePublishInfo() {
        if (StringUtils.isBlank(infoDto.getBeginTimeStr())) {
            this.beginTimeTextView.setText(DateUtil.getCurrentDate2());
        } else {
            this.beginTimeTextView.setText(infoDto.getBeginTimeStr());
        }

        this.begingDateTipTextView.setText("提示：入住时间不能早于 " + infoDto.getBeginTimeStr());

        this.moneyTipTextView.setText("提示：大于等于" + infoDto.getMoney() + "元，多于部分作为佣金奖励。");

        flowlayout.setVisibility(View.VISIBLE);
        flowlayout.setAdapter(new TagAdapter<HouseTagAppDto>(infoDto.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, HouseTagAppDto s) {
                TextView tv = (TextView) LayoutInflater.from(KeeperHousePublishActivity.this).inflate(R.layout.tag_layout, parent, false);
                tv.setText(s.getName());
                return tv;
            }
        });
    }

    private boolean checkValue() {
        if (StringUtils.isBlank(monthMoneyEditText.getText().toString().trim())) {
            Toast.makeText(this, "请输入租金", Toast.LENGTH_SHORT).show();
            return false;
        } else if (this.flowlayout.getSelectedList().size() == 0) {
            Toast.makeText(this, "请选择标签", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void requestPublish() {
        String tagIds = "";
        for (Iterator<Integer> iterator = this.flowlayout.getSelectedList().iterator(); iterator.hasNext(); ) {
            tagIds += (infoDto.getTags().get(iterator.next()).getId() + ",");
        }

        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", this.getIntent().getStringExtra("houseId"));
        tempMap.put("monthMoney", monthMoneyEditText.getText().toString().trim());
        tempMap.put("tagIds", tagIds.substring(0, tagIds.lastIndexOf(',')));
        tempMap.put("leaseTime", String.valueOf(DateUtil.string2MilliSec(beginTimeTextView.getText().toString())));

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_RELEASE, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperHousePublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();

                        KeeperHousePublishActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperHousePublishActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
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

            case R.id.publishBtn:
                if (checkValue()) {
                    requestPublish();
                }
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (infoDto == null)
            return;

        try {
            double input = Double.parseDouble(editable.toString());
            double limit = Double.parseDouble(infoDto.getMoney());

            if (input < limit) {
                this.monthMoneyEditText.setTextColor(this.getResources().getColor(R.color.redme));

                this.earningTextView.setText("0.00 元/月");

            } else {
                this.monthMoneyEditText.setTextColor(Color.parseColor("#666666"));

                this.earningTextView.setText((input - limit) * infoDto.getProportion() / 100 + " 元/月");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

