package com.housekeeper.activity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ImageAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
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
 * Created by sth on 12/14/15.
 */
public class HomeTopImageLayout extends LinearLayout {

    private BaseActivity context = null;

    private AutoScrollViewPager viewPager = null;
    private ImagePagerAdapter viewPagerAdapter = null;
    private List<ImageAppDto> imageURLList = new ArrayList<ImageAppDto>();

    private LinearLayout indicatorLayout;
    private ImageView[] indicatorImageViews = null;

    public HomeTopImageLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public HomeTopImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_home_top_image, this);
    }

    private void initViewPager() {
        // indicator
        indicatorLayout = (LinearLayout) this.findViewById(R.id.indicatorLayout);
        indicatorLayout.removeAllViews();

        indicatorImageViews = new ImageView[imageURLList.size()];
        for (int i = 0; i < imageURLList.size(); i++) {
            ImageView imageView = new ImageView(context);
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
        viewPagerAdapter = new ImagePagerAdapter(context, imageURLList);

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

        final GestureDetector tapGestureDetector = new GestureDetector(context, new TapGestureListener());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });

    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            return super.onSingleTapConfirmed(event);
        }
    }

    public void requestTopImage() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", "AGENT_INDEX");

        JSONRequest request = new JSONRequest(context, RequestEnum.TOP_IMG, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ImageAppDto.class);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
                    AppMessageDto<List<ImageAppDto>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        responseTopImage(dto.getData());

                    } else {
                        Toast.makeText(context, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, "正在发送请稍候...");
    }

    private void responseTopImage(List<ImageAppDto> list) {
        imageURLList = list;
        initViewPager();
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();
    }

    public AutoScrollViewPager getViewPager() {
        return viewPager;
    }
}
