package com.housekeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ares.house.dto.app.MyHQInfoAppDto;
import com.housekeeper.activity.view.ConfirmPayDialog;
import com.housekeeper.client.Constants;
import com.housekeeper.client.TransferInfo;
import com.housekeeper.utils.AdapterUtil;
import com.housekeeper.utils.MathUtil;
import com.housekeeper.utils.RateUtil;
import com.wufriends.housekeeper.keeper.R;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by sth on 9/2/15.
 * <p/>
 * 活期转入
 */
public class CurrentTransferInActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView balanceTextView = null;
    private EditText moneyEditText = null;
    private TextView todayLimitTextView = null; // 今日可购买金额
    private Button investmentBtn = null;
    private TextView dateTextView = null; // 预计收益时间

    private View popupLocationView = null;
    private PopupWindow tipPopup = null;
    private TextView tipTextView = null;

    private MyHQInfoAppDto infoDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_transfer_in);

        initView();

        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
        titleTextView.setText("转入");

        TextView bankLimitTextView = (TextView) this.findViewById(R.id.bankLimitTextView);
        bankLimitTextView.setOnClickListener(this);

        Button backButton = (Button) this.findViewById(R.id.backBtn);
        backButton.setOnClickListener(this);

        balanceTextView = (TextView) this.findViewById(R.id.balanceTextView);
        moneyEditText = (EditText) this.findViewById(R.id.moneyEditText);
        moneyEditText.addTextChangedListener(this);
        todayLimitTextView = (TextView) this.findViewById(R.id.todayLimitTextView);
        dateTextView = (TextView) this.findViewById(R.id.dateTextView);

        popupLocationView = this.findViewById(R.id.popupLocationView);

        investmentBtn = (Button) this.findViewById(R.id.investmentBtn);
        investmentBtn.setOnClickListener(this);
    }

    private void refreshData() {
        infoDto = (MyHQInfoAppDto) this.getIntent().getSerializableExtra("DTO");
        balanceTextView.setText(infoDto.getUserMoney() + "");
        todayLimitTextView.setText(infoDto.getSurplusMoney() + " 元");
        dateTextView.setText(infoDto.getEarningDayStr());
    }

    private boolean checkValue() {
        try {
            String inputStr = moneyEditText.getText().toString().trim();

            if (StringUtils.isEmpty(inputStr)) {
                Toast.makeText(this, "请输入投资金额", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Double.parseDouble(inputStr) < infoDto.getMinBuy()) {
                Toast.makeText(this, "最少投资金额为" + infoDto.getMinBuy() + "元", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Double.parseDouble(inputStr) > Double.parseDouble(infoDto.getSurplusMoney())) {
                Toast.makeText(this, "今日最大可购买金额" + infoDto.getSurplusMoney() + "元", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.investmentBtn: // 立即投资
                if (checkValue()) {
                    try {
                        ConfirmPayDialog dialog = new ConfirmPayDialog(this);
                        dialog.setTransferInfo(new TransferInfo(infoDto.getDebtId(), Double.parseDouble(moneyEditText.getText().toString().trim()), Double.parseDouble(infoDto.getUserMoney())));
                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();

                        Toast.makeText(this, "系统异常，请重新登录", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.bankLimitTextView: {
                Intent intent = new Intent(this, ShowWebViewActivity.class);
                intent.putExtra("title", "银行限额表");
                intent.putExtra("url", Constants.HOST_IP + "/app/bank.html");
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            this.finish();
        }
    }

    // 弹出本息合计
    private void showPopTip() {
        try {
            if (null == tipPopup) {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.layout_popup_tip, null);
                tipTextView = (TextView) layout.findViewById(R.id.tipTextView);

                tipPopup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tipPopup.setFocusable(false);
                tipPopup.setBackgroundDrawable(new BitmapDrawable());
                tipPopup.setOutsideTouchable(false);
            }

            BigDecimal principal = new BigDecimal(moneyEditText.getText().toString().replace("￥", "").trim());
            BigDecimal yearRate = new BigDecimal(infoDto.getRate());

            tipTextView.setText(this.calcTotalPrincipalAndInterest(principal, yearRate) + " 元");

            int[] location = new int[2];
            popupLocationView.getLocationOnScreen(location);

            tipPopup.showAtLocation(popupLocationView, Gravity.NO_GRAVITY, location[0] + AdapterUtil.dip2px(this, 100), location[1] - AdapterUtil.dip2px(this, 35));

            handler.postDelayed(runnable, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        public void run() {
            tipPopup.dismiss();
            handler.removeCallbacks(this);
        }
    };

    private String calcTotalPrincipalAndInterest(BigDecimal principal, BigDecimal yearRate) {
        return MathUtil.moneyFormatDown(RateUtil.getDayEarnings(MathUtil.add(yearRate), principal)).toString();
    }

    // 监听投资金额改变
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            double money = Double.parseDouble(s.toString().trim());
            if (money < infoDto.getMinBuy())
                return;

            showPopTip();

        } catch (Exception e) {
        }
    }

}
