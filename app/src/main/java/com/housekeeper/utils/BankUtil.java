package com.housekeeper.utils;

import android.text.TextUtils;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.JSONRequest;
import com.housekeeper.model.BankEntityEx;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.List;

/**
 * 得到所有的银行信息
 *
 * @author sth
 */
public class BankUtil {

    private static boolean hasGetBankList = false;

    private static ArrayList<BankEntityEx> bankList = null;

    public static ArrayList<BankEntityEx> getBankList(BaseActivity context) {
        if (bankList == null) {
            parseBankList(); // 初始化
        }

        if (!hasGetBankList) {
            requestSupportBankList(context);
        }

        if (bankList == null || bankList.size() == 0) {
            parseBankList();
            requestSupportBankList(context);
        }

        return bankList;
    }

    public static BankEntityEx getBankFromCode(String code, BaseActivity context) {
        List<BankEntityEx> list = getBankList(context);
        if (list == null) {
            return null;
        }

        for (BankEntityEx model : list) {
            if (TextUtils.equals(model.getCode(), code)) {
                return model;
            }
        }

        return null;
    }

    private static void requestSupportBankList(BaseActivity context) {
        JSONRequest request = new JSONRequest(context, RequestEnum.BANKS, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                ActivityUtil.getSharedPreferences().edit().putString(BankUtil.kBANKLIST, jsonObject).commit();
                hasGetBankList = true;

                parseBankList();
            }
        });

        context.addToRequestQueue(request, null);
    }

    private static void parseBankList() {
        String str = ActivityUtil.getSharedPreferences().getString(kBANKLIST, DEFAULT_BANK_LIST);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, BankEntityEx.class);
            JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, javaType);
            AppMessageDto<List<BankEntityEx>> dto = objectMapper.readValue(str, type);

            if (dto.getStatus() == AppResponseStatus.SUCCESS) {
                bankList = (ArrayList<BankEntityEx>) dto.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String kBANKLIST = "kBANKLIST";

    public static String DEFAULT_BANK_LIST =
            "[{\"single\":50000,\"day\":50000,\"month\":0,\"name\":\"工商银行\",\"code\":\"ICBC\",\"img\":\"bank_4\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"农业银行\",\"code\":\"ABC\",\"img\":\"bank_2\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"招商银行\",\"code\":\"CMB\",\"img\":\"bank_6\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"建设银行\",\"code\":\"CCB\",\"img\":\"bank_3\"}," +
                    "{\"single\":50000,\"day\":500000,\"month\":0,\"name\":\"中国银行\",\"code\":\"BOC\",\"img\":\"bank_1\"}," +
                    "{\"single\":5000,\"day\":5000,\"month\":0,\"name\":\"民生银行\",\"code\":\"CMBC\",\"img\":\"bank_12\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"浦发银行\",\"code\":\"SPDB\",\"img\":\"bank_7\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"光大银行\",\"code\":\"CEB\",\"img\":\"bank_10\"}," +
                    "{\"single\":50000,\"day\":1000000,\"month\":0,\"name\":\"兴业银行\",\"code\":\"CIB\",\"img\":\"bank_11\"}]";

}
