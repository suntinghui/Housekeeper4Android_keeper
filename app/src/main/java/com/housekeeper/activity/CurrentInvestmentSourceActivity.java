package com.housekeeper.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.DebtSourceListAppDto;
import com.ares.house.dto.app.Paginable;
import com.housekeeper.activity.view.CustomNetworkImageView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/4/15.
 */
public class CurrentInvestmentSourceActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private ListView listView;

    private SwipeRefreshLayout mSwipeLayout = null;

    private List<DebtSourceListAppDto> mList = new ArrayList<DebtSourceListAppDto>();
    private Adapter adapter = null;

    private int pageNo = 1;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_investment_source);

        initView();

        this.requesBuyList("正在请求数据...");
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("投资去向");

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

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

        this.requesBuyList(null);
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

        this.requesBuyList(null);
    }

    private void requesBuyList(String msg) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("id", this.getIntent().getStringExtra("id"));
        tempMap.put("pageNo", pageNo + "");
        tempMap.put("pageSize", Constants.PAGESIZE + "");

        JSONRequest request = new JSONRequest(this, RequestEnum.DEBTPACKAGE_SOURCE_LIST, tempMap, false, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, DebtSourceListAppDto.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

                AppMessageDto<Paginable<DebtSourceListAppDto>> dto = null;
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

                        ActivityUtil.setEmptyView(CurrentInvestmentSourceActivity.this, listView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requesBuyList("正在请求数据...");
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
        private RelativeLayout contentLayout;
        private CustomNetworkImageView logoImageView;
        private TextView numTextView;
        private TextView realNameTextView;
        private TextView orgTextView;
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

                convertView = mInflater.inflate(R.layout.layout_current_investment_source, null);

                holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
                holder.logoImageView = (CustomNetworkImageView) convertView.findViewById(R.id.logoImageView);
                holder.numTextView = (TextView) convertView.findViewById(R.id.numTextView);
                holder.realNameTextView = (TextView) convertView.findViewById(R.id.realNameTextView);
                holder.orgTextView = (TextView) convertView.findViewById(R.id.orgTextView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 0) {
                holder.contentLayout.setBackgroundResource(R.drawable.bg_orange_gray);
            } else {
                holder.contentLayout.setBackgroundResource(R.drawable.bg_white_gray);
            }

            final DebtSourceListAppDto infoDto = mList.get(position);

            holder.logoImageView.setDefaultImageResId(R.drawable.fenqi_head_default);
            holder.logoImageView.setErrorImageResId(R.drawable.fenqi_head_default);
            holder.logoImageView.setLocalImageBitmap(R.drawable.fenqi_head_default);
            holder.logoImageView.setImageUrl(infoDto.getLogoImg(), ImageCacheManager.getInstance().getImageLoader());

            holder.numTextView.setText(infoDto.getSourceNum());
            holder.orgTextView.setText(infoDto.getOrganization());

            if (StringUtils.isEmpty(infoDto.getRealName())) {
                holder.realNameTextView.setText(infoDto.getRealName());
            } else {
                holder.realNameTextView.setText(infoDto.getRealName() + "  ");
            }

            holder.contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (infoDto.isNew()) {
                        Intent intent = new Intent(CurrentInvestmentSourceActivity.this, StagingUserActivityEx.class);
                        intent.putExtra("sourceNum", infoDto.getSourceNum());
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(CurrentInvestmentSourceActivity.this, StagingUserActivity.class);
                        intent.putExtra("image", infoDto.getLogoImg());
                        intent.putExtra("realname", infoDto.getRealName());
                        intent.putExtra("org", infoDto.getOrganization());
                        intent.putExtra("credit", infoDto.getCredit());
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;
        }
    }
}
