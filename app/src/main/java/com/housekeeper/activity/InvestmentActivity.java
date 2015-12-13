package com.housekeeper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.DebtPackageListCountDownAppDto;
import com.housekeeper.activity.view.InvestmentCurrentLayout;
import com.housekeeper.activity.view.InvestmentScheduledLayout;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.housekeeper.utils.DateUtil;
import com.housekeeper.utils.StringUtil;
import com.wufriends.housekeeper.keeper.R;

/**
 * 投资理财
 *
 * @author sth
 */
public class InvestmentActivity extends BaseActivity implements OnClickListener {

    public static final int TYPE_HQ = 0x100;
    public static final int TYPE_DQ = 0x101;

    private static int type = TYPE_HQ;

    private TextView refreshTextView; // 刷新

    private TextView rushVoteTextView = null; // 抢投
    private TextView scheduledVoteTextView = null; // 定投

    // 提醒
    private LinearLayout remindLayout = null;

    private RelativeLayout hqRemindLayout = null;
    private TextView hqCountTextView = null;
    private TextView hqAmountTextView = null;

    private RelativeLayout scheduledRemindLayout = null; // 定投提醒
    private TextView scheduledRemindTimeTextView = null; // 定投时间
    private Button scheduledReminBtn = null;

    // 内容Layout
    private LinearLayout contentLayout = null;
    private InvestmentCurrentLayout rushLayout = null;
    private InvestmentScheduledLayout scheduledLayout = null;

    public static final String ACTION_CHANNEL = "com.gugu.channel";
    private ChannelReceiver channelReceiver = null;

    private Timer timer = null;

    // 启动线程后，直接从－1开始减。
    private long rushSecond = -1;
    private long scheduledSecond = -1;
    private boolean theActivityIsShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_investment);

        type = this.getIntent().getIntExtra("type", type);

        initView();

        this.setSelectChannel();

        this.registerChannelReceiver();

        timer = new Timer();
        timer.schedule(timingTask, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(this.channelReceiver);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void onResume() {
        super.onResume();

        theActivityIsShow = true;

        refreshData("正在请求数据...");
    }

    public void onPause() {
        super.onPause();

        theActivityIsShow = false;
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        if (this.type == TYPE_HQ) {
            titleTextView.setText("活期理财");
        } else {
            titleTextView.setText("定期理财");
        }

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        refreshTextView = (TextView) this.findViewById(R.id.refreshTextView);
        refreshTextView.setOnClickListener(this);
        refreshTextView.setVisibility(View.INVISIBLE);

        rushVoteTextView = (TextView) this.findViewById(R.id.rushVoteTextView);
        rushVoteTextView.setOnClickListener(this);
        rushVoteTextView.setSelected(true);

        scheduledVoteTextView = (TextView) this.findViewById(R.id.scheduledVoteTextView);
        scheduledVoteTextView.setOnClickListener(this);

        // 提醒
        remindLayout = (LinearLayout) this.findViewById(R.id.remindLayout);
        if (this.type == TYPE_HQ) {
            this.findViewById(R.id.topLayout).setVisibility(View.GONE);
        } else {
            this.findViewById(R.id.topLayout).setVisibility(View.VISIBLE);
        }

        hqRemindLayout = (RelativeLayout) this.findViewById(R.id.hqRemindLayout);
        hqCountTextView = (TextView) this.findViewById(R.id.hqCountTextView);
        hqAmountTextView = (TextView) this.findViewById(R.id.hqAmountTextView);

        scheduledRemindLayout = (RelativeLayout) this.findViewById(R.id.scheduledRemindLayout);
        scheduledRemindTimeTextView = (TextView) this.findViewById(R.id.scheduledRemindTimeTextView);
        scheduledReminBtn = (Button) this.findViewById(R.id.scheduledReminBtn);
        scheduledReminBtn.setOnClickListener(this);

        // 提醒end

        contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);

        rushLayout = new InvestmentCurrentLayout(this);
        contentLayout.addView(rushLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        rushLayout.setVisibility(View.VISIBLE);

        scheduledLayout = new InvestmentScheduledLayout(this);
        contentLayout.addView(scheduledLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        scheduledLayout.setVisibility(View.GONE);

        // 设置提醒我的字体
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Helvetica Neue UltraLight.ttf");
        scheduledRemindTimeTextView.setTypeface(typeFace);
    }

    private void setSelectChannel() {
        if (type == TYPE_HQ) {
            rushVoteTextView.setSelected(true);
            scheduledVoteTextView.setSelected(false);

            rushLayout.setVisibility(View.VISIBLE);
            scheduledLayout.setVisibility(View.GONE);

            hqRemindLayout.setVisibility(View.VISIBLE);
            scheduledRemindLayout.setVisibility(View.GONE);
            remindLayout.invalidate();

        } else if (type == TYPE_DQ) {
            rushVoteTextView.setSelected(false);
            scheduledVoteTextView.setSelected(true);

            rushLayout.setVisibility(View.GONE);
            scheduledLayout.setVisibility(View.VISIBLE);

            hqRemindLayout.setVisibility(View.GONE);
            scheduledRemindLayout.setVisibility(View.VISIBLE);
            remindLayout.invalidate();

            scheduledLayout.initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.refreshTextView: {
                this.refreshData("正在请求数据...");
            }
            break;

            case R.id.rushVoteTextView: {
                type = TYPE_HQ;

                this.setSelectChannel();
            }
            break;

            case R.id.scheduledReminBtn:
                //requestAddRemin("DT");
                break;

            case R.id.scheduledVoteTextView: {
                type = TYPE_DQ;
                this.setSelectChannel();
            }
            break;
        }

    }

    private void refreshData(String msg) {
        if (rushLayout.getVisibility() == View.VISIBLE) {
            //requestCountDown("HQ");
            rushLayout.requestHQInfo(msg);
        } else if (scheduledLayout.getVisibility() == View.VISIBLE) {
            //requestCountDown("DT");
            scheduledLayout.refreshData(msg);
        }
    }

    // 取得倒计时数据
    private void requestCountDown(final String typeStr) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("telphone", ActivityUtil.getSharedPreferences().getString(Constants.UserName, ""));
        tempMap.put("type", typeStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.DEBTPACKAGE_COUNTDOWN, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, DebtPackageListCountDownAppDto.class);
                    AppMessageDto<DebtPackageListCountDownAppDto> dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        responseCountDown(typeStr, dto.getData());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
    }

    private void responseCountDown(String type, DebtPackageListCountDownAppDto dto) {
        // 为了能使刷新的时候保证服务器能准备好，所以将时间调慢5s

        // 类型 a定投 b抢投 c转让
        if (dto.getType() == 'b') { // 抢投


        } else if (dto.getType() == 'a') { // 定投
            scheduledSecond = dto.getSecond();

            if (scheduledSecond <= 0) {
                scheduledReminBtn.setVisibility(View.GONE);
                return;
            }

            scheduledReminBtn.setVisibility(View.VISIBLE);
            if (scheduledSecond <= 3 * 60) {
                scheduledReminBtn.setText("即将开始");
                scheduledReminBtn.setEnabled(false);
                return;
            }

            if (dto.isAdd()) {
                scheduledReminBtn.setText("已添加提醒");
                scheduledReminBtn.setEnabled(false);
            } else {
                scheduledReminBtn.setText("提醒我");
                scheduledReminBtn.setEnabled(true);
            }
        } else if (type.equals("HQ")) {
            hqCountTextView.setText("已经为" + dto.getCompleteCount() + "人完成优质债权投资");
            hqAmountTextView.setText(StringUtil.formatAmount(Double.parseDouble(dto.getCompleteMoney())));
        }
    }

    // 添加提醒
    private void requestAddRemin(final String typeStr) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", typeStr);

        JSONRequest request = new JSONRequest(this, RequestEnum.DEBTPACKAGE_REMINDME, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, javaType);

                    Toast.makeText(InvestmentActivity.this, dto.getMsg(), Toast.LENGTH_SHORT).show();

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        responseRemind(typeStr);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.addToRequestQueue(request, null);
    }

    // 添加提醒成功
    private void responseRemind(String type) {
        if (type.equals("QT")) {


        } else if (type.equals("DT")) {
            scheduledReminBtn.setText("已添加提醒");
            scheduledReminBtn.setEnabled(false);
        }
    }

    // 定位channel
    private void registerChannelReceiver() {
        channelReceiver = new ChannelReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHANNEL);
        this.registerReceiver(channelReceiver, filter);
    }

    public class ChannelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            type = intent.getIntExtra("TYPE", TYPE_HQ);
            setSelectChannel();
        }
    }

    public static void setDefaultType(int defaultType) {
        type = defaultType;
    }

    Handler timingHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    rushSecond--;
                    scheduledSecond--;

                    // 如果等于0，则自动刷新
                    if (rushSecond == 0) {
                        rushLayout.requestHQInfo("正在请求数据...");
                    }

                    if (scheduledSecond == 0) {
                        scheduledLayout.refreshData("正在请求数据...");
                    }

                    if (theActivityIsShow) {
                        scheduledRemindTimeTextView.setText(DateUtil.second2Time(scheduledSecond));
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    };

    TimerTask timingTask = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            timingHandler.sendMessage(message);
        }
    };

}
