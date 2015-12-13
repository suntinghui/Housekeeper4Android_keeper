package com.ares.house.dto.app;

import java.io.Serializable;

public class UserHouseListAppDto implements Serializable {

	private static final long serialVersionUID = 8845698858675873772L;
	private int houseId;
	/**
	 * 租赁标示
	 */
	private int leaseId;
	/**
	 * 房子首页图片
	 */
	private String indexImg;
	/**
	 * 门牌号
	 */
	private String houseNum;
	/**
	 * 街道地址
	 */
	private String address;
	/**
	 * 小区名称
	 */
	private String community;
	/**
	 * 城市名称
	 */
	private String cityStr;
	/**
	 * 区名称
	 */
	private String areaStr;
	/**
	 * 租期开始时间
	 */
	private String beginTimeStr;
	/**
	 * 租期结束时间
	 */
	private String endTimeStr;
	/**
	 * 月租金
	 */
	private String monthMoney;
	/**
	 * 押金
	 */
	private String mortgageMoney;
	/**
	 * 租期总月数
	 */
	private int totalMonth;
	/**
	 * 当前要支付的月数 month=1的时候说明是第一次支付 中介费、押金都要支付
	 */
	private int month;
	/**
	 * 中介费
	 */
	private String agencyFee;
	/**
	 * 总支付金额 为空则当前还不需要支付 为空的时候说明当前不需要支付
	 */
	private String totalPay;
	/**
	 * 如果余额不足时，需要银行卡支付金额 如果为空直接输入交易密码支付
	 */
	private String bankPay;
	/**
	 * 中介公司名称
	 */
	private String companyName;
	/**
	 * 中介公司logo
	 */
	private String companyLogo;
	/**
	 * 中介人名字
	 */
	private String agentUserName;
	/**
	 * 中介头像
	 */
	private String agentLogo;
	/**
	 * 支付状态 a等待支付 b支付失败 c支付确认中 d支付成功
	 */
	private char paymentStatus;
	/**
	 * 取暖费
	 */
	private String heatingFeesMoney;
	/**
	 * false 房东交取暖费
	 */
	private boolean heatingFees;
	/**
	 * 物业费
	 */
	private String propertyFeesMoney;
	/**
	 * false 房东交物业费
	 */
	private boolean propertyFees;
	/**
	 * 距离交租日还剩几天
	 */
	private int surplusDay;
	/**
	 * 距离交租日百分比
	 */
	private int surplusDayPercentage;
	/**
	 * 最晚交租时间
	 */
	private String payEndTimeStr;
	/**
	 * 是否是立即支付
	 */
	private boolean atOncePay;
	/**
	 * 首月首付
	 */
	private String firstMonthMoney;
	/**
	 * b正常 c退租中 d已完成
	 */
	private char status;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCityStr() {
		return cityStr;
	}

	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public String getMortgageMoney() {
		return mortgageMoney;
	}

	public void setMortgageMoney(String mortgageMoney) {
		this.mortgageMoney = mortgageMoney;
	}

	public int getTotalMonth() {
		return totalMonth;
	}

	public void setTotalMonth(int totalMonth) {
		this.totalMonth = totalMonth;
	}

	public String getAgencyFee() {
		return agencyFee;
	}

	public void setAgencyFee(String agencyFee) {
		this.agencyFee = agencyFee;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public String getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(String totalPay) {
		this.totalPay = totalPay;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public char getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getHeatingFeesMoney() {
		return heatingFeesMoney;
	}

	public void setHeatingFeesMoney(String heatingFeesMoney) {
		this.heatingFeesMoney = heatingFeesMoney;
	}

	public boolean isHeatingFees() {
		return heatingFees;
	}

	public void setHeatingFees(boolean heatingFees) {
		this.heatingFees = heatingFees;
	}

	public String getPropertyFeesMoney() {
		return propertyFeesMoney;
	}

	public void setPropertyFeesMoney(String propertyFeesMoney) {
		this.propertyFeesMoney = propertyFeesMoney;
	}

	public boolean isPropertyFees() {
		return propertyFees;
	}

	public void setPropertyFees(boolean propertyFees) {
		this.propertyFees = propertyFees;
	}

	public int getSurplusDay() {
		return surplusDay;
	}

	public void setSurplusDay(int surplusDay) {
		this.surplusDay = surplusDay;
	}

	public int getSurplusDayPercentage() {
		return surplusDayPercentage;
	}

	public void setSurplusDayPercentage(int surplusDayPercentage) {
		this.surplusDayPercentage = surplusDayPercentage;
	}

	public String getBankPay() {
		return bankPay;
	}

	public void setBankPay(String bankPay) {
		this.bankPay = bankPay;
	}

	public String getIndexImg() {
		return indexImg;
	}

	public void setIndexImg(String indexImg) {
		this.indexImg = indexImg;
	}

	public String getAgentLogo() {
		return agentLogo;
	}

	public void setAgentLogo(String agentLogo) {
		this.agentLogo = agentLogo;
	}

	public String getPayEndTimeStr() {
		return payEndTimeStr;
	}

	public void setPayEndTimeStr(String payEndTimeStr) {
		this.payEndTimeStr = payEndTimeStr;
	}

	public boolean isAtOncePay() {
		return atOncePay;
	}

	public void setAtOncePay(boolean atOncePay) {
		this.atOncePay = atOncePay;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public String getFirstMonthMoney() {
		return firstMonthMoney;
	}

	public void setFirstMonthMoney(String firstMonthMoney) {
		this.firstMonthMoney = firstMonthMoney;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
