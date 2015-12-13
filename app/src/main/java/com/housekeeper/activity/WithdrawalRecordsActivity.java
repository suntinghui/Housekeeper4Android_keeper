package com.housekeeper.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.Paginable;
import com.ares.house.dto.app.WithdrawalMoneyAppDto;
import com.housekeeper.activity.view.WithdrawalExAdapter;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.ViewUtil;
import com.idunnololz.widgets.AnimatedExpandableListView;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 提现记录
 * 
 * @author sth
 * 
 */
public class WithdrawalRecordsActivity extends BaseActivity implements OnClickListener, OnLoadListener, OnRefreshListener {

	private TextView countTextView = null;
	private AnimatedExpandableListView listView;
	private WithdrawalExAdapter adapter = null;

	private SwipeRefreshLayout mSwipeLayout = null;

	private List<WithdrawalMoneyAppDto> mList = new ArrayList<WithdrawalMoneyAppDto>();

	private int pageNo = 1;
	private int totalPage = 0;

	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_withdrawal_records);

		initView();

		this.requesTransferHistory("正在请求数据...");
	}

	private void initView() {
		Button backButton = (Button) this.findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);

		((TextView) this.findViewById(R.id.titleTextView)).setText("提现记录");

		initSwipeRefresh();

		countTextView = (TextView) this.findViewById(R.id.countTextView);
		countTextView.setVisibility(View.INVISIBLE);

		adapter = new WithdrawalExAdapter(this);
		
		listView = (AnimatedExpandableListView) this.findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (listView.isGroupExpanded(groupPosition)) {
					listView.collapseGroupWithAnimation(groupPosition);
				} else {
					listView.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}

		});
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

		JSONRequest request = new JSONRequest(this, RequestEnum.WITHDRAWAL_LIST, tempMap, false, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
				JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, WithdrawalMoneyAppDto.class);
				JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

				AppMessageDto<Paginable<WithdrawalMoneyAppDto>> dto = null;
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
						adapter.notifyDataSetChanged();

						ActivityUtil.setEmptyView(WithdrawalRecordsActivity.this, listView).setOnClickListener(new OnClickListener() {
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

	private void showCount(int totalCount) {
		if (totalCount == count)
			return;

		count = totalCount;

		countTextView.setText(count + "项");
		ViewUtil.dropoutView(countTextView, 500);
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
