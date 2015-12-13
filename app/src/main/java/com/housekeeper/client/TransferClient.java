package com.housekeeper.client;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.BindingBankActivity;
import com.housekeeper.activity.NotFirstPayActivity;
import com.housekeeper.activity.PaySuccessActivity;
import com.housekeeper.activity.SetTransferPWDActivity;
import com.housekeeper.activity.view.PayUseBalanceDialog;
import com.housekeeper.client.net.JSONRequest;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 无论是抢投、定投、转让的付款都走此流程。
 * <p/>
 * 首先判断是否有设置交易密码，如果没有设置则首先设置交易密码。设置成功后重新走付款流程。
 * <p/>
 * 一个原则，如果只用余额就能完成支付，则用交易密码做为安全凭证。如果需要银行卡支付则用手机验证码做为安全凭证，不再验证交易密码，两者二选其一。
 *
 * @author sth
 */
public class TransferClient {

    private static TransferClient instance = null;

    private BaseActivity context = null;
    private TransferInfo transferInfo = null;

    public static TransferClient getInstance() {
        if (instance == null) {
            instance = new TransferClient();
        }

        return instance;
    }

    /**
     * 注意债权包标识
     *
     * @param activity
     * @param info
     */
    public void transfer(BaseActivity activity, TransferInfo info) {
        this.context = activity;
        this.transferInfo = info;

        // 首先判断交易密码
        requestBankInfo();
    }

    private void requestBankInfo() {
        JSONRequest request = new JSONRequest(context, RequestEnum.SECURITY_CENTER_BANK_INFO, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Map.class);
                    AppMessageDto<Map<String, String>> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        HashMap<String, String> map = (HashMap<String, String>) dto.getData();
                        responseBankInfo(map);

                    } else {
                        Toast.makeText(context, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, "正在处理请稍候...");
    }

    private void responseBankInfo(HashMap<String, String> map) {
        String TRANSACTION_PASSWORD = map.get("TRANSACTION_PASSWORD");
        if (TRANSACTION_PASSWORD == null) {
            // 没有设置交易密码，去设置。设置成功后，需要再手动触发一下。
            Intent intent = new Intent(context, SetTransferPWDActivity.class);
            intent.putExtra("TYPE", SetTransferPWDActivity.TYPE_SET);
            context.startActivity(intent);

        } else {
            // 判断是否需要使用银行卡支付
            if (transferInfo.shouldUserBankCard()) {
                // 如果需要使用银行卡支付，首先判断是否已经绑定了银行卡信息。
                String bankId = map.get("BANK_ID");
                if (bankId == null || TextUtils.isEmpty(bankId) || TextUtils.equals(bankId, "null")) {
                    // 没有绑定
                    Intent intent = new Intent(context, BindingBankActivity.class);
                    context.startActivity(intent);

                } else {
                    // 已经绑定
                    Intent intent = new Intent(context, NotFirstPayActivity.class);
                    intent.putExtra("INFO", transferInfo);
                    intent.putExtra("MAP", map);
                    context.startActivityForResult(intent, 0);
                }

            } else {
                // 如果不需要银行卡支付，说明余额足够，直接交易密码验证通过后使用余额支付。
                final PayUseBalanceDialog dialog = new PayUseBalanceDialog(context);
                dialog.setTotalMoney(transferInfo.getTransferMoney());
                dialog.setBalanceMoney(transferInfo.getBalanceMoney());
                dialog.setOnConfirmListener(new PayUseBalanceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String pwdStr) {
                        requestPayUseBalance(dialog, pwdStr);
                    }
                });
                dialog.show();
            }
        }
    }

    private void requestPayUseBalance(final PayUseBalanceDialog dialog, String pwd) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("houseId", transferInfo.getId());
        // 使用余额支付，说明余额大于或是等于投资金额
        //tempMap.put("money", transferInfo.getTransferMoney());
        //tempMap.put("surplus", "true");
        tempMap.put("password", pwd);

        JSONRequest request = new JSONRequest(context, RequestEnum.LEASE_PAY_RENT_SURPLUS, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, Integer.class);
                    AppMessageDto<Integer> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                        if (dto.getData() == 0 || dto.getData() == -1) { // 等于0说明直接使⽤用余额购买成功, -1不使用摇一摇
                            Intent intent = new Intent(context, PaySuccessActivity.class);
                            intent.putExtra("TYPE", PaySuccessActivity.TYPE_BALANCE);
                            intent.putExtra("INFO", transferInfo);
                            intent.putExtra("SHAKE", "" + dto.getData());
                            context.startActivityForResult(intent, 0);

                            dialog.dismiss();

                        } else {
                            Toast.makeText(context, dto.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        dialog.setError(dto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, "正在请求数据...");
    }

}
