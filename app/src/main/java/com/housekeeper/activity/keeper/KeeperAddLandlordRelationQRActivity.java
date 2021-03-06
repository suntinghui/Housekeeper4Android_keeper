package com.housekeeper.activity.keeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.ares.house.dto.app.LandlordJoinAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.MyImageRequest;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 10/5/15.
 */
public class KeeperAddLandlordRelationQRActivity extends BaseActivity implements View.OnClickListener {

    private CustomNetworkImageView headImageView;
    private TextView addressTextView;
    private TextView cityTextView;
    private TextView begingDateTextView;
    private TextView endDateTextView;
    private TextView yearMoneyTextView;

    private ImageView qrImageView;
    private TextView numberTextView;

    private LandlordJoinAppDto appDto;
    private HouseAddListAppDto infoDto;

    private TabhostReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_landlord_relation_qr);

        appDto = (LandlordJoinAppDto) this.getIntent().getSerializableExtra("LandlordJoinAppDto");
        infoDto = (HouseAddListAppDto) this.getIntent().getSerializableExtra("HouseAddListAppDto");

        this.initView();

        this.registerTabhostReceiver();

        this.requestQRCodeImage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(receiver);
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("房东关联");

        this.headImageView = (CustomNetworkImageView) this.findViewById(R.id.headImageView);
        this.addressTextView = (TextView) this.findViewById(R.id.addressTextView);
        this.cityTextView = (TextView) this.findViewById(R.id.cityTextView);
        this.begingDateTextView = (TextView) this.findViewById(R.id.begingDateTextView);
        this.endDateTextView = (TextView) this.findViewById(R.id.endDateTextView);
        this.yearMoneyTextView = (TextView) this.findViewById(R.id.yearMoneyTextView);

        this.qrImageView = (ImageView) this.findViewById(R.id.qrImageView);

        this.numberTextView = (TextView) this.findViewById(R.id.numberTextView);
        this.numberTextView.setText(appDto.getLandlordJoinCode());

        // Set Value
        this.headImageView.setImageUrl(Constants.HOST_IP + appDto.getLogo(), ImageCacheManager.getInstance().getImageLoader());

        this.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
        this.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
        this.begingDateTextView.setText(infoDto.getBeginTimeStr());
        this.endDateTextView.setText(infoDto.getEndTimeStr());
        this.yearMoneyTextView.setText(infoDto.getYearMoney() + " 元");
    }

    private void requestQRCodeImage() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        // /house/qr/{type}/{size}/{houseId}.app
        StringBuffer hostSB = new StringBuffer(Constants.HOST_IP);
        hostSB.append("/rpc/house/qr/");
        hostSB.append("LANDLORD/");
        hostSB.append(width / 2 + "/");
        hostSB.append(appDto.getHouseId() + ".app");

        MyImageRequest request = new MyImageRequest(hostSB.toString(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap response) {

                hideProgress();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrImageView.setImageBitmap(response);
                    }
                });
            }
        }, width, height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private void registerTabhostReceiver() {
        receiver = new TabhostReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHECK_RELATION);
        this.registerReceiver(receiver, filter);
    }

    public class TabhostReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(KeeperAddLandlordRelationQRActivity.this, "房东关联成功，请提交", Toast.LENGTH_SHORT).show();

            finish();
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
