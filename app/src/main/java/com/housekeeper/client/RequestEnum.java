package com.housekeeper.client;

import java.util.HashMap;

import com.housekeeper.client.net.RequestModel;

public class RequestEnum {

    private static HashMap<String, RequestModel> requestMap = null;

    public static RequestModel getRequest(String id) {
        if (null == requestMap) {
            requestMap = new HashMap<String, RequestModel>();

            requestMap.put(USER_REGIST_CHECK_TELPHONE, new RequestModel(USER_REGIST_CHECK_TELPHONE, Constants.HOST_IP_REQ + "/rpc/user/regist/check/telphone.app"));
            requestMap.put(USER_REGIST_SMS_SEND, new RequestModel(USER_REGIST_SMS_SEND, Constants.HOST_IP_REQ + "/rpc/user/regist/sms/send.app"));
            requestMap.put(USER_REGIST_PASSWORD_SET, new RequestModel(USER_REGIST_PASSWORD_SET, Constants.HOST_IP_REQ + "/rpc/user/regist/password/set.app"));
            requestMap.put(SECURITY_CENTER_TRANSACTION_PASSWORD_SAVE, new RequestModel(SECURITY_CENTER_TRANSACTION_PASSWORD_SAVE, Constants.HOST_IP_REQ + "/rpc/security/center/transaction/password/save.app"));
            requestMap.put(USER_LOGIN, new RequestModel(USER_LOGIN, Constants.HOST_IP_REQ + "/rpc/user/login.app"));
            requestMap.put(USER_VERIFY_TOKEN, new RequestModel(USER_VERIFY_TOKEN, Constants.HOST_IP_REQ + "/rpc/user/verify/token.app"));
            requestMap.put(USER_LOGOUT, new RequestModel(USER_LOGOUT, Constants.HOST_IP_REQ + "/rpc/user/logout.app"));
            requestMap.put(USER_MY, new RequestModel(USER_MY, Constants.HOST_IP_REQ + "/rpc/user/my.app"));
            requestMap.put(HOUSE_ADD_LIST, new RequestModel(HOUSE_ADD_LIST, Constants.HOST_IP_REQ + "/rpc/house/add/list.app"));
            requestMap.put(HOUSE_ADD, new RequestModel(HOUSE_ADD, Constants.HOST_IP_REQ + "/rpc/house/add.app"));
            requestMap.put(HOUSE_ADDINFO, new RequestModel(HOUSE_ADDINFO, Constants.HOST_IP_REQ + "/rpc/house/addinfo.app"));
            requestMap.put(HOUSE_DELETE, new RequestModel(HOUSE_DELETE, Constants.HOST_IP_REQ + "/rpc/house/delete.app"));
            requestMap.put(HOUSE_SETINFO, new RequestModel(HOUSE_SETINFO, Constants.HOST_IP_REQ + "/rpc/house/setinfo.app"));
            requestMap.put(HOUSE_SETINFO, new RequestModel(HOUSE_SETINFO, Constants.HOST_IP_REQ + "/rpc/house/setinfo.app"));
            requestMap.put(HOUSE_SETTRAFFIC, new RequestModel(HOUSE_SETTRAFFIC, Constants.HOST_IP_REQ + "/rpc/house/settraffic.app"));
            requestMap.put(HOUSE_EQUIPMENT, new RequestModel(HOUSE_EQUIPMENT, Constants.HOST_IP_REQ + "/rpc/house/equipment.app"));
            requestMap.put(HOUSE_SETEQUIPMENT, new RequestModel(HOUSE_SETEQUIPMENT, Constants.HOST_IP_REQ + "/rpc/house/setEquipment.app"));
            requestMap.put(HOUSE_SETLANDLORDRENT, new RequestModel(HOUSE_SETLANDLORDRENT, Constants.HOST_IP_REQ + "/rpc/house/setLandlordRent.app"));
            requestMap.put(HOUSE_SETLANDLORDBANKSENDVCODE, new RequestModel(HOUSE_SETLANDLORDBANKSENDVCODE, Constants.HOST_IP_REQ + "/rpc/house/setLandlordBankSendVcode.app"));
            requestMap.put(HOUSE_SETLANDLORDBANK, new RequestModel(HOUSE_SETLANDLORDBANK, Constants.HOST_IP_REQ + "/rpc/house/setLandlordBank.app"));
            requestMap.put(HOUSE_LANDLORDJOIN, new RequestModel(HOUSE_LANDLORDJOIN, Constants.HOST_IP_REQ + "/rpc/house/landlordJoin.app"));
            requestMap.put(HOUSE_AGENTCONFIRMLANDLORDJOIN, new RequestModel(HOUSE_AGENTCONFIRMLANDLORDJOIN, Constants.HOST_IP_REQ + "/rpc/house/agentConfirmLandlordJoin.app"));
            requestMap.put(HOUSE_LANDLORDJOIN_INFO, new RequestModel(HOUSE_LANDLORDJOIN_INFO, Constants.HOST_IP_REQ + "/rpc/house/landlordJoin/info.app"));
            requestMap.put(UPLOAD, new RequestModel(UPLOAD, Constants.HOST_IP_REQ + "/rpc/upload.app"));
            requestMap.put(HOUSE_SETIMG, new RequestModel(HOUSE_SETIMG, Constants.HOST_IP_REQ + "/rpc/house/setimg.app"));
            requestMap.put(HOUSE_IMG_DELETE, new RequestModel(HOUSE_IMG_DELETE, Constants.HOST_IP_REQ + "/rpc/house/img/delete.app"));
            requestMap.put(HOUSE_GETIMG, new RequestModel(HOUSE_GETIMG, Constants.HOST_IP_REQ + "/rpc/house/getimg.app"));
            requestMap.put(HOUSE_INFO, new RequestModel(HOUSE_INFO, Constants.HOST_IP_REQ + "/rpc/house/info.app"));
            requestMap.put(HOUSE_TRAFFIC, new RequestModel(HOUSE_TRAFFIC, Constants.HOST_IP_REQ + "/rpc/house/traffic.app"));
            requestMap.put(HOUSE_LANDLORDRENTALFEE, new RequestModel(HOUSE_LANDLORDRENTALFEE, Constants.HOST_IP_REQ + "/rpc/house/landlordRentalFee.app"));
            requestMap.put(HOUSE_LANDLORD, new RequestModel(HOUSE_LANDLORD, Constants.HOST_IP_REQ + "/rpc/house/landlord.app"));
            requestMap.put(HOUSE_AGENT_USERINFO, new RequestModel(HOUSE_AGENT_USERINFO, Constants.HOST_IP_REQ + "/rpc/house/agent/userinfo.app"));
            requestMap.put(HOUSE_GET_QR, new RequestModel(HOUSE_GET_QR, Constants.HOST_IP_REQ + "/rpc/house/qr/"));
            requestMap.put(BANKS, new RequestModel(BANKS, Constants.HOST_IP_REQ + "/rpc/banks.app"));
            requestMap.put(BANK_AREA, new RequestModel(BANK_AREA, Constants.HOST_IP_REQ + "/rpc/bank/area.app"));
            requestMap.put(SECURITY_CENTER_BANK_INFO, new RequestModel(SECURITY_CENTER_BANK_INFO, Constants.HOST_IP_REQ + "/rpc/security/center/bank/info.app"));
            requestMap.put(SECURITY_CENTER_BANK_BIND_SENDVCODE, new RequestModel(SECURITY_CENTER_BANK_BIND_SENDVCODE, Constants.HOST_IP_REQ + "/rpc/security/center/bank/bind/sendvcode.app"));
            requestMap.put(SECURITY_CENTER_BANK_BIND_PAY, new RequestModel(SECURITY_CENTER_BANK_BIND_PAY, Constants.HOST_IP_REQ + "/rpc/security/center/bank/bind/pay.app"));
            requestMap.put(SECURITY_CENTER_BANK_UPDATE_ADDRESS, new RequestModel(SECURITY_CENTER_BANK_UPDATE_ADDRESS, Constants.HOST_IP_REQ + "/rpc/security/center/bank/update/address.app"));
            requestMap.put(SECURITY_CENTER_BANK_UNBIND, new RequestModel(SECURITY_CENTER_BANK_UNBIND, Constants.HOST_IP_REQ + "/rpc/security/center/bank/unbind.app"));
            requestMap.put(LEASE_SETLEASEIMG, new RequestModel(LEASE_SETLEASEIMG, Constants.HOST_IP_REQ + "/rpc/lease/setLeaseImg.app"));
            requestMap.put(SECURITY_CENTER_EMERGENCY_CONTACT_SAVE, new RequestModel(SECURITY_CENTER_EMERGENCY_CONTACT_SAVE, Constants.HOST_IP_REQ + "/rpc/security/center/emergency/contact/save.app"));
            requestMap.put(SECURITY_CENTER_ITEM_SAVE, new RequestModel(SECURITY_CENTER_ITEM_SAVE, Constants.HOST_IP_REQ + "/rpc/security/center/item/save.app"));
            requestMap.put(USER_SET_LOGO, new RequestModel(USER_SET_LOGO, Constants.HOST_IP_REQ + "/rpc/user/set/logo.app"));
            requestMap.put(SECURITY_CENTER_IMGITEM_SAVE, new RequestModel(SECURITY_CENTER_IMGITEM_SAVE, Constants.HOST_IP_REQ + "/rpc/security/center/imgitem/save.app"));
            requestMap.put(SECURITY_CENTER_ITEM_INFO, new RequestModel(SECURITY_CENTER_ITEM_INFO, Constants.HOST_IP_REQ + "/rpc/security/center/item/info.app"));
            requestMap.put(LEASE_SETIMG, new RequestModel(LEASE_SETIMG, Constants.HOST_IP_REQ + "/rpc/lease/setImg.app"));
            requestMap.put(LEASE_GETIMG, new RequestModel(LEASE_GETIMG, Constants.HOST_IP_REQ + "/rpc/lease/getImg.app"));
            requestMap.put(LEASE_DELETE_IMG, new RequestModel(LEASE_DELETE_IMG, Constants.HOST_IP_REQ + "/rpc/lease/delete/img.app"));
            requestMap.put(LEASE_SETRENT, new RequestModel(LEASE_SETRENT, Constants.HOST_IP_REQ + "/rpc/lease/setRent.app"));
            requestMap.put(LEASE_USERJOIN_INFO, new RequestModel(LEASE_USERJOIN_INFO, Constants.HOST_IP_REQ + "/rpc/lease/userjoin/info.app"));
            requestMap.put(LEASE_USERJOIN, new RequestModel(LEASE_USERJOIN, Constants.HOST_IP_REQ + "/rpc/lease/userjoin.app"));
            requestMap.put(LEASE_AGENTCONFIRMUSERJOIN, new RequestModel(LEASE_AGENTCONFIRMUSERJOIN, Constants.HOST_IP_REQ + "/rpc/lease/agentConfirmUserJoin.app"));
            requestMap.put(LEASE_WAIT, new RequestModel(LEASE_WAIT, Constants.HOST_IP_REQ + "/rpc/lease/wait.app"));
            requestMap.put(LEASE_USER_HOUSE, new RequestModel(LEASE_USER_HOUSE, Constants.HOST_IP_REQ + "/rpc/lease/user/house.app"));
            requestMap.put(WITHDRAWAL_SURPLUS, new RequestModel(WITHDRAWAL_SURPLUS, Constants.HOST_IP_REQ + "/rpc/withdrawal/surplus.app"));
            requestMap.put(WITHDRAWAL_APPLY, new RequestModel(WITHDRAWAL_APPLY, Constants.HOST_IP_REQ + "/rpc/withdrawal/apply.app"));
            requestMap.put(WITHDRAWAL_LIST, new RequestModel(WITHDRAWAL_LIST, Constants.HOST_IP_REQ + "/rpc/withdrawal/list.app"));
            requestMap.put(LEASE_PAY_RENT_SURPLUS, new RequestModel(LEASE_PAY_RENT_SURPLUS, Constants.HOST_IP_REQ + "/rpc/lease/pay/rent/surplus.app"));
            requestMap.put(LEASE_PAY_RENT_SENDVCODE, new RequestModel(LEASE_PAY_RENT_SENDVCODE, Constants.HOST_IP_REQ + "/rpc/lease/pay/rent/sendvcode.app"));
            requestMap.put(LEASE_PAY_RENT, new RequestModel(LEASE_PAY_RENT, Constants.HOST_IP_REQ + "/rpc/lease/pay/rent.app"));
            requestMap.put(USER_PASSWORD_VALID, new RequestModel(USER_PASSWORD_VALID, Constants.HOST_IP_REQ + "/rpc/user/password/valid.app"));
            requestMap.put(SECURITY_CENTER_INFO, new RequestModel(SECURITY_CENTER_INFO, Constants.HOST_IP_REQ + "/rpc/security/center/info.app"));
            requestMap.put(USER_PASSWORD_UPDATE, new RequestModel(USER_PASSWORD_UPDATE, Constants.HOST_IP_REQ + "/rpc/user/password/update.app"));

            requestMap.put(LINK_ARTICLE, new RequestModel(LINK_ARTICLE, Constants.HOST_IP_REQ + "/rpc/link/article.app"));
            requestMap.put(USER_PASSWORD_UPDATE, new RequestModel(USER_PASSWORD_UPDATE, Constants.HOST_IP_REQ + "/rpc/user/password/update.app"));
            requestMap.put(USER_NOLOGIN_PASSWORD_UPDATE, new RequestModel(USER_NOLOGIN_PASSWORD_UPDATE, Constants.HOST_IP_REQ + "/rpc/user/nologin/password/update.app"));
            requestMap.put(LEASE_AGENT_HOUSE, new RequestModel(LEASE_AGENT_HOUSE, Constants.HOST_IP_REQ + "/rpc/lease/agent/house.app"));
            requestMap.put(LEASE_AGENT_HOUSE_RENT, new RequestModel(LEASE_AGENT_HOUSE_RENT, Constants.HOST_IP_REQ + "/rpc/lease/agent/house/rent.app"));
            requestMap.put(LEASE_SETPROPERTY, new RequestModel(LEASE_SETPROPERTY, Constants.HOST_IP_REQ + "/rpc/lease/setProperty.app"));
            requestMap.put(LEASE_GETPROPERTY, new RequestModel(LEASE_GETPROPERTY, Constants.HOST_IP_REQ + "/rpc/lease/getProperty.app"));
            requestMap.put(LEASE_LEASED, new RequestModel(LEASE_LEASED, Constants.HOST_IP_REQ + "/rpc/lease/leased.app"));
            requestMap.put(CONTACT, new RequestModel(CONTACT, Constants.HOST_IP_REQ + "/rpc/contact.app"));
            requestMap.put(STARTUP_IMAGE, new RequestModel(STARTUP_IMAGE, Constants.HOST_IP_REQ + "/rpc/startup/image.app"));
            requestMap.put(LOAN_APPLY, new RequestModel(LOAN_APPLY, Constants.HOST_IP_REQ + "/rpc/loan/apply.app"));
            requestMap.put(SECURITY_CENTER_GETIMG, new RequestModel(SECURITY_CENTER_GETIMG, Constants.HOST_IP_REQ + "/rpc/security/center/getImg.app"));
            requestMap.put(SECURITY_CENTER_DELETEIMG, new RequestModel(SECURITY_CENTER_DELETEIMG, Constants.HOST_IP_REQ + "/rpc/security/center/deleteImg.app"));
            requestMap.put(HOUSE_GETRELEASE, new RequestModel(HOUSE_GETRELEASE, Constants.HOST_IP_REQ + "/rpc/house/getRelease.app"));
            requestMap.put(HOUSE_RELEASE, new RequestModel(HOUSE_RELEASE, Constants.HOST_IP_REQ + "/rpc/house/release.app"));
            requestMap.put(HOUSE_CANCELRELEASE, new RequestModel(HOUSE_CANCELRELEASE, Constants.HOST_IP_REQ + "/rpc/house/cancelRelease.app"));
            requestMap.put(HOUSE_RESERVE, new RequestModel(HOUSE_RESERVE, Constants.HOST_IP_REQ + "/rpc/house/reserve.app"));
            requestMap.put(HOUSE_RESERVE_DELETE, new RequestModel(HOUSE_RESERVE_DELETE, Constants.HOST_IP_REQ + "/rpc/house/reserve/delete.app"));
            requestMap.put(HOUSE_RESERVE_REMARK, new RequestModel(HOUSE_RESERVE_REMARK, Constants.HOST_IP_REQ + "/rpc/house/reserve/remark.app"));
            requestMap.put(HOUSE_RESERVE_LIST, new RequestModel(HOUSE_RESERVE_LIST, Constants.HOST_IP_REQ + "/rpc/house/reserve/list.app"));
            requestMap.put(CITYAREA, new RequestModel(CITYAREA, Constants.HOST_IP_REQ + "/rpc/cityarea.app"));
            requestMap.put(HOUSE_RELEASE_INFO, new RequestModel(HOUSE_RELEASE_INFO, Constants.HOST_IP_REQ + "/rpc/house/release/info.app"));
            requestMap.put(LEASE_RELEASE_LIST, new RequestModel(LEASE_RELEASE_LIST, Constants.HOST_IP_REQ + "/rpc/lease/release/list.app"));
            requestMap.put(LEASE_WITHDRAW, new RequestModel(LEASE_WITHDRAW, Constants.HOST_IP_REQ + "/rpc/lease/withdraw.app"));
            requestMap.put(HOUSE_SET_POSITION, new RequestModel(HOUSE_SET_POSITION, Constants.HOST_IP_REQ + "/rpc/house/set/position.app"));


            // 注意保留 rpc
        }

        return requestMap.get(id);
    }

    public static final String USER_REGIST_CHECK_TELPHONE = "USER_REGIST_CHECK_TELPHONE"; // 验证手机号是否已经注册
    public static final String USER_REGIST_SMS_SEND = "USER_REGIST_SMS_SEND"; // 注册发送验证码
    public static final String USER_REGIST_PASSWORD_SET = "USER_REGIST_PASSWORD_SET"; // 注册设置密码
    public static final String SECURITY_CENTER_TRANSACTION_PASSWORD_SAVE = "SECURITY_CENTER_TRANSACTION_PASSWORD_SAVE"; // 设置交易密码
    public static final String USER_LOGIN = "USER_LOGIN"; // 登录成功
    public static final String USER_VERIFY_TOKEN = "USER_VERIFY_TOKEN"; // 验证令牌
    public static final String USER_LOGOUT = "USER_LOGOUT"; // 退出登录
    public static final String USER_MY = "USER_MY"; // 房管家-我
    public static final String HOUSE_ADD_LIST = "HOUSE_ADD_LIST"; // 房屋添加列表
    public static final String HOUSE_ADD = "HOUSE_ADD"; // 添加一个空房子
    public static final String HOUSE_ADDINFO = "HOUSE_ADDINFO"; // 获取房屋添加信息
    public static final String HOUSE_DELETE = "HOUSE_DELETE"; // 删除房屋信息
    public static final String HOUSE_SETINFO = "HOUSE_SETINFO"; // 设置房屋信息
    public static final String HOUSE_SETTRAFFIC = "HOUSE_SETTRAFFIC"; // 设置公交信息
    public static final String HOUSE_EQUIPMENT = "HOUSE_EQUIPMENT"; // 获取所有配套设施
    public static final String HOUSE_SETEQUIPMENT = "HOUSE_SETEQUIPMENT"; // 设置配套设施
    public static final String HOUSE_SETLANDLORDRENT = "HOUSE_SETLANDLORDRENT"; // 设置房东房屋租赁费⽤用
    public static final String HOUSE_SETLANDLORDBANKSENDVCODE = "HOUSE_SETLANDLORDBANKSENDVCODE"; // 房东收款账号-发送验证码
    public static final String HOUSE_SETLANDLORDBANK = "HOUSE_SETLANDLORDBANK"; // 房东收款账号-提交
    public static final String HOUSE_LANDLORDJOIN = "HOUSE_LANDLORDJOIN"; // 房东提交⼆二维码
    public static final String HOUSE_AGENTCONFIRMLANDLORDJOIN = "HOUSE_AGENTCONFIRMLANDLORDJOIN"; // 中介确认与房东关联
    public static final String HOUSE_LANDLORDJOIN_INFO = "HOUSE_LANDLORDJOIN_INFO"; // 获取房东关联信息
    public static final String UPLOAD = "UPLOAD"; // 批量上传图⽚
    public static final String HOUSE_SETIMG = "HOUSE_SETIMG"; // 设置房屋图⽚片信息
    public static final String HOUSE_IMG_DELETE = "HOUSE_IMG_DELETE"; // 删除房屋图⽚
    public static final String HOUSE_GETIMG = "HOUSE_GETIMG"; // 获取房屋图片信息
    public static final String HOUSE_INFO = "HOUSE_INFO"; // 获取房屋信息
    public static final String HOUSE_TRAFFIC = "HOUSE_TRAFFIC"; // 获取交通信息
    public static final String HOUSE_LANDLORDRENTALFEE = "HOUSE_LANDLORDRENTALFEE"; // 获取房屋租赁费⽤用信
    public static final String HOUSE_LANDLORD = "HOUSE_LANDLORD"; // 获取房东信息
    public static final String HOUSE_AGENT_USERINFO = "HOUSE_AGENT_USERINFO"; // 获取中介人信息
    public static final String HOUSE_GET_QR = "HOUSE_GET_QR"; // 获取关联⼆二维码
    public static final String BANKS = "BANKS"; // 银⾏卡列表
    public static final String BANK_AREA = "BANK_AREA"; // 开户行城市
    public static final String SECURITY_CENTER_BANK_INFO = "SECURITY_CENTER_BANK_INFO"; // 获取绑定银⾏行卡信息
    public static final String SECURITY_CENTER_BANK_BIND_SENDVCODE = "SECURITY_CENTER_BANK_BIND_SENDVCODE"; // 绑定银⾏行卡发送验证码
    public static final String SECURITY_CENTER_BANK_BIND_PAY = "SECURITY_CENTER_BANK_BIND_PAY"; // 绑定银⾏行卡
    public static final String SECURITY_CENTER_BANK_UPDATE_ADDRESS = "SECURITY_CENTER_BANK_UPDATE_ADDRESS"; // 设置开户⾏行
    public static final String SECURITY_CENTER_BANK_UNBIND = "SECURITY_CENTER_BANK_UNBIND"; // 解绑银⾏行卡
    public static final String LEASE_SETLEASEIMG = "LEASE_SETLEASEIMG"; // 设置租户图⽚片信息
    public static final String SECURITY_CENTER_EMERGENCY_CONTACT_SAVE = "SECURITY_CENTER_EMERGENCY_CONTACT_SAVE"; // 设置紧急联系⼈人
    public static final String SECURITY_CENTER_ITEM_SAVE = "SECURITY_CENTER_ITEM_SAVE"; // 设置微信
    public static final String USER_SET_LOGO = "USER_SET_LOGO"; // 设置头像
    public static final String SECURITY_CENTER_IMGITEM_SAVE = "SECURITY_CENTER_IMGITEM_SAVE"; // 设置⾝身份证、⼯工作证
    public static final String SECURITY_CENTER_ITEM_INFO = "SECURITY_CENTER_ITEM_INFO"; // 获取微信、⼯工作证、⾝身份证照⽚片、紧急联系⼈人
    public static final String LEASE_SETIMG = "LEASE_SETIMG"; // 设置租户⾝身份证、租房合同
    public static final String LEASE_GETIMG = "LEASE_GETIMG"; // 获取租户⾝身份证、租房合同
    public static final String LEASE_DELETE_IMG = "LEASE_DELETE_IMG"; // 删除租房合同
    public static final String LEASE_SETRENT = "LEASE_SETRENT"; // 设置租⾦金信息
    public static final String LEASE_USERJOIN_INFO = "LEASE_USERJOIN_INFO"; // 获取⽤用户关联信息
    public static final String LEASE_USERJOIN = "LEASE_USERJOIN"; // ⽤用户提交关联码
    public static final String LEASE_AGENTCONFIRMUSERJOIN = "LEASE_AGENTCONFIRMUSERJOIN"; // 中介确认租客关联
    public static final String LEASE_WAIT = "LEASE_WAIT"; // 中介未租列表
    public static final String LEASE_USER_HOUSE = "LEASE_USER_HOUSE"; // 获取租户信息
    public static final String WITHDRAWAL_SURPLUS = "WITHDRAWAL_SURPLUS"; // 提现余额查询
    public static final String WITHDRAWAL_APPLY = "WITHDRAWAL_APPLY"; // 申请提现
    public static final String WITHDRAWAL_LIST = "WITHDRAWAL_LIST"; // 提现记录
    public static final String LEASE_PAY_RENT_SURPLUS = "LEASE_PAY_RENT_SURPLUS"; // 余额⽀支付房租
    public static final String LEASE_PAY_RENT_SENDVCODE = "LEASE_PAY_RENT_SENDVCODE"; // 银行卡支付房租发送验证码
    public static final String LEASE_PAY_RENT = "LEASE_PAY_RENT"; // 银行卡支付房租
    public static final String USER_PASSWORD_VALID = "USER_PASSWORD_VALID"; // 验证登录密码
    public static final String SECURITY_CENTER_INFO = "SECURITY_CENTER_INFO"; // 个人中心
    public static final String USER_PASSWORD_UPDATE = "USER_PASSWORD_UPDATE"; // 修改密码

    public static final String LINK_ARTICLE = "LINK_ARTICLE"; // 媒体报道
    public static final String USER_NOLOGIN_PASSWORD_UPDATE = "USER_NOLOGIN_PASSWORD_UPDATE"; // 未登录修改密码
    public static final String LEASE_AGENT_HOUSE = "LEASE_AGENT_HOUSE"; // 收租记录
    public static final String LEASE_AGENT_HOUSE_RENT = "LEASE_AGENT_HOUSE_RENT"; // 租金记录
    public static final String LEASE_SETPROPERTY = "LEASE_SETPROPERTY"; // 设置租赁属性
    public static final String LEASE_GETPROPERTY = "LEASE_GETPROPERTY"; // 获取租赁属性
    public static final String LEASE_LEASED = "LEASE_LEASED"; // 已租列表
    public static final String CONTACT = "CONTACT"; // 联系我们
    public static final String STARTUP_IMAGE = "STARTUP_IMAGE"; // 获取启动图片
    public static final String LOAN_APPLY = "LOAN_APPLY"; // 申请贷款
    public static final String SECURITY_CENTER_GETIMG = "SECURITY_CENTER_GETIMG"; // 获取用户证件图片
    public static final String SECURITY_CENTER_DELETEIMG = "SECURITY_CENTER_DELETEIMG"; // 删除用户证件图片
    public static final String HOUSE_GETRELEASE = "HOUSE_GETRELEASE"; // 发布房源数据
    public static final String HOUSE_RELEASE = "HOUSE_RELEASE"; // 发布房源
    public static final String HOUSE_CANCELRELEASE = "HOUSE_CANCELRELEASE"; // 取消发布
    public static final String HOUSE_RESERVE = "HOUSE_RESERVE"; // 预约看房
    public static final String HOUSE_RESERVE_DELETE = "HOUSE_RESERVE_DELETE"; // 删除看房记录
    public static final String HOUSE_RESERVE_REMARK = "HOUSE_RESERVE_REMARK"; // 看房记录设置备注
    public static final String HOUSE_RESERVE_LIST = "HOUSE_RESERVE_LIST"; // 用户看房记录
    public static final String CITYAREA = "CITYAREA"; // 城市地区数据
    public static final String HOUSE_RELEASE_INFO = "HOUSE_RELEASE_INFO"; // 获取发布房源详细信息
    public static final String LEASE_RELEASE_LIST = "LEASE_RELEASE_LIST"; // 房源列表
    public static final String LEASE_WITHDRAW = "LEASE_WITHDRAW"; // 退租
    public static final String HOUSE_SET_POSITION = "HOUSE_SET_POSITION"; // 设置房屋位置

}
