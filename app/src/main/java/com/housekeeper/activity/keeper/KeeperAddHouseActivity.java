package com.housekeeper.activity.keeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.HouseAddListAppDto;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.ActivityManager;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.umeng.analytics.MobclickAgent;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sth on 9/16/15.
 */
public class KeeperAddHouseActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private Adapter adapter = null;
    private SwipeRefreshLayout mSwipeLayout = null;
    private List<HouseAddListAppDto> mList = new ArrayList<HouseAddListAppDto>();

    private TextView addNewHouseTextView = null;

    private int newHouseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_add_house);

        this.initView();

        this.requestHouseList("正在请求数据...");
    }

    @Override
    protected void onResume() {
        super.onResume();

        newHouseId = -1;
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        this.findViewById(R.id.backBtn).setVisibility(View.INVISIBLE);

        ((TextView) this.findViewById(R.id.titleTextView)).setText("添加房源");

        initSwipeRefresh();

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);

        addNewHouseTextView = (TextView) this.findViewById(R.id.addNewHouseTextView);
        addNewHouseTextView.setOnClickListener(this);
    }

    private void initSwipeRefresh() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColor(R.color.redme, R.color.blueme, R.color.orangeme, R.color.greenme);
        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
        mSwipeLayout.setLoadNoFull(true);
    }

    // 下拉刷新
    @Override
    public void onRefresh() {
        this.requestHouseList(null);
    }

    private void requestHouseList(String msg) {
        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_ADD_LIST, null, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, HouseAddListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<List<HouseAddListAppDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        mList.clear();

                        mList.addAll(dto.getData());

                        adapter.notifyDataSetChanged();

                        ActivityUtil.setEmptyView(KeeperAddHouseActivity.this, listView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestHouseList("正在请求数据...");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
                }

            }
        }, new ResponseErrorListener(this) {

            @Override
            public void todo() {
                mSwipeLayout.setRefreshing(false);
            }
        });

        if (!this.addToRequestQueue(request, msg)) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void requestAddNewHouse(){
        JSONRequest request = new JSONRequest(this, RequestEnum.HOUSE_ADD, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, HouseAddListAppDto.class);
                    AppMessageDto<HouseAddListAppDto> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        Toast.makeText(KeeperAddHouseActivity.this, "成功添加新房源，请完善数据", Toast.LENGTH_SHORT).show();

                        newHouseId = dto.getData().getId();

                        requestHouseList("正在请求数据...");

                    } else {
                        Toast.makeText(KeeperAddHouseActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, "正在请求数据...");
    }

    private class ViewHolder {
        private LinearLayout rootLayout;
        private LinearLayout infoLayout;
        private LinearLayout addLayout;

        private CustomNetworkImageView headImageView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView begingDateTextView;
        private TextView endDateTextView;
        private TextView yearMoneyTextView;
    }

    public class Adapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public Adapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (null == convertView) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.item_keeper_add_house, null);

                holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.rootLayout);
                holder.infoLayout = (LinearLayout) convertView.findViewById(R.id.infoLayout);
                holder.addLayout = (LinearLayout) convertView.findViewById(R.id.addLayout);

                holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
                holder.addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
                holder.cityTextView = (TextView) convertView.findViewById(R.id.cityTextView);
                holder.begingDateTextView = (TextView) convertView.findViewById(R.id.begingDateTextView);
                holder.endDateTextView = (TextView) convertView.findViewById(R.id.endDateTextView);
                holder.yearMoneyTextView = (TextView) convertView.findViewById(R.id.yearMoneyTextView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final HouseAddListAppDto infoDto = mList.get(position);

            holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
            holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
            holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
            holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

            holder.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
            holder.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
            holder.begingDateTextView.setText(infoDto.getBeginTimeStr());
            holder.endDateTextView.setText(infoDto.getEndTimeStr());
            holder.yearMoneyTextView.setText(infoDto.getYearMoney() + " 元");

            if (infoDto.getId() == newHouseId) {
                YoYo.with(Techniques.BounceIn).duration(500).playOn(holder.rootLayout);
            }

            holder.infoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(KeeperAddHouseActivity.this, KeeperAddHouseListActivity.class);
                    intent.putExtra("DTO", infoDto);
                    KeeperAddHouseActivity.this.startActivityForResult(intent, 0);
                }
            });

            holder.addLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (infoDto.isAgentComplete() && infoDto.isRentalComplete() && infoDto.isEstateComplete() && infoDto.isInfoComplete()) {
                        Intent intent = new Intent(KeeperAddHouseActivity.this, KeeperAddHouseRelationActivity.class);
                        intent.putExtra("DTO", infoDto);
                        KeeperAddHouseActivity.this.startActivityForResult(intent, 1);
                    } else {

                        Toast.makeText(KeeperAddHouseActivity.this, "请先完善房源信息后再关联房东", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestHouseList(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.addNewHouseTextView:
                requestAddNewHouse();
                break;
        }
    }
}
