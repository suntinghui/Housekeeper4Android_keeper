package com.housekeeper.activity.tenant;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseReleaseListAppDto;
import com.ares.house.dto.app.Paginable;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.PublishRoommateAdapter;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.model.Linyi;
import com.housekeeper.utils.ActivityUtil;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/29/15.
 */
public class TenantPublishListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    final String[] TITLELIST = new String[]{"区域", "租金", "户型"};
    final String[] MONEYLIST = new String[]{"不限", "500以下", "500-1000", "1000－1500", "1500－2000", "2000以上"};
    final String[] ROOMCOUNTLST = new String[]{"不限", "一室", "二室", "三室", "四室及以上"};

    private boolean roommate = true; // true－合租  false－ 整租

    private ListView listView;
    private PublishRoommateAdapter adapter;

    private SwipeRefreshLayout mSwipeLayout = null;
    private List<HouseReleaseListAppDto> mList = new ArrayList<HouseReleaseListAppDto>();

    private DropDownMenu mMenu;

    private int pageNo = 1;
    private int totalPage = 0;

    private String areaId = Linyi.getCode(0);
    private int minMoney = 0;
    private int maxMoney = Integer.MAX_VALUE;
    private int roomCount = 0; // 0不限制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_publish_list);

        this.roommate = this.getIntent().getBooleanExtra("roommate", true);

        this.initView();

        this.requestPublishList("正在请求数据...");
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText(this.roommate ? "合租" : "整租");

        initSwipeRefresh();

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new PublishRoommateAdapter(this);
        listView.setAdapter(adapter);

        // DropDownMenu
        mMenu = (DropDownMenu) findViewById(R.id.menu);

        mMenu.setmMenuCount(3);
        mMenu.setmShowCount(6);
        mMenu.setShowCheck(true);
        mMenu.setmMenuTitleTextSize(15);
        mMenu.setmMenuTitleTextColor(Color.parseColor("#444444"));
        mMenu.setmMenuListTextSize(14);
        mMenu.setmMenuListTextColor(Color.parseColor("#666666"));
        mMenu.setmMenuBackColor(Color.parseColor("#eeeeee"));
        mMenu.setmMenuPressedBackColor(Color.WHITE);
        mMenu.setmMenuPressedTitleTextColor(getResources().getColor(R.color.darkyellow));

        mMenu.setmCheckIcon(R.drawable.ico_make);

        mMenu.setmUpArrow(R.drawable.tenant_arrow_up);
        mMenu.setmDownArrow(R.drawable.tenant_arrow_down);

        mMenu.setDefaultMenuTitle(TITLELIST);

        mMenu.setIsDebug(false);
        mMenu.setShowDivider(true);
        mMenu.setmMenuListBackColor(getResources().getColor(R.color.white));
        mMenu.setmMenuListSelectorRes(R.color.white);
        mMenu.setmArrowMarginTitle(30);

        mMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int rowIndex, int columnIndex) {
                if (columnIndex == 0) { // 区域
                    areaId = Linyi.getCode(rowIndex);

                } else if (columnIndex == 1) { // 租金
                    switch (rowIndex) { // "不限", "500以下", "500-1000", "1000－1500", "1500－2000", "2000以上"
                        case 0:
                            minMoney = 0;
                            maxMoney = Integer.MAX_VALUE;
                            break;

                        case 1:
                            minMoney = 0;
                            maxMoney = 500;
                            break;

                        case 2:
                            minMoney = 500;
                            maxMoney = 1000;
                            break;

                        case 3:
                            minMoney = 1000;
                            maxMoney = 1500;
                            break;

                        case 4:
                            minMoney = 1500;
                            maxMoney = 2000;
                            break;

                        case 5:
                            minMoney = 2000;
                            maxMoney = Integer.MAX_VALUE;
                            break;

                    }

                } else if (columnIndex == 2) {// 户型
                    switch (rowIndex) { // "不限", "一室", "二室", "三室", "四室及以上"
                        case 0:
                            roomCount = 0;
                            break;

                        case 1:
                            roomCount = 1;
                            break;

                        case 2:
                            roomCount = 2;
                            break;

                        case 3:
                            roomCount = 3;
                            break;

                        case 4:
                            roomCount = 4;
                            break;
                    }
                }

                refresh();
            }
        });
        List<String[]> items = new ArrayList<>();
        items.add(Linyi.getCityNameList());
        items.add(MONEYLIST);
        items.add(ROOMCOUNTLST);
        mMenu.setmMenuItems(items);
    }

    @SuppressLint("ResourceAsColor")
    private void initSwipeRefresh() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnLoadListener(this);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mSwipeLayout.setLoadNoFull(true);
    }

    private void refresh() {
        pageNo = 1;
        totalPage = 0;

        requestPublishList("正在请求数据...");
    }

    // 下拉刷新
    @Override
    public void onRefresh() {
        pageNo = 1;
        totalPage = 0;

        this.requestPublishList(null);
    }

    // 上拉刷新
    @Override
    public void onLoad() {
        pageNo++;

        if (pageNo > totalPage) {
            Toast.makeText(this, "没有更多数据", Toast.LENGTH_SHORT).show();
            mSwipeLayout.setLoading(false);
            mSwipeLayout.setRefreshing(false);
            return;
        }

        this.requestPublishList(null);
    }

    private void requestPublishList(String msg) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("areaId", areaId);
        tempMap.put("roommate", String.valueOf(this.roommate));
        tempMap.put("minMoney", minMoney + "");
        tempMap.put("maxMoney", maxMoney + "");
        tempMap.put("roomCount", roomCount + "");
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_RELEASE_LIST, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, HouseReleaseListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<HouseReleaseListAppDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        int totalCount = dto.getData().getTotalCount();
                        showCount(totalCount);

                        if (pageNo == 1) {
                            mList.clear();
                        }

                        mList.addAll(dto.getData().getList());
                        adapter.setData(mList);

                        ActivityUtil.setEmptyView(TenantPublishListActivity.this, listView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPublishList("正在请求数据...");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    mSwipeLayout.setLoading(false);
                    mSwipeLayout.setRefreshing(false);

                    if (pageNo == totalPage) {
                        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                mSwipeLayout.setLoading(false);
                mSwipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, msg)) {
            mSwipeLayout.setRefreshing(false);
            mSwipeLayout.setLoading(false);
        }
    }

    private void showCount(int totalCount) {

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
