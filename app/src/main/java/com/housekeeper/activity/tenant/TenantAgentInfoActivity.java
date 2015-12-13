package com.housekeeper.activity.tenant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AgentUserInfoAppDto;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.UserAppDto;
import com.ares.house.dto.app.UserHouseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sth on 10/7/15.
 * <p>
 * 租户 之  中介 信息
 */
public class TenantAgentInfoActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView imageView;
    private TextView nameTextView;
    private TextView idCardTextView;
    private TextView companyTextView;

    private TextView telphoneTextView;

    private Button dialBtn;
    private LinearLayout verifyProfileLayout; // 验证资料

    private AgentUserInfoAppDto appDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_agent_info);

        this.initView();

        this.requestAgentUserInfo();
    }

    private void initView() {
        ((Button) this.findViewById(R.id.backBtn)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("房管家");

        this.imageView = (CircleImageView) this.findViewById(R.id.imageView);
        this.nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        this.idCardTextView = (TextView) this.findViewById(R.id.idCardTextView);
        this.companyTextView = (TextView) this.findViewById(R.id.companyTextView);

        this.telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);

        this.dialBtn = (Button) this.findViewById(R.id.dialBtn);
        this.dialBtn.setOnClickListener(this);

        this.verifyProfileLayout = (LinearLayout) this.findViewById(R.id.verifyProfileLayout);
        this.verifyProfileLayout.setOnClickListener(this);
    }

    private void requestAgentUserInfo() {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("houseId", this.getIntent().getStringExtra("houseId"));

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_AGENT_USERINFO, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, AgentUserInfoAppDto.class);
                    AppMessageDto<AgentUserInfoAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        appDto = dto.getData();

                        responseAgentUserInfo();

                    } else {
                        Toast.makeText(TenantAgentInfoActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseAgentUserInfo() {
        this.imageView.setImageURL(Constants.HOST_IP + appDto.getLogoUrl());
        this.nameTextView.setText("姓名：" + (StringUtils.isBlank(appDto.getRealName()) ? "未知" : appDto.getRealName()));
        this.idCardTextView.setText("身份证号：" + (StringUtils.isBlank(appDto.getIdCard()) ? "未知" : appDto.getIdCard()));
        this.companyTextView.setText("工作单位：" + (StringUtils.isBlank(appDto.getCompanyName()) ? "未知" : appDto.getCompanyName()));
        this.telphoneTextView.setText(appDto.getTelphone());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.dialBtn: {
                if (appDto == null)
                    return;

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + appDto.getTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;

            case R.id.verifyProfileLayout: {
                Intent intent = new Intent(this, TenantKeeperDetailActivity.class);
                intent.putExtra("DTO", appDto);
                this.startActivity(intent);
            }
            break;
        }
    }
}
