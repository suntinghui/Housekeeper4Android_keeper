package com.housekeeper.activity.keeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.ares.house.dto.app.AgentLeaseHouseListDto;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.MoneyHistoryAppDto;
import com.ares.house.dto.app.Paginable;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sth on 10/4/15.
 * <p>
 * <p>
 * 收租记录
 */
public class KeeperRentRecordListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private SwipeRefreshLayout mSwipeLayout = null;

    private List<AgentLeaseHouseListDto> mList = new ArrayList<AgentLeaseHouseListDto>();
    private Adapter adapter = null;

    private int pageNo = 1;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_keeper_rentrecord_list);

        this.initView();

        this.requesTransferHistory("正在请求数据...");
    }

    private void initView() {
        this.findViewById(R.id.backBtn).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.titleTextView)).setText("收租记录");

        initSwipeRefresh();

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
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

    // 下拉刷新
    @Override
    public void onRefresh() {
        pageNo = 1;
        totalPage = 0;

        this.requesTransferHistory(null);
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

        this.requesTransferHistory(null);
    }

    private void requesTransferHistory(String msg) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.LEASE_AGENT_HOUSE, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, AgentLeaseHouseListDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<AgentLeaseHouseListDto>> dto = null;
                try {
                    dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        totalPage = dto.getData().getTotalPage();
                        pageNo = dto.getData().getPageNo();

                        if (pageNo == 1) {
                            mList.clear();
                        }

                        mList.addAll(dto.getData().getList());
                        adapter.notifyDataSetChanged();

                        ActivityUtil.setEmptyView(KeeperRentRecordListActivity.this, listView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requesTransferHistory("正在请求数据...");
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

    private class ViewHolder {
        private LinearLayout infoLayout;
        private CustomNetworkImageView headImageView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView begingDateTextView;
        private TextView endDateTextView;
        private TextView agentBegingDateTextView;
        private TextView agentEndDateTextView;

        private TextView monthTextView;
        private TextView monthMoneyTextView;
        private TextView commissionTextView;
        private TextView timeTextView;
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

                convertView = mInflater.inflate(R.layout.item_keeper_rent_record, null);

                holder.infoLayout = (LinearLayout) convertView.findViewById(R.id.infoLayout);

                holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
                holder.addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
                holder.cityTextView = (TextView) convertView.findViewById(R.id.cityTextView);
                holder.begingDateTextView = (TextView) convertView.findViewById(R.id.begingDateTextView);
                holder.endDateTextView = (TextView) convertView.findViewById(R.id.endDateTextView);
                holder.agentBegingDateTextView = (TextView) convertView.findViewById(R.id.agentBegingDateTextView);
                holder.agentEndDateTextView = (TextView) convertView.findViewById(R.id.agentEndDateTextView);

                holder.monthTextView = (TextView) convertView.findViewById(R.id.monthTextView);
                holder.monthMoneyTextView = (TextView) convertView.findViewById(R.id.monthMoneyTextView);
                holder.commissionTextView = (TextView) convertView.findViewById(R.id.commissionTextView);
                holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AgentLeaseHouseListDto infoDto = mList.get(position);

            holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
            holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
            holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
            holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImg(), ImageCacheManager.getInstance().getImageLoader());

            holder.addressTextView.setText(infoDto.getCommunity() + " " + infoDto.getHouseNum());
            holder.cityTextView.setText(infoDto.getCityStr() + " " + infoDto.getAreaStr() + " " + infoDto.getAddress());
            holder.begingDateTextView.setText(infoDto.getBeginTimeStr());
            holder.endDateTextView.setText(infoDto.getEndTimeStr());
            holder.agentBegingDateTextView.setText(infoDto.getAgentBeginTimeStr());
            holder.agentEndDateTextView.setText(infoDto.getAgentEndTimeStr());

            holder.monthTextView.setText(infoDto.getMonth() + "/" + infoDto.getTotalMonth());
            holder.monthMoneyTextView.setText("月租金：" + infoDto.getMonthMoney() + "元");
            holder.commissionTextView.setText("佣金：" + infoDto.getCommission() + "元");
            holder.timeTextView.setText(infoDto.getTimeStr());

            holder.infoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(KeeperRentRecordListActivity.this, KeeperRentRecordDetailActivity.class);
                    intent.putExtra("leaseId", infoDto.getLeaseId() + "");
                    KeeperRentRecordListActivity.this.startActivityForResult(intent, 0);
                }
            });

            return convertView;
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
