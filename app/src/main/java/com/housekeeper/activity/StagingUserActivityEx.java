package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.DebtOrderInfoAppDto;
import com.housekeeper.activity.view.CreditReviceDialog;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StagingUserActivityEx extends BaseActivity implements OnClickListener {

    private CustomNetworkImageView headImageView;
    private TextView numTextView;
    private TextView sourceTypeTextView;
    private TextView sourceInfoTextView;
    private TextView companyNameTextView;
    private TextView totalMoneyTextView;

    private LinearLayout detailLayout; // 资料详情
    private LinearLayout creditReviewLayout; // 还款保障

    private DebtOrderInfoAppDto appDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staging_user_ex);

        initView();

        this.requestInfo();
    }

    private void initView() {
        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("债权信息");

        headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        numTextView = (TextView) this.findViewById(R.id.numTextView);
        sourceTypeTextView = (TextView) this.findViewById(R.id.sourceTypeTextView);
        sourceInfoTextView = (TextView) this.findViewById(R.id.sourceInfoTextView);
        companyNameTextView = (TextView) this.findViewById(R.id.companyNameTextView);
        totalMoneyTextView = (TextView) this.findViewById(R.id.totalMoneyTextView);

        detailLayout = (LinearLayout) this.findViewById(R.id.detailLayout);
        detailLayout.setOnClickListener(this);

        creditReviewLayout = (LinearLayout) this.findViewById(R.id.creditReviewLayout);
        creditReviewLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.creditReviewLayout: // 还款保障
                showCreditRevew();
                break;

            case R.id.detailLayout: {// 资料详情
                if (appDto == null)
                    return;

                Intent intent = new Intent(this, ShowWebViewActivity.class);
                intent.putExtra("title", "资料详情");
                intent.putExtra("url", appDto.getInfoUrl());
                startActivity(intent);
            }
            break;
        }

    }

    private void showCreditRevew() {
        CreditReviceDialog dialog = new CreditReviceDialog(this, appDto.getGuarantee());
        dialog.show();
    }

    private void requestInfo() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("sourceNum", this.getIntent().getStringExtra("sourceNum"));

        JSONRequest request = new JSONRequest(this, RequestEnum.DEBTPACKAGE_ORDER_INFO, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, DebtOrderInfoAppDto.class);
                    AppMessageDto<DebtOrderInfoAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        appDto = dto.getData();

                        responseInfo();

                    } else {
                        Toast.makeText(StagingUserActivityEx.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void responseInfo() {
        headImageView.setLocalImageBitmap(R.drawable.user_default_head);
        headImageView.setErrorImageResId(R.drawable.user_default_head);
        headImageView.setImageUrl(appDto.getCompanyLogo(), ImageCacheManager.getInstance().getImageLoader());


        numTextView.setText(appDto.getNum());
        sourceTypeTextView.setText(appDto.getSourceTypeStr()+"：");
        sourceInfoTextView.setText(appDto.getSourceTypeInfo());
        companyNameTextView.setText(appDto.getCompanyName());
        totalMoneyTextView.setText(appDto.getTotalMoney() + "元");
    }

}
