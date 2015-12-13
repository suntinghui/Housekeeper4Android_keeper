package com.housekeeper.activity.tenant;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.LinkArticle;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.MediaImagePagerAdapter;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by sth on 10/23/15.
 */
public class TenantHomeActivity extends BaseActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx scrollView;

    private LinearLayout wholeRentLayout;
    private LinearLayout shareRentLaout;

    private AutoScrollViewPager mediaViewPager = null;
    private MediaImagePagerAdapter mediaViewPagerAdapter = null;
    private List<LinkArticle> mediaImageURLList = new ArrayList<LinkArticle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_home);

        loadViewForCode();

        requestMediaImage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mediaViewPager) {
            mediaViewPager.startAutoScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mediaViewPager) {
            mediaViewPager.stopAutoScroll();
        }
    }

    private void loadViewForCode() {
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        // 背景图片
        View zoomView = LayoutInflater.from(this).inflate(R.layout.tenant_home_profile, null, false);
        scrollView.setZoomView(zoomView);

        // 下面的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.tenant_home_content, null, false);
        scrollView.setScrollContentView(contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        initView();
    }

    private void initView() {
        this.wholeRentLayout = (LinearLayout) scrollView.getRootView().findViewById(R.id.wholeRentLayout);
        this.wholeRentLayout.setOnClickListener(this);

        this.shareRentLaout = (LinearLayout) scrollView.getRootView().findViewById(R.id.shareRentLayout);
        this.shareRentLaout.setOnClickListener(this);
    }

    private void initMediaViewPager() {
        // ViewPager
        mediaViewPager = (AutoScrollViewPager) this.findViewById(R.id.mediaViewPager);
        mediaViewPager.setInterval(3000);
        mediaViewPager.setCycle(true);
        mediaViewPager.setAutoScrollDurationFactor(7.0);
        mediaViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        mediaViewPager.setStopScrollWhenTouch(true);
        mediaViewPagerAdapter = new MediaImagePagerAdapter(this, mediaImageURLList);

        // viewPagerAdapter.setInfiniteLoop(true);
        mediaViewPager.setAdapter(mediaViewPagerAdapter);
        mediaViewPager.startAutoScroll();
    }

    // 取得媒体报道图片
    private void requestMediaImage() {
        JSONRequest request = new JSONRequest(this, RequestEnum.LINK_ARTICLE, null, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, LinkArticle.class);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);
                    AppMessageDto<List<LinkArticle>> dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        mediaImageURLList = dto.getData();

                        initMediaViewPager();

                        mediaViewPagerAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wholeRentLayout: {
                Intent intent = new Intent(this, TenantPublishListActivity.class);
                intent.putExtra("roommate", false);
                this.startActivity(intent);
            }
            break;

            case R.id.shareRentLayout: {
                Intent intent = new Intent(this, TenantPublishListActivity.class);
                intent.putExtra("roommate", true);
                this.startActivity(intent);
            }
            break;
        }
    }
}
