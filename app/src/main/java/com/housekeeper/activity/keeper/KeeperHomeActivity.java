package com.housekeeper.activity.keeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.LeasedListAppDto;
import com.ares.house.dto.app.Paginable;
import com.ares.house.dto.app.WaitLeaseListAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.HousePushIntentService;
import com.housekeeper.activity.view.EmptyLayout;
import com.housekeeper.activity.view.HomeTopImageLayout;
import com.housekeeper.activity.view.HomeTopLayout;
import com.housekeeper.activity.view.KeeperLeasedAdapter;
import com.housekeeper.activity.view.KeeperUnLeaseAdapter;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.melnykov.fab.FloatingActionButton;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 9/16/15.
 */
public class KeeperHomeActivity extends BaseActivity implements HomeTopLayout.ItemChangeListener {

    private int type = HomeTopLayout.TYPE_UNLEASE;

    private HomeTopLayout topLayout = null;
    private HomeTopImageLayout topImageLayout = null;
    private EmptyLayout emptyLayout = null;

    private ListView listView = null;

    private KeeperLeasedAdapter leasedAdapter = null;
    private KeeperLeasedAdapter cancelLeaseAdapter = null;
    private KeeperUnLeaseAdapter unLeaseAdapter = null;

    private List<LeasedListAppDto> leasedList = new ArrayList<LeasedListAppDto>();
    private List<LeasedListAppDto> cancelLeaseList = new ArrayList<LeasedListAppDto>();
    private List<WaitLeaseListAppDto> unLeaseList = new ArrayList<WaitLeaseListAppDto>();

    private SwipeRefreshLayout swipeLayout = null;
    private int pageNo = 1;
    private int totalPage = 0;

    private FloatingActionButton floatButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_home);

        this.initView();

        this.aboutUmeng();

        requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != topImageLayout.getViewPager()) {
            topImageLayout.getViewPager().startAutoScroll();
        }

        topImageLayout.requestTopImage();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != topImageLayout.getViewPager()) {
            topImageLayout.getViewPager().stopAutoScroll();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        requestData();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("首页");

        this.findViewById(R.id.backBtn).setVisibility(View.GONE);

        this.initSwipeRefresh();

        this.initTopImageView();

        this.initTopItemView();

        this.initFooterView();

        listView = (ListView) this.findViewById(R.id.listView);

        leasedAdapter = new KeeperLeasedAdapter(this);
        cancelLeaseAdapter = new KeeperLeasedAdapter(this);
        unLeaseAdapter = new KeeperUnLeaseAdapter(this);

        floatButton = (FloatingActionButton) this.findViewById(R.id.floatButton);
        floatButton.attachToListView(listView);
        floatButton.hide();
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setSelection(0);
            }
        });
    }

    private void initTopImageView() {
        topImageLayout = new HomeTopImageLayout(this);
    }

    private void initTopItemView() {
        topLayout = new HomeTopLayout(this);
        topLayout.setOnItemChangeListener(this);
    }

    private void initFooterView() {
        emptyLayout = new EmptyLayout(this);
        emptyLayout.getEmptyImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });
    }

    private void setAdapter(BaseAdapter adapter) {
        try {
            listView.removeHeaderView(topImageLayout);
            listView.removeHeaderView(topLayout);
            listView.removeFooterView(emptyLayout);

        } catch (Exception e) {
        }

        listView.addHeaderView(topImageLayout, null, false);
        listView.addHeaderView(topLayout, null, false);

        if (adapter.isEmpty()) {
            listView.addFooterView(emptyLayout);
        }

        if (adapter.getCount() > 3) {
            floatButton.show(true);
        } else {
            floatButton.hide(false);
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    private void initSwipeRefresh() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNo++;

                if (pageNo > totalPage) {
                    Toast.makeText(KeeperHomeActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                    swipeLayout.setLoading(false);
                    swipeLayout.setRefreshing(false);
                    return;
                }

                requestData();
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                totalPage = 0;

                requestData();
            }
        });

        swipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        swipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        swipeLayout.setLoadNoFull(true);
    }

    private void requestData() {
        if (type == HomeTopLayout.TYPE_UNLEASE) {
            requestUnLeaseList();
        } else if (type == HomeTopLayout.TYPE_LEASED) {
            requestLeasedList();
        } else if (type == HomeTopLayout.TYPE_CANCELLEASE) {
            requestCancelLeaseList();
        }
    }

    // 退租
    private void requestCancelLeaseList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");
        tempMap.put("return", true + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_LEASED, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, LeasedListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<LeasedListAppDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        if (pageNo == 1) {
                            cancelLeaseList.clear();
                        }

                        cancelLeaseList.addAll(dto.getData().getList());
                        cancelLeaseAdapter.setData(cancelLeaseList, true);

                        setAdapter(cancelLeaseAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    swipeLayout.setLoading(false);
                    swipeLayout.setRefreshing(false);

                    if (pageNo == totalPage) {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                swipeLayout.setLoading(false);
                swipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, "正在请求数据...")) {
            swipeLayout.setRefreshing(false);
            swipeLayout.setLoading(false);
        }
    }

    // 已租
    private void requestLeasedList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");
        tempMap.put("return", false + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_LEASED, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, LeasedListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<LeasedListAppDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        if (pageNo == 1) {
                            leasedList.clear();
                        }

                        leasedList.addAll(dto.getData().getList());
                        leasedAdapter.setData(leasedList, false);

                        setAdapter(leasedAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    swipeLayout.setLoading(false);
                    swipeLayout.setRefreshing(false);

                    if (pageNo == totalPage) {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                swipeLayout.setLoading(false);
                swipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, "正在请求数据...")) {
            swipeLayout.setRefreshing(false);
            swipeLayout.setLoading(false);
        }
    }

    private void requestUnLeaseList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_WAIT, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, WaitLeaseListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<WaitLeaseListAppDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        if (pageNo == 1) {
                            unLeaseList.clear();
                        }

                        unLeaseList.addAll(dto.getData().getList());
                        unLeaseAdapter.setData(unLeaseList);

                        setAdapter(unLeaseAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    swipeLayout.setLoading(false);
                    swipeLayout.setRefreshing(false);

                    if (pageNo == totalPage) {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        swipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                swipeLayout.setLoading(false);
                swipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, "正在请求数据...")) {
            swipeLayout.setRefreshing(false);
            swipeLayout.setLoading(false);
        }
    }

    @Override
    public void onItemChanged(int type) {
        this.type = type;

        pageNo = 1;
        totalPage = 0;

        requestData();


        switch (type) {
            case HomeTopLayout.TYPE_LEASED: {

            }
            break;

            case HomeTopLayout.TYPE_UNLEASE: {

            }
            break;

            case HomeTopLayout.TYPE_CANCELLEASE: {

            }
            break;
        }
    }

    private void aboutUmeng() {
        // UMeng
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);
        MobclickAgent.setAutoLocation(true);

        // 推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mPushAgent.setDebugMode(false);
        mPushAgent.setPushIntentServiceClass(HousePushIntentService.class);

        String deviceToken = UmengRegistrar.getRegistrationId(this);
        Log.e("UMENG", "UMENG DEVICE TOKEN : " + deviceToken);
        SharedPreferences.Editor editor = ActivityUtil.getSharedPreferences().edit();
        editor.putString(Constants.DEVICETOKEN, deviceToken);
        editor.commit();

        // 解决在通知栏里面显示的始终是最新的那一条的问题，谨慎使用，以免用户看到消息过多卸载应用。
        // 合并
        mPushAgent.setMergeNotificaiton(true);

        mPushAgent.onAppStart();

        // UMeng检查更新
        checkUpdate();
    }

    // 检查更新
    private void checkUpdate() {
        // 因为友盟的更新设置是静态的参数，如果在应用中不止一次调用了检测更新的方法，而每次的设置都不一样，请在每次检测更新的函数之前先恢复默认设置再设置参数，避免在其他地方设置的参数影响到这次更新
        UmengUpdateAgent.setDefault();
        // updateOnlyWifi 布尔值true(默认)只在wifi环境下检测更新，false在所有网络环境中均检测更新。
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // deltaUpdate 布尔值true(默认)使用增量更新，false使用全量更新。看了FAQ，貌似增量更新会可能有问题，为了保险起见，不使用增量更新
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTimeMillis = 0;

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTimeMillis = System.currentTimeMillis();
        } else {
            MobclickAgent.onKillProcess(this); // 用来保存统计数据

            for (Activity act : ActivityManager.getInstance().getAllActivity()) {
                act.finish();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
