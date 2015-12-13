package com.housekeeper.activity.keeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.UserJoinAppDto;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.activity.view.VerifyTransferPWDDialog;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * Created by sth on 10/8/15.
 *
 * 关联租户
 */
public class KeeperLeaseRelationActivity extends BaseActivity implements View.OnClickListener {

    private CustomNetworkImageView headImageView;
    private TextView telphoneTextView;
    private LinearLayout relationLayout;
    private CheckBox checkbox;
    private Button commitBtn;

    private LinearLayout tenantInfoLayout;
    private TextView tenantInfoStatusTextView;

    private LinearLayout tenantContractLayout;
    private TextView tenantContractStatusTextView;

    private LinearLayout tenantFeeLayout;
    private TextView tenantFeeStatusTextView;

    private VerifyTransferPWDDialog verifyTransferPwdDialog = null;

    private UserJoinAppDto appDto;
    private WaitLeaseListAppDto infoDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_lease_relation);

        infoDto = (WaitLeaseListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLeaseUserJoinInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("关联租户");

        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);
        this.relationLayout = (LinearLayout) this.findViewById(R.id.relationLayout);
        this.checkbox = (CheckBox) this.findViewById(R.id.checkbox);
        this.commitBtn = (Button) this.findViewById(R.id.commitBtn);

        this.tenantInfoLayout = (LinearLayout) this.findViewById(R.id.tenantInfoLayout);
        this.tenantInfoStatusTextView = (TextView) this.findViewById(R.id.tenantInfoStatusTextView);

        this.tenantContractLayout = (LinearLayout) this.findViewById(R.id.tenantContractLayout);
        this.tenantContractStatusTextView = (TextView) this.findViewById(R.id.tenantContractStatusTextView);

        this.tenantFeeLayout = (LinearLayout) this.findViewById(R.id.tenantFeeLayout);
        this.tenantFeeStatusTextView = (TextView) this.findViewById(R.id.tenantFeeStatusTextView);

        this.relationLayout.setOnClickListener(this);
        this.commitBtn.setOnClickListener(this);
        this.tenantInfoLayout.setOnClickListener(this);
        this.tenantContractLayout.setOnClickListener(this);
        this.tenantFeeLayout.setOnClickListener(this);
    }

    private void requestLeaseUserJoinInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getHouseId() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_USERJOIN_INFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, UserJoinAppDto.class);
                    AppMessageDto<UserJoinAppDto> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        appDto = dto.getData();

                        responseLeaseUserJoinInfo();

                    } else {
                        Toast.makeText(KeeperLeaseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseLeaseUserJoinInfo() {
        if (appDto.isUserJoin()) { // 用户是否已经提交关联码
            this.commitBtn.setEnabled(true);
            this.checkbox.setChecked(true);

            this.telphoneTextView.setText(appDto.getTelphone());
            this.telphoneTextView.setTextColor(getResources().getColor(R.color.blueme));
        } else {
            this.commitBtn.setEnabled(false);
            this.checkbox.setChecked(false);

            this.telphoneTextView.setText("关联后可见");
            this.telphoneTextView.setTextColor(getResources().getColor(R.color.gray_1));
        }


        if (appDto.isUserInfoComplete()) {
            this.tenantInfoStatusTextView.setText("已完成");
            this.tenantInfoStatusTextView.setTextColor(getResources().getColor(R.color.blueme));
        } else {
            this.tenantInfoStatusTextView.setText("未完成");
            this.tenantInfoStatusTextView.setTextColor(getResources().getColor(R.color.gray_1));
        }

        if (appDto.isContractComplete()) {
            this.tenantContractStatusTextView.setText("已完成");
            this.tenantContractStatusTextView.setTextColor(getResources().getColor(R.color.blueme));
        } else {
            this.tenantContractStatusTextView.setText("未完成");
            this.tenantContractStatusTextView.setTextColor(getResources().getColor(R.color.gray_1));
        }

        if (appDto.isInfoComplete()) {
            this.tenantFeeStatusTextView.setText("已完成");
            this.tenantFeeStatusTextView.setTextColor(getResources().getColor(R.color.blueme));
        } else {
            this.tenantFeeStatusTextView.setText("未完成");
            this.tenantFeeStatusTextView.setTextColor(getResources().getColor(R.color.gray_1));
        }
    }

    private void requestConfirmRelation(String pwdStr) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("houseId", infoDto.getHouseId() + "");
        map.put("password", pwdStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_AGENTCONFIRMUSERJOIN, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperLeaseRelationActivity.this, "关联成功", Toast.LENGTH_SHORT).show();

                        KeeperLeaseRelationActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperLeaseRelationActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
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
        verifyTransferPwdDialog.setTitle("请仔细核实租户信息");
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

            case R.id.relationLayout: {
                if (!appDto.isUserInfoComplete()) {
                    Toast.makeText(this, "请先完善租户信息", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!appDto.isContractComplete()) {
                    Toast.makeText(this, "请先完善租户合同", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!appDto.isInfoComplete()) {
                    Toast.makeText(this, "请先完善租金详情", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, KeeperAddTenantRelationQRActivity.class);
                intent.putExtra("UserJoinAppDto", appDto);
                startActivity(intent);
            }
            break;

            case R.id.commitBtn:
                commitAction();
                break;

            case R.id.tenantInfoLayout: {
                Intent intent = new Intent(this, KeeperLeaseInfoActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.tenantContractLayout: {
                Intent intent = new Intent(this, KeeperLeaseContractActivity.class);
                intent.putExtra("DTO", infoDto);
                this.startActivity(intent);
            }
            break;

            case R.id.tenantFeeLayout: {
                Intent intent = new Intent(this, KeeperLeaseFeeActivity.class);
                intent.putExtra("DTO", appDto);
                this.startActivity(intent);
            }
            break;
        }
    }
}
