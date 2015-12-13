package com.housekeeper.activity.landlord;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ContactAppDto;
import com.ares.house.dto.app.OpenCityAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.ContactCityLayout;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * Created by sth on 10/31/15.
 */
public class LandlordContactKeeperActivity extends BaseActivity implements View.OnClickListener {

    private TextView telphoneTextView = null;
    private LinearLayout contentLayout = null;

    private ContactAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_landlord_contact_keeper);

        this.initView();

        requestContactInfo();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("联系房管家");

        this.telphoneTextView = (TextView) this.findViewById(R.id.telphoneTextView);
        this.contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
    }

    private void requestContactInfo() {
        JSONRequest request = new JSONRequest(this, RequestEnum.CONTACT, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, ContactAppDto.class);
                    AppMessageDto<ContactAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        infoDto = dto.getData();

                        responseContactInfo();

                    } else {
                        Toast.makeText(LandlordContactKeeperActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void responseContactInfo() {
        String str = "我们正在努力和更多城市的优秀房产经纪公司合作，如您希望和我们合用，请致电：" + "<a href=\"tel:" + this.infoDto.getTelphone() + "\">" + this.infoDto.getTelphone() + "</a>";
        this.telphoneTextView.setText(Html.fromHtml(str));

        this.contentLayout.removeAllViews();
        for (OpenCityAppDto dto : this.infoDto.getOpenCitys()) {
            ContactCityLayout layout = new ContactCityLayout(this);
            layout.setData(dto);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            this.contentLayout.addView(layout, params);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;
        }
    }
}
