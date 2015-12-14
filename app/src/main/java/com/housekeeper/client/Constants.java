package com.housekeeper.client;

import com.housekeeper.utils.ActivityUtil;

public class Constants {

    // 类型
    public static final String kCURRENT_TYPE = "kCURRENT_TYPE";

    public static final int SMS_MAX_TIME = 60; // 短信平台重发最大时间 秒
    public static final int INITIAL_DELAY_MILLIS = 175;

    public static final int PAGESIZE = 20;

    // 生产
//    public static final String HOST_IP = "http://www.baggugu.com";
//    public static final String HOST_IP_REQ = HOST_IP + ":8111";

    // 测试
//    public static final String HOST_IP = "http://182.92.217.168:8112";
//    public static final String HOST_IP_REQ = HOST_IP;

    public static final String HOST_IP = "http://192.168.1.106:8716";
    public static final String HOST_IP_REQ = HOST_IP;

    public static final String PROTOCOL_IP = HOST_IP + "/app/agreement.html";

    public static final String PHONE_SERVICE = "01053812098";

    public static final String Base_Token = "Base-Token";
    public static final String SESSIONID = "Cookie";
    public static final String Set_Cookie = "Set-Cookie";

    public static final String DEVICETOKEN = "DEVICETOKEN";

    public static final String FIRST_LANUCH = "FIRST_LANUCH_" + ActivityUtil.getVersionCode();

    public static final String HEAD_RANDOM = "HEAD_RANDOM";

    public static final String UserName = "UserName";
    public static final String Password = "Password";
    public static final String USERID = "USERID";

    public static final String UMengPUSHId = "UMengPUSHId";

    public static final String WX_APP_ID = "wx2e4661c7c5c4b28a";
    public static final String WX_AppSecret = "f12ac04cb345abd1bc9d23a699094a0b";

    public static final String QQ_APP_ID = "1104763238";
    public static final String QQ_APP_KEY = "1gkTiZkUig0Fia01";

    public static boolean NEED_REFRESH_LOGIN = false;

    public static String ACTION_CHECK_TABHOST = "com.housekeeper.check.tabhost";
    public static String ACTION_CHECK_RELATION = "ACTION_CHECK_RELATION";


}
