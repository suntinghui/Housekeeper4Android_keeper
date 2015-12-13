package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseInfoAppDto;
import com.ares.house.dto.app.ImageAppDto;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.housekeeper.activity.keeper.KeeperAddHouseDeedActivity;
import com.housekeeper.activity.view.EquipmentAdapter;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.EquipmentAppDtoEx;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import cn.trinea.android.view.autoscrollviewpager.ImagePagerAdapter;

/**
 * Created by sth on 10/26/15.
 */
public class HouseInfoActivity extends BaseActivity implements View.OnClickListener {

    private AutoScrollViewPager viewPager = null;
    private ImagePagerAdapter viewPagerAdapter = null;
    private List<ImageAppDto> imageURLList = new ArrayList<ImageAppDto>();

    private LinearLayout indicatorLayout;
    private ImageView[] indicatorImageViews = null;

    private TextView communityTextView = null;
    private TextView typeTextView = null;
    private TextView decorateTextView = null;
    private TextView areaSizeTextView = null;
    private TextView orientationTextView = null;
    private TextView floorTextView = null;
    private TextView houseTypeTextView = null;

    private AsymmetricGridView gridView = null;
    private EquipmentAdapter adapter = null;

    private TextView heatingFeesTextView = null;

    private TextView busTextView = null;
    private TextView subwayTextView = null;

    private LinearLayout houseCertLayout = null; // 房屋证件

    private HouseInfoAppDto appDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_house_info);

        this.initView();

        requestHouseInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != viewPager) {
            viewPager.startAutoScroll();
        }
    }

    public void onPause() {
        super.onPause();

        if (null != viewPager) {
            viewPager.stopAutoScroll();
        }
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("房屋信息");

        this.communityTextView = (TextView) this.findViewById(R.id.communityTextView);
        this.typeTextView = (TextView) this.findViewById(R.id.typeTextView);
        this.decorateTextView = (TextView) this.findViewById(R.id.decorateTextView);
        this.areaSizeTextView = (TextView) this.findViewById(R.id.areaSizeTextView);
        this.orientationTextView = (TextView) this.findViewById(R.id.orientationTextView);
        this.floorTextView = (TextView) this.findViewById(R.id.floorTextView);
        this.houseTypeTextView = (TextView) this.findViewById(R.id.houseTypeTextView);

        gridView = (AsymmetricGridView) this.findViewById(R.id.gridView);

        gridView.setRequestedColumnCount(3);
        gridView.setRowHeight(45);
        gridView.determineColumns();
        gridView.setAllowReordering(true);
        gridView.isAllowReordering(); // true

        adapter = new EquipmentAdapter(this);
        AsymmetricGridViewAdapter asymmetricAdapter = new AsymmetricGridViewAdapter<>(this, gridView, adapter);
        gridView.setAdapter(asymmetricAdapter);

        heatingFeesTextView = (TextView) this.findViewById(R.id.heatingFeesTextView);
        busTextView = (TextView) this.findViewById(R.id.busTextView);
        subwayTextView = (TextView) this.findViewById(R.id.subwayTextView);

        houseCertLayout = (LinearLayout) this.findViewById(R.id.houseCertLayout);
        houseCertLayout.setOnClickListener(this);
    }

    private void initViewPager() {
        // indicator
        indicatorLayout = (LinearLayout) this.findViewById(R.id.indicatorLayout);
        indicatorLayout.removeAllViews();

        indicatorImageViews = new ImageView[imageURLList.size()];
        for (int i = 0; i < imageURLList.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            indicatorImageViews[i] = imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            indicatorLayout.addView(indicatorImageViews[i], layoutParams);
        }

        // http://www.trinea.cn/android/auto-scroll-view-pager/
        // ViewPager
        viewPager = (AutoScrollViewPager) this.findViewById(R.id.viewPager);
        viewPager.setInterval(3000);
        viewPager.setCycle(true);
        viewPager.setAutoScrollDurationFactor(7.0);
        viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        viewPager.setStopScrollWhenTouch(false);
        viewPagerAdapter = new ImagePagerAdapter(this, imageURLList);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int index) {
                int position = index % imageURLList.size();
                for (int i = 0; i < imageURLList.size(); i++) {
                    if (i == position) {
                        indicatorImageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
                    } else {
                        indicatorImageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                    }

                }
            }

        });
        // viewPagerAdapter.setInfiniteLoop(true);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.startAutoScroll();

        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.houseCertLayout: {
                if (appDto == null)
                    return;

                Intent intent = new Intent(this, KeeperAddHouseDeedActivity.class);
                intent.putExtra("houseId", appDto.getId() + "");
                intent.putExtra("editable", false);
                this.startActivity(intent);
            }
            break;
        }
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            return super.onSingleTapConfirmed(event);
        }
    }

    private void requestHouseInfo() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", this.getIntent().getStringExtra("houseId"));

        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_INFO, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, HouseInfoAppDto.class);
                    AppMessageDto<HouseInfoAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        appDto = dto.getData();

                        responseHouseInfo();

                    } else {
                        Toast.makeText(HouseInfoActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void responseHouseInfo() {
        imageURLList = appDto.getTopImages();
        initViewPager();
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();

        this.communityTextView.setText(appDto.getCommunity() + " " + appDto.getHouseNum());

        this.typeTextView.setText(Html.fromHtml("<font color=#999999>房型：</font><font color=#222222>" + appDto.getType() + "</font>"));
        this.decorateTextView.setText(Html.fromHtml("<font color=#999999>装修：</font><font color=#222222>" + appDto.getDecorate() + "</font>"));
        this.areaSizeTextView.setText(Html.fromHtml("<font color=#999999>面积：</font><font color=#222222>" + appDto.getAreaSize() + " 平米" + "</font>"));
        this.orientationTextView.setText(Html.fromHtml("<font color=#999999>朝向：</font><font color=#222222>" + appDto.getOrientation() + "</font>"));
        this.floorTextView.setText(Html.fromHtml("<font color=#999999>楼层：</font><font color=#222222>" + appDto.getFloor() + "</font>"));
        this.houseTypeTextView.setText(Html.fromHtml("<font color=#999999>类型：</font><font color=#222222>" + appDto.getHouseType() + "</font>"));

        this.adapter.setData(appDto.getEquipments(), false);

        this.heatingFeesTextView.setText(appDto.isHeatingFees() ? "租户交" : "房东交");

        this.busTextView.setText(Html.fromHtml("<font color=#999999>公交：</font><font color=#222222>" + appDto.getBus() + "</font>"));
        this.subwayTextView.setText(Html.fromHtml("<font color=#999999>地铁：</font><font color=#222222>" + appDto.getSubway() + "</font>"));
    }


}
