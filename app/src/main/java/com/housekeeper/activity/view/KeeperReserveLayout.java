package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ReserveListAppDto;
import com.housekeeper.activity.keeper.KeeperRemarkActivity;
import com.housekeeper.activity.keeper.KeeperReserveListActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.onivas.promotedactions.PromotedActionsLibrary;

/**
 * Created by sth on 10/31/15.
 */
public class KeeperReserveLayout extends LinearLayout {

    private KeeperReserveListActivity context = null;

    private CustomNetworkImageView logoImageView;
    private TextView usernameTextView;
    private TextView dialTextView;
    private TextView setRemarkTextView;
    private FrameLayout operatorLayout;

    private TextView remarkTextView;
    private TextView handleTextView;
    private TextView starsTextView;

    private ReserveListAppDto dto = null;

    public KeeperReserveLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public KeeperReserveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (KeeperReserveListActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_keeper_reserve, this);

        this.logoImageView = (CustomNetworkImageView) this.findViewById(R.id.logoImageView);
        this.usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        this.dialTextView = (TextView) this.findViewById(R.id.dialTextView);
        this.operatorLayout = (FrameLayout) this.findViewById(R.id.operatorLayout);
        initMenu();

        this.setRemarkTextView = (TextView) this.findViewById(R.id.setRemarkTextView);
        this.remarkTextView = (TextView) this.findViewById(R.id.remarkTextView);
        this.handleTextView = (TextView) this.findViewById(R.id.handleTextView);
        this.starsTextView = (TextView) this.findViewById(R.id.starsTextView);
    }

    private void initMenu() {
        final PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();

        promotedActionsLibrary.setup(context.getApplicationContext(), this.operatorLayout);
        promotedActionsLibrary.setIsPortrait(false);

        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.keeper_img_18), new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dto.getTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.keeper_img_19), new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperRemarkActivity.class);
                intent.putExtra("DTO", dto);
                context.startActivity(intent);
            }
        });
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.keeper_img_17), new OnClickListener() {

            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("\n您确定要删除吗？").setContentText("").setCancelText("取消").setConfirmText("确定").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                        requestDelete();

                    }
                }).show();
            }
        });

        promotedActionsLibrary.addMainItem(getResources().getDrawable(R.drawable.keeper_img_16));
    }

    public void setData(final ReserveListAppDto dto) {
        this.dto = dto;

        this.logoImageView.setImageUrl(Constants.HOST_IP + dto.getUserImg(), ImageCacheManager.getInstance().getImageLoader());
        this.usernameTextView.setText(dto.getUserName());
        this.remarkTextView.setText("备注：" + (StringUtils.isBlank(dto.getRemark()) ? "无" : dto.getRemark()));
        this.handleTextView.setText(dto.isHandle() ? "已看房" : "未看房");
        this.starsTextView.setText("租房意向：" + dto.getStars());

        this.setRemarkTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperRemarkActivity.class);
                intent.putExtra("DTO", dto);
                context.startActivity(intent);
            }
        });

        this.dialTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dto.getTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void requestDelete() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", this.dto.getId() + "");

        JSONRequest request = new JSONRequest(context, RequestEnum.HOUSE_RESERVE_DELETE, map, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

                        context.requestReserveList();

                    } else {
                        Toast.makeText(context, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, "正在提交数据...");
    }

}
