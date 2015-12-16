package com.housekeeper.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.MyHQInfoAppDto;
import com.ares.house.dto.app.RateCompareAppDto;
import com.dacer.androidcharts.LineView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gelitenight.waveview.library.WaveHelper;
import com.gelitenight.waveview.library.WaveView;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.CurrentInvestmentSourceActivity;
import com.housekeeper.activity.CurrentTransferInActivity;
import com.housekeeper.activity.CurrentTransferOutActivity;
import com.housekeeper.activity.InvestmentActivity;
import com.housekeeper.activity.ShowWebViewActivity;
import com.housekeeper.activity.YesterdayEarningsActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.utils.ActivityUtil;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sth on 10/14/15.
 */
public class InvestmentCurrentLayout extends LinearLayout implements View.OnClickListener {

    private List<String> messageList = Arrays.asList(new String[]{"租房金融", "购房首付贷"});

    private BaseActivity context;

    private TextView totalEarningsTextView; // 累计收益
    private TextView buyMoneyTextView; // 总投资额
    private TextView yesterdayEarningsTextView; // 昨天收益
    private LinearLayout investmentWhereLayout; // 投资去向
    private LinearLayout yesterdayEarningsLayout; // 昨日收益
    private CircleProgress circleProgress = null;
    private WaveView waveView = null;
    private TextView rateTextView = null; // 利息
    private TextView minBuyTextView = null; // 起投金额
    private Button investmentBtn = null;
    private Button transferOutBtn = null;

    private TextView messageTextView;
    private LineView lineView;

    private MyHQInfoAppDto infoDto;

    private int maxCount = 7;

    private WaveHelper mWaveHelper;

    private TextView downloadTextView;

    public InvestmentCurrentLayout(BaseActivity context) {
        super(context);

        this.initView(context);
    }

    public InvestmentCurrentLayout(InvestmentActivity context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }


    private void initView(BaseActivity context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_investment_current, this);

        totalEarningsTextView = (TextView) this.findViewById(R.id.totalEarningsTextView);
        buyMoneyTextView = (TextView) this.findViewById(R.id.buyMoneyTextView);
        yesterdayEarningsTextView = (TextView) this.findViewById(R.id.yesterdayEarningsTextView);
        rateTextView = (TextView) this.findViewById(R.id.rateTextView);
        minBuyTextView = (TextView) this.findViewById(R.id.minBuyTextView);

        messageTextView = (TextView) this.findViewById(R.id.messageTextView);
        timer.schedule(task, 0, 2500);

        circleProgress = (CircleProgress) this.findViewById(R.id.circleProgress);
        circleProgress.setType(CircleProgress.ARC);
        circleProgress.setPaintWidth(10);
        circleProgress.setSubPaintColor(Color.parseColor("#1caff6"));
        circleProgress.setBottomPaintColor(Color.parseColor("#D1F2FE"));

        waveView = (WaveView) this.findViewById(R.id.waveView);
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setBorder(1, Color.parseColor("#881caff6"));

        investmentWhereLayout = (LinearLayout) this.findViewById(R.id.investmentWhereLayout);
        investmentWhereLayout.setOnClickListener(this);

        yesterdayEarningsLayout = (LinearLayout) this.findViewById(R.id.yesterdayEarningsLayout);
        yesterdayEarningsLayout.setOnClickListener(this);

        investmentBtn = (Button) this.findViewById(R.id.investmentBtn);
        investmentBtn.setOnClickListener(this);

        transferOutBtn = (Button) this.findViewById(R.id.transferOutBtn);
        transferOutBtn.setOnClickListener(this);

        downloadTextView = (TextView) this.findViewById(R.id.downloadTextView);
        downloadTextView.setOnClickListener(this);
        if (ActivityUtil.isPackageExists(context, "com.wufriends.gugu")) {
            downloadTextView.setText("打开");
        } else {
            downloadTextView.setText("下载");
        }

        lineView = (LineView) findViewById(R.id.line_view);
        lineView.setDrawDotLine(true); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_LAST); //optional
    }

    private void setLineViewData(LineView lineView) {
        lineView.setPreTipList(new ArrayList<String>(Arrays.asList(new String[]{"鼓鼓活期:", "余额宝:"})));
        lineView.setTailTipList(new ArrayList<String>(Arrays.asList(new String[]{"%  ", "%"})));

        ArrayList<Integer> dataList = new ArrayList<Integer>();
        for (RateCompareAppDto dto : this.infoDto.getRateCompare1()) {
            dataList.add((int) (Float.parseFloat(dto.getValue()) * 100));
        }

        ArrayList<Integer> dataList2 = new ArrayList<Integer>();
        for (RateCompareAppDto dto : this.infoDto.getRateCompare2()) {
            dataList2.add((int) (Float.parseFloat(dto.getValue()) * 100));
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
        dataLists.add(dataList);
        dataLists.add(dataList2);

        lineView.setDataList(dataLists);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.investmentBtn: {
                if (infoDto == null)
                    return;

                Intent intent = new Intent(context, CurrentTransferInActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivityForResult(intent, 0);
            }
            break;

            case R.id.transferOutBtn: {
                if (infoDto == null)
                    return;

                Intent intent = new Intent(context, CurrentTransferOutActivity.class);
                intent.putExtra("DTO", infoDto);
                context.startActivityForResult(intent, 0);
            }
            break;

            case R.id.downloadTextView: {
                if (ActivityUtil.isPackageExists(context, "com.wufriends.gugu")) {
                    // 打开
                    Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.wufriends.gugu");
                    context.startActivity(LaunchIntent);
                } else {
                    // 下载
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.wufriends.gugu");
                    intent.setData(content_url);
                    context.startActivity(intent);
                }
            }
            break;

            case R.id.yesterdayEarningsLayout: {
                Intent intent = new Intent(context, YesterdayEarningsActivity.class);
                context.startActivity(intent);
            }
            break;

            case R.id.investmentWhereLayout: {
                if (infoDto == null)
                    return;

                if (Double.parseDouble(infoDto.getBuyMoney()) > 0) {
                    Intent intent = new Intent(context, CurrentInvestmentSourceActivity.class);
                    intent.putExtra("id", infoDto.getDebtId() + "");
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, ShowWebViewActivity.class);
                    intent.putExtra("title", "投资去向");
                    intent.putExtra("url", infoDto.getHintUrl());
                    context.startActivity(intent);
                }
            }
            break;
        }
    }

    public void initData() {
        if (infoDto == null) {
            this.requestHQInfo("正在请求数据...");
        }
    }

    // 我的活期理财详情
    public void requestHQInfo(String msg) {
        JSONRequest request = new JSONRequest(context, RequestEnum.USER_HQ_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, MyHQInfoAppDto.class);
                    AppMessageDto<MyHQInfoAppDto> dto = objectMapper.readValue(jsonObject, javaType);
                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        infoDto = dto.getData();

                        responseHQInfo(infoDto);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, msg);
    }

    private void responseHQInfo(MyHQInfoAppDto dto) {
        totalEarningsTextView.setText(dto.getTotalEarnings() + "");
        buyMoneyTextView.setText(dto.getBuyMoney() + "");
        yesterdayEarningsTextView.setText(dto.getYesterdayEarnings() + "");
        minBuyTextView.setText(dto.getMinBuy() + "元");

        // 设置进度
        try {
            int progress = 100 - (int) (100 * Double.parseDouble(dto.getSurplusMoney()) / Double.parseDouble(dto.getTotalMoney()));
            setCircleProgress(progress);

            waveView.setWaterLevelRatio(progress / 100.0f);
            waveView.setWaveColor(Color.parseColor("#661caff6"), Color.parseColor("#ee1caff6"));
            mWaveHelper = new WaveHelper(waveView);
            mWaveHelper.start();
        } catch (Exception e) {
            e.printStackTrace();

            setCircleProgress(0);
        }

        rateTextView.setText(dto.getRate());

        maxCount = this.infoDto.getRateCompare1().size();


        // LineView
        ArrayList<String> bottomTextList = new ArrayList<String>();
        for (int i = 0; i < maxCount; i++) {
            bottomTextList.add(this.infoDto.getRateCompare1().get(i).getDate());
        }

        lineView.setBottomTextList(bottomTextList);
        this.setLineViewData(lineView);
    }

    public void setCircleProgress(final int progress) {

        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                for (int i = 0; i <= 100; i++) {
                    if (i > progress)
                        break;

                    publishProgress(i);

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

                circleProgress.setmSubCurProgress(values[0]);
            }
        }.execute(0);
    }

    ////////////////////////////////////////////////////
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    int i = 0;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    messageTextView.setText(messageList.get(i % messageList.size()));

                    in();
                    out();

                    i++;
                }

                break;
            }
            super.handleMessage(msg);
        }
    };

    private void in() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageTextView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp).duration(500).playOn(messageTextView);
            }
        }, 0);
    }

    private void out() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                YoYo.with(Techniques.SlideOutUp).duration(500).playOn(messageTextView);
            }
        }, 2200);
    }
}
