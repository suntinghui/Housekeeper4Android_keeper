package com.housekeeper.activity.view;

import org.codehaus.jackson.map.DeserializationConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import com.android.volley.Response;

import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.DebtPackageAppDto;
import com.ares.house.dto.app.Paginable;
import com.housekeeper.activity.InvestmentActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;
import com.wufriends.housekeeper.keeper.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 投资理财 定投
 *
 * @author sth
 */

public class InvestmentScheduledLayout extends LinearLayout implements OnRefreshListener, OnLoadListener {

    private InvestmentActivity context;
    private ListView scheduledListView = null;
    private VoteOfScheduledAdapter scheduledAdapter = null;
    private SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = null;
    private SwipeRefreshLayout mSwipeLayout = null;

    private List<DebtPackageAppDto> deptList = null;

    private int pageNo = 1;
    private int totalPage = 0;

    public InvestmentScheduledLayout(InvestmentActivity context) {
        super(context);

        this.initView(context);
    }

    public InvestmentScheduledLayout(InvestmentActivity context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(InvestmentActivity context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_investment_scheduled, this);

        initSwipeRefresh();

        scheduledListView = (ListView) this.findViewById(R.id.scheduledListView);
        scheduledAdapter = new VoteOfScheduledAdapter(this.context);
        swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(scheduledAdapter);
        swingBottomInAnimationAdapter.setAbsListView(scheduledListView);
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(Constants.INITIAL_DELAY_MILLIS);
        scheduledListView.setAdapter(swingBottomInAnimationAdapter);
    }

    @SuppressLint("ResourceAsColor")
    private void initSwipeRefresh() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);
        mSwipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mSwipeLayout.setLoadNoFull(true);
    }

    // 下拉刷新
    @Override
    public void onRefresh() {
        pageNo = 1;
        totalPage = 0;

        this.requestDebetListAction(true, null);
    }

    // 上拉刷新
    @Override
    public void onLoad() {
        pageNo++;

        if (pageNo > totalPage) {
            Toast.makeText(this.context, "没有更多数据", Toast.LENGTH_SHORT).show();
            mSwipeLayout.setLoading(false);
            return;
        }

        this.requestDebetListAction(false, null);
    }

    public void initData() {
        if (deptList == null) {
            deptList = new ArrayList<DebtPackageAppDto>();
            refreshData("正在请求数据...");
        }
    }

    public void refreshData(String msg) {
        requestDebetListAction(true, msg);
    }

    private void requestDebetListAction(final boolean isRefresh, String msg) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");
        tempMap.put("type", "DT");

        JSONRequest request = new JSONRequest(this.context, RequestEnum.DEBTPACKAGE_LIST, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, DebtPackageAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<DebtPackageAppDto>> dto = null;

                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        if (isRefresh) {
                            deptList.clear();
                        }
                        deptList.addAll(dto.getData().getList());
                        scheduledAdapter.setData(deptList);

                        scheduledAdapter.notifyDataSetChanged();
                        swingBottomInAnimationAdapter.notifyDataSetChanged();

                        ActivityUtil.setEmptyView(InvestmentScheduledLayout.this, scheduledListView).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refreshData("正在请求数据...");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setLoading(false);

                    if (pageNo == totalPage) {
                        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(context) {

            @Override
            public void todo() {
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setLoading(false);
            }
        });

        if (!context.addToRequestQueue(request, msg)) {
            mSwipeLayout.setRefreshing(false);
            mSwipeLayout.setLoading(false);
        }
    }

}
