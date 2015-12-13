package com.housekeeper.activity.keeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.ares.house.dto.app.LandlordJoinAppDto;
import com.ares.house.dto.app.TreeNodeAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.SelectCityLayout;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.model.BankEntityEx;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.AdapterUtil;
import com.housekeeper.utils.BankUtil;
import com.housekeeper.utils.IDCardValidate;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sth on 10/4/15.
 */
public class KeeperAddHouseRelationActivity extends BaseActivity implements View.OnClickListener {

    private Button backBtn;
    private TextView titleTextView;
    private CustomNetworkImageView headImageView;
    private TextView telphoneTextView;
    private TextView bankCardTextView;
    private CheckBox checkbox;
    private Button commitBtn;
    private EditText realNameEditText;
    private EditText telphoneEditText;
    private EditText idCardEditText;
    private EditText bankCardEditText;
    private TextView bankTextView;
    private EditText bankAddressEditText;
    private EditText vcodeEditText;
    private Button timeBtn;
    private Button manualCommitBtn;
    private LinearLayout relationLayout;
    private Spinner bankSpinner;

    private SelectCityLayout selectCityLayout = null;

    private List<BankEntityEx> bankList = null;
    private int selectedBankIndex = 0;

    private VerifyTransferPWDDialog verifyTransferPwdDialog = null;

    // 银行短信验证码时间为60s
    private int currentTime = Constants.SMS_MAX_TIME;
    private Timer timer = null;

    private HouseAddListAppDto infoDto = null;
    private LandlordJoinAppDto appDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house_relation);

        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("DTO");

        bankList = BankUtil.getBankList(this);
        if (bankList == null || bankList.size() == 0) {
            Toast.makeText(this, "正在初始化银行列表，请重试", Toast.LENGTH_SHORT).show();

            this.finish();
        }

        this.initView();

        this.requestBankArea();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestHouseInfo();
    }

    private void initView() {
        this.backBtn = (Button) this.findViewById(R.id.backBtn);
        this.titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);
        this.bankCardTextView = (TextView) this.findViewById(R.id.bankCardTextView);
        this.checkbox = (CheckBox) this.findViewById(R.id.checkbox);
        this.commitBtn = (Button) this.findViewById(R.id.commitBtn);
        this.realNameEditText = (EditText) this.findViewById(R.id.realNameEditText);
        this.telphoneEditText = (EditText) this.findViewById(R.id.telphoneEditText);
        this.idCardEditText = (EditText) this.findViewById(R.id.idCardEditText);
        this.bankCardEditText = (EditText) this.findViewById(R.id.bankCardEditText);
        this.bankTextView = (TextView) this.findViewById(R.id.bankTextView);
        this.bankAddressEditText = (EditText) this.findViewById(R.id.bankAddressEditText);
        this.vcodeEditText = (EditText) this.findViewById(R.id.vcodeEditText);
        this.timeBtn = (Button) this.findViewById(R.id.timeBtn);
        this.manualCommitBtn = (Button) this.findViewById(R.id.manualCommitBtn);
        this.relationLayout = (LinearLayout) this.findViewById(R.id.relationLayout);
        this.bankSpinner = (Spinner) this.findViewById(R.id.bankSpinner);

        this.selectCityLayout = (SelectCityLayout) this.findViewById(R.id.selectCityLayout);


        this.titleTextView.setText("关联房东");

        this.backBtn.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.bankTextView.setOnClickListener(this);
        this.relationLayout.setOnClickListener(this);
        this.manualCommitBtn.setOnClickListener(this);
        this.timeBtn.setOnClickListener(this);

        bankTextView.setText(bankList.get(0).getName());
        bankTextView.setCompoundDrawables(getSelectedDrawable(bankList.get(0).getLogoId()), null, null, null);
        bankTextView.setCompoundDrawablePadding(AdapterUtil.dip2px(this, 10));
    }

    private void requestHouseInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_LANDLORDJOIN_INFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, LandlordJoinAppDto.class);
                    AppMessageDto<LandlordJoinAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        appDto = dto.getData();

                        responseHouseInfo();

                    } else {
                        Toast.makeText(KeeperAddHouseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseHouseInfo() {
        if (appDto.isLandlordJoin()) {
            this.commitBtn.setEnabled(true);
            this.checkbox.setChecked(true);

            this.telphoneTextView.setText(appDto.getTelphone());
            this.telphoneTextView.setTextColor(getResources().getColor(R.color.blueme));

            this.bankCardTextView.setText(appDto.getBankCard());
            this.bankCardTextView.setTextColor(getResources().getColor(R.color.blueme));

        } else {
            this.commitBtn.setEnabled(false);
            this.checkbox.setChecked(false);

            this.telphoneTextView.setText("关联后可见");
            this.telphoneTextView.setTextColor(Color.parseColor("#999999"));

            this.bankCardTextView.setText("关联后可见");
            this.bankCardTextView.setTextColor(Color.parseColor("#999999"));
        }
    }

    private void requestConfirmRelation(String pwdStr) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getId() + "");
        map.put("password", pwdStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_AGENTCONFIRMLANDLORDJOIN, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperAddHouseRelationActivity.this, "房源信息关联成功", Toast.LENGTH_SHORT).show();

                        KeeperAddHouseRelationActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperAddHouseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    // 二维码关联
    private void commitAction() {
        verifyTransferPwdDialog = new VerifyTransferPWDDialog(this);
        verifyTransferPwdDialog.setTitle("请仔细核实房东信息");
        verifyTransferPwdDialog.setTip("交易密码验证通过后方可关联成功");
        verifyTransferPwdDialog.setOnConfirmListener(new VerifyTransferPWDDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String pwdStr) {
                requestConfirmRelation(pwdStr);
            }
        });
        verifyTransferPwdDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.commitBtn: {
                commitAction();
            }
            break;

            case R.id.bankTextView: {
                this.chooseBank();
            }
            break;

            case R.id.relationLayout: {
                Intent intent = new Intent(this, KeeperAddLandlordRelationQRActivity.class);
                intent.putExtra("HouseAddListAppDto", infoDto);
                intent.putExtra("LandlordJoinAppDto", appDto);
                startActivity(intent);
            }
            break;

            case R.id.timeBtn: // 发送短信验证码
                if (checkValueForRequestCode()) {
                    requestSendCode();
                }
                break;

            case R.id.manualCommitBtn: {
                if (checkValueForRequestCommit()) {
                    requestManualCommit();
                }
            }
            break;

        }
    }

    private boolean checkValueForRequestCode() {
        String bank = bankTextView.getText().toString().trim();
        String realname = realNameEditText.getText().toString().trim();
        String telphone = telphoneEditText.getText().toString().trim();
        String idCard = idCardEditText.getText().toString().trim();
        String bankCard = bankCardEditText.getText().toString().trim();

        String idValidate = "";
        try {
            idValidate = IDCardValidate.IDCardValidate(idCard);
        } catch (Exception e) {
            e.printStackTrace();
            idValidate = "身份证号输入错误";
        }

        if (TextUtils.isEmpty(realname)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(realNameEditText);
            return false;

        } else if (TextUtils.isEmpty(telphone)) {
            Toast.makeText(this, "请输入预留的手机号", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(telphoneEditText);
            return false;
        } else if (telphone.length() < 11) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(telphoneEditText);
            return false;
        } else if (TextUtils.isEmpty(idCard)) {
            Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(idCardEditText);
            return false;

        } else if (!idValidate.equals("")) {
            Toast.makeText(this, idValidate, Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(idCardEditText);
            return false;
        } else if (TextUtils.isEmpty(bank)) {
            Toast.makeText(this, "请选择银行", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(bankTextView);
            return false;
        } else if (TextUtils.isEmpty(bankCard)) {
            Toast.makeText(this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(bankCardEditText);
            return false;

        } else if (bankCard.length() < 16) {
            Toast.makeText(this, "银行卡号格式不正确", Toast.LENGTH_SHORT).show();
            ActivityUtil.shakeView(bankCardEditText);
            return false;
        }

        return true;
    }

    private boolean checkValueForRequestCommit() {
        if (checkValueForRequestCode()) {
            String code = vcodeEditText.getText().toString().trim();
            String bankAddress = bankAddressEditText.getText().toString().trim();

            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this, "请输入收到的短信验证码", Toast.LENGTH_SHORT).show();
                ActivityUtil.shakeView(vcodeEditText);
                return false;

            } else if (code.length() < 6) {
                Toast.makeText(this, "请输入6位短信验证码", Toast.LENGTH_SHORT).show();
                ActivityUtil.shakeView(vcodeEditText);
                return false;
            } else if (selectCityLayout.getSelectedAreaId() == 0) {
                this.requestBankArea();

                Toast.makeText(this, "请选择开户城市", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(bankAddress)) {
                Toast.makeText(this, "请输入开户支行名称", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }

        return false;
    }

    private HashMap<String, String> getRequestMap() {
        String realname = realNameEditText.getText().toString().trim();
        String telphone = telphoneEditText.getText().toString().trim();
        String idCard = idCardEditText.getText().toString().trim();
        String bankCard = bankCardEditText.getText().toString().trim();

        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", infoDto.getId() + "");
        tempMap.put("bankId", bankList.get(selectedBankIndex).getCode());
        tempMap.put("realName", realname);
        tempMap.put("telphone", telphone);
        tempMap.put("idCard", idCard);
        tempMap.put("bankCard", bankCard);

        return tempMap;
    }

    // 房东收款账号 － 发送验证码
    private void requestSendCode() {
        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_SETLANDLORDBANKSENDVCODE, getRequestMap(), false, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperAddHouseRelationActivity.this, "验证码已发送至您的手机", Toast.LENGTH_SHORT).show();

                        // 启动定时器
                        startTimer();
                    } else {
                        showMessage(dto.getMsg());
                        // Toast.makeText(BindingBankActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new ResponseErrorListener(this));

        this.addToRequestQueue(request, "正在请求数据...");
    }

    // 提交房东收款账号
    private void requestManualCommit() {
        final HashMap<String, String> map = this.getRequestMap();
        map.put("vcode", vcodeEditText.getText().toString().trim());
        map.put("bankAreaId", selectCityLayout.getSelectedAreaId() + "");
        map.put("bankAddress", bankAddressEditText.getText().toString().trim());

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_SETLANDLORDBANK, map, false, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperAddHouseRelationActivity.this, "关联成功", Toast.LENGTH_SHORT).show();
                        KeeperAddHouseRelationActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperAddHouseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new ResponseErrorListener(this));

        this.addToRequestQueue(request, "正在请求数据...");
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
                        Toast.makeText(KeeperAddHouseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    // timer
    private void startTimer() {
        currentTime = Constants.SMS_MAX_TIME;
        timeBtn.setText(currentTime + " 秒后重发");
        timeBtn.setVisibility(View.VISIBLE);

        timeBtn.setEnabled(false);
        timeBtn.setTextColor(Color.parseColor("#999999"));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }

        }, 1000, 1000);
    }

    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    if (currentTime > 0) {
                        timeBtn.setText(--currentTime + " 秒后重发");

                    } else {
                        timer.cancel();
                        timer = null;

                        timeBtn.setEnabled(true);
                        timeBtn.setTextColor(Color.parseColor("#ffffff"));
                        timeBtn.setText("重新发送");
                    }

                    break;

                default:
                    break;
            }
        }
    };

    private void showMessage(String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(null).setContentText("\n" + msg + "\n").setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        }).show();
    }

    // 选择银行卡
    private void chooseBank() {
        final SpinnerAdapter adapter = new SpinnerAdapter(this);
        // bankSpinner.setPrompt("请选择银行");
        bankSpinner.setAdapter(adapter);
        adapter.setSelectedIndex(0);
        bankSpinner.setSelection(selectedBankIndex);
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankTextView.setText(bankList.get(position).getName());
                bankTextView.setCompoundDrawables(getSelectedDrawable(bankList.get(position).getLogoId()), null, null, null);

                selectedBankIndex = position;

                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bankSpinner.performClick();
    }

    private class ViewHolder {
        private LinearLayout contentLayout;
        private TextView bankNameTextView;
    }

    public class SpinnerAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        private Context mContext;

        public SpinnerAdapter(Context pContext) {
            this.mContext = pContext;

            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return bankList.size();
        }

        @Override
        public Object getItem(int position) {
            return bankList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (null == convertView) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.spinner_bank_item, null);

                holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);
                holder.bankNameTextView = (TextView) convertView.findViewById(R.id.bankNameTextView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.bankNameTextView.setText(bankList.get(position).getName());
            holder.bankNameTextView.setCompoundDrawables(getSelectedDrawable(bankList.get(position).getLogoId()), null, null, null);
            holder.contentLayout.setSelected(selectedIndex == position);

            return convertView;
        }

        private int selectedIndex = 0;

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

    }

    private Drawable getSelectedDrawable(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
