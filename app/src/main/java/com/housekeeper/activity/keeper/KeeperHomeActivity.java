package com.housekeeper.activity.keeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.housekeeper.activity.view.KeeperLeasedAdapter;
import com.housekeeper.activity.view.KeeperUnLeaseAdapter;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
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
public class KeeperHomeActivity extends BaseActivity implements View.OnClickListener {

    private TextView leasedTextView; // 已租
    private TextView unLeaseTextView; // 未租

    private LinearLayout leasedLayout;
    private LinearLayout unLeaseLayout;

    private ListView leasedListView;
    private ListView unLeaseListView;

    private KeeperLeasedAdapter leasedAdapter = null;
    private KeeperUnLeaseAdapter unLeaseAdapter = null;

    private List<LeasedListAppDto> leasedList = new ArrayList<LeasedListAppDto>();
    private List<WaitLeaseListAppDto> unLeaseList = new ArrayList<WaitLeaseListAppDto>();

    private SwipeRefreshLayout unLeaseSwipeLayout = null;
    private int unLeasePageNo = 1;
    private int unLeaseTotalPage = 0;

    private SwipeRefreshLayout leasedSwipeLayout = null;
    private int leasedPageNo = 1;
    private int leasedTotalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_home);

        this.initView();

        this.aboutUmeng();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (leasedTextView.isSelected()) {
            this.requestLeasedList();

        } else if (unLeaseTextView.isSelected()) {
            this.requestUnLeaseList();
        }
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("首页");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);
        backButton.setVisibility(View.GONE);

        this.initLeasedSwipeRefresh();
        this.initUnLeaseSwipeRefresh();

        leasedTextView = (TextView) this.findViewById(R.id.leasedTextView);
        leasedTextView.setOnClickListener(this);

        unLeaseTextView = (TextView) this.findViewById(R.id.unLeaseTextView);
        unLeaseTextView.setOnClickListener(this);

        leasedLayout = (LinearLayout) this.findViewById(R.id.leasedLayout);
        unLeaseLayout = (LinearLayout) this.findViewById(R.id.unLeaseLayout);

        leasedListView = (ListView) this.findViewById(R.id.leasedListView);
        leasedAdapter = new KeeperLeasedAdapter(this);
        leasedListView.setAdapter(leasedAdapter);

        unLeaseListView = (ListView) this.findViewById(R.id.unLeaseListView);
        unLeaseAdapter = new KeeperUnLeaseAdapter(this);
        unLeaseListView.setAdapter(unLeaseAdapter);

        leasedTextView.setSelected(true);
        unLeaseTextView.setSelected(false);
        leasedLayout.setVisibility(View.VISIBLE);
        unLeaseLayout.setVisibility(View.GONE);

        LinearLayout emptyLayout = (LinearLayout) this.findViewById(R.id.emptyLayout);
        leasedListView.setEmptyView(emptyLayout);
        ImageView noDataImageView = (ImageView) emptyLayout.findViewById(R.id.noDataImageView);
        noDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLeasedList();
            }
        });

        LinearLayout unEmptyLayout = (LinearLayout) this.findViewById(R.id.unEmptyLayout);
        unLeaseListView.setEmptyView(unEmptyLayout);
        ImageView unNoDataImageView = (ImageView) unEmptyLayout.findViewById(R.id.noDataImageView);
        unNoDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUnLeaseList();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initLeasedSwipeRefresh() {
        leasedSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.leasedSwipeLayout);
        leasedSwipeLayout.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                leasedPageNo++;

                if (leasedPageNo > leasedTotalPage) {
                    Toast.makeText(KeeperHomeActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                    leasedSwipeLayout.setLoading(false);
                    leasedSwipeLayout.setRefreshing(false);
                    return;
                }

                requestLeasedList();
            }
        });
        leasedSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leasedPageNo = 1;
                leasedTotalPage = 0;

                requestLeasedList();
            }
        });
        leasedSwipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        leasedSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        leasedSwipeLayout.setLoadNoFull(true);
    }

    @SuppressLint("ResourceAsColor")
    private void initUnLeaseSwipeRefresh() {
        unLeaseSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.unLeaseSwipeLayout);
        unLeaseSwipeLayout.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                unLeasePageNo++;

                if (unLeasePageNo > unLeaseTotalPage) {
                    Toast.makeText(KeeperHomeActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                    unLeaseSwipeLayout.setLoading(false);
                    unLeaseSwipeLayout.setRefreshing(false);
                    return;
                }

                requestUnLeaseList();
            }
        });
        unLeaseSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                unLeasePageNo = 1;
                unLeaseTotalPage = 0;

                requestUnLeaseList();
            }
        });
        unLeaseSwipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        unLeaseSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        unLeaseSwipeLayout.setLoadNoFull(true);
    }

    private void requestLeasedList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", leasedPageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");

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

                        leasedTotalPage = dto.getData().getTotalPage();
                        leasedPageNo = dto.getData().getPageNo();

                        if (leasedPageNo == 1) {
                            leasedList.clear();
                        }

                        leasedList.addAll(dto.getData().getList());
                        leasedAdapter.setData(leasedList);

                        ActivityUtil.setEmptyView(KeeperHomeActivity.this, leasedListView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestLeasedList();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    leasedSwipeLayout.setLoading(false);
                    leasedSwipeLayout.setRefreshing(false);

                    if (leasedPageNo == leasedTotalPage) {
                        leasedSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        leasedSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                leasedSwipeLayout.setLoading(false);
                leasedSwipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, "正在请求数据...")) {
            leasedSwipeLayout.setRefreshing(false);
            leasedSwipeLayout.setLoading(false);
        }
    }

    private void requestUnLeaseList() {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", unLeasePageNo + "");
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

                        unLeaseTotalPage = dto.getData().getTotalPage();
                        unLeasePageNo = dto.getData().getPageNo();

                        if (unLeasePageNo == 1) {
                            unLeaseList.clear();
                        }

                        unLeaseList.addAll(dto.getData().getList());
                        unLeaseAdapter.setData(unLeaseList);

                        ActivityUtil.setEmptyView(KeeperHomeActivity.this, unLeaseListView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestUnLeaseList();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    unLeaseSwipeLayout.setLoading(false);
                    unLeaseSwipeLayout.setRefreshing(false);

                    if (unLeasePageNo == unLeaseTotalPage) {
                        unLeaseSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                    } else {
                        unLeaseSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
                    }
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                unLeaseSwipeLayout.setLoading(false);
                unLeaseSwipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, "正在请求数据...")) {
            unLeaseSwipeLayout.setRefreshing(false);
            unLeaseSwipeLayout.setLoading(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.leasedTextView: {
                this.requestLeasedList();

                leasedTextView.setSelected(true);
                unLeaseTextView.setSelected(false);
                leasedLayout.setVisibility(View.VISIBLE);
                unLeaseLayout.setVisibility(View.GONE);
            }
            break;

            case R.id.unLeaseTextView: {
                this.requestUnLeaseList();

                leasedTextView.setSelected(false);
                unLeaseTextView.setSelected(true);
                leasedLayout.setVisibility(View.GONE);
                unLeaseLayout.setVisibility(View.VISIBLE);
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
