package com.housekeeper.activity.keeper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ReserveListAppDto;
import com.flashgugu.library.widgets.paginationseekbar.PaginationSeekBar;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.StringUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

/**
 * Created by sth on 10/31/15.
 */
public class KeeperRemarkActivity extends BaseActivity implements View.OnClickListener {

    private EditText remarkEditText;
    private Button handleBtn;
    private PaginationSeekBar seekBar;
    private Button commitBtn;

    private ReserveListAppDto appDto = null;

    private boolean handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_remark);

        this.appDto = (ReserveListAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("设置备注");

        this.remarkEditText = (EditText) this.findViewById(R.id.remarkEditText);
        this.handleBtn = (Button) this.findViewById(R.id.handleBtn);
        this.handleBtn.setOnClickListener(this);

        this.commitBtn = (Button) this.findViewById(R.id.commitBtn);
        this.commitBtn.setOnClickListener(this);

        seekBar = (PaginationSeekBar) findViewById(R.id.starsSeekbar);
        seekBar.setMin(1);
        seekBar.setMax(1);
        seekBar.setPagecountPerOneboard(1, 9);
        seekBar.setHapticFeedbackEnabled(true);

        try {
            remarkEditText.setText(appDto.getCommunity());
            remarkEditText.setSelection(remarkEditText.getText().toString().trim().length());
        } catch (Exception e) {
        }

        this.handle = appDto.isHandle();
        this.handleBtn.setBackgroundResource(this.handle ? R.drawable.btn_toggle_on : R.drawable.btn_toggle_off);
        this.seekBar.setProgress(appDto.getStars());
    }

    private void requestSetRemark() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", appDto.getId() + "");
        map.put("remark", remarkEditText.getText().toString().trim());
        map.put("handle", String.valueOf(handle));
        map.put("stars", this.seekBar.getProgress() + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_RESERVE_REMARK, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(KeeperRemarkActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                        KeeperRemarkActivity.this.finish();

                    } else {
                        Toast.makeText(KeeperRemarkActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在提交数据...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.handleBtn: {
                this.handle = !this.handle;
                this.handleBtn.setBackgroundResource(this.handle ? R.drawable.btn_toggle_on : R.drawable.btn_toggle_off);
            }
            break;

            case R.id.commitBtn:
                this.requestSetRemark();
                break;
        }
    }
}
