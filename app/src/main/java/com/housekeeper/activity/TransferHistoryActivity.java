package com.housekeeper.activity;

import org.codehaus.jackson.map.DeserializationConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;

import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.MoneyHistoryAppDto;
import com.ares.house.dto.app.Paginable;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.client.net.ResponseErrorListener;
import com.housekeeper.utils.ActivityUtil;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;
import com.wufriends.housekeeper.keeper.R;

/**
 * 交易记录
 * 
 * @author sth
 * 
 */
public class TransferHistoryActivity extends BaseActivity implements OnClickListener, OnLoadListener, OnRefreshListener {

	private ListView listView;

	private Drawable inDrawable;
	private Drawable outDrawable;

	private SwipeRefreshLayout mSwipeLayout = null;

	private List<MoneyHistoryAppDto> mList = new ArrayList<MoneyHistoryAppDto>();
	private Adapter adapter = null;

	private int pageNo = 1;
	private int totalPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_transfer_history);

		initView();

		this.requesTransferHistory("正在请求数据...");
	}

	private void initView() {
		Button backButton = (Button) this.findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);

		((TextView) this.findViewById(R.id.titleTextView)).setText("交易记录");

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

		JSONRequest request = new JSONRequest(this, RequestEnum.TRANSFER_HISTORY, tempMap, false, new Response.Listener<String>() {

			@Override
			public void onResponse(String jsonObject) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
				JavaType type = objectMapper.getTypeFactory().constructParametricType(Paginable.class, MoneyHistoryAppDto.class);
				JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, type);

				AppMessageDto<Paginable<MoneyHistoryAppDto>> dto = null;
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

						ActivityUtil.setEmptyView(TransferHistoryActivity.this, listView).setOnClickListener(new OnClickListener() {
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
		private LinearLayout contentLayout;
		private TextView transferTypeTextView; // 账户充值
		private TextView transferNumTextView; // 交易流水号
		private TextView amountTextView;
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

				convertView = mInflater.inflate(R.layout.layout_transfer_history, null);

				holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);
				holder.transferTypeTextView = (TextView) convertView.findViewById(R.id.transferTypeTextView);
				holder.transferNumTextView = (TextView) convertView.findViewById(R.id.transferNumTextView);
				holder.amountTextView = (TextView) convertView.findViewById(R.id.amountTextView);
				holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 0) {
				holder.contentLayout.setBackgroundResource(R.drawable.bg_orange_gray);
			} else {
				holder.contentLayout.setBackgroundResource(R.drawable.bg_white_gray);
			}

			MoneyHistoryAppDto dto = mList.get(position);

			holder.transferTypeTextView.setText(dto.getDisplay());
			if (dto.isIncome()) {
				// 收入
				holder.transferTypeTextView.setCompoundDrawables(getDrawable(true), null, null, null);
				holder.amountTextView.setTextColor(getResources().getColor(R.color.redme));
				holder.amountTextView.setText("+" + dto.getMoney() + "元");
			} else {
				holder.transferTypeTextView.setCompoundDrawables(getDrawable(false), null, null, null);
				holder.amountTextView.setTextColor(getResources().getColor(R.color.greenme));
				holder.amountTextView.setText("-" + dto.getMoney() + "元");
			}
			holder.transferNumTextView.setText(dto.getDpnum());
			holder.timeTextView.setText(dto.getTime());

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

	private Drawable getDrawable(boolean in) {
		if (inDrawable == null) {
			inDrawable = getResources().getDrawable(R.drawable.transfer_in);
			inDrawable.setBounds(0, 0, inDrawable.getMinimumWidth(), inDrawable.getMinimumHeight());
		}

		if (outDrawable == null) {
			outDrawable = getResources().getDrawable(R.drawable.transfer_out);
			outDrawable.setBounds(0, 0, outDrawable.getMinimumWidth(), outDrawable.getMinimumHeight());
		}

		if (in) {
			return inDrawable;
		} else {
			return outDrawable;
		}
	}

}
