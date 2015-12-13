package com.ares.house.dto.app;

import java.io.Serializable;

public class UserJoinAppDto implements Serializable {

	private static final long serialVersionUID = 1053100111271701508L;
	/**
	 * 房屋标示
	 */
	private int houseId;
	/**
	 * 用户手机号
	 */
	private String telphone;
	/**
	 * 用户头像
	 */
	private String logo;
	/**
	 * 用户是否已提交关联码
	 */
	private boolean userJoin;
	/**
	 * 用户信息是否已完成
	 */
	private boolean userInfoComplete;
	/**
	 * 租房合同是否已完成
	 */
	private boolean contractComplete;
	/**
	 * 租金信息是否已完成
	 */
	private boolean infoComplete;
	/**
	 * 交租日
	 */
	private int payDay;
	/**
	 * 月租金
	 */
	private String monthMoney;
	/**
	 * 押金
	 */
	private String mortgageMoney;
	/**
	 * 中介费
	 */
	private String agencyFee;
	/**
	 * 取暖费
	 */
	private String heatingFeesMoney;
	/**
	 * 物业费
	 */
	private String propertyFeesMoney;
	/**
	 * 取暖费 false房东交
	 */
	private boolean heatingFees;
	/**
	 * 物业费 false房东交
	 */
	private boolean propertyFees;
	/**
	 * 租户租期至
	 */
	private Long beginTime;
	/**
	 * 租户租期至
	 */
	private String beginTimeStr;
	/**
	 * 租户租期至
	 */
	private Long endTime;
	/**
	 * 租户租期至
	 */
	private String endTimeStr;
	/**
	 * 房东委代理期
	 */
	private String landlordEndTimeStr;
	/**
	 * 最小月租金
	 */
	private String minMonthMoney;
	/**
	 * 违约扣除押金比例
	 */
	private int violateDeduct;
	/**
	 * 用户关联码
	 */
	private String userJoinCode;
	/**
	 * 首页图片
	 */
	private String indexImgUrl;
	/**
	 * 门牌号
	 */
	private String houseNum;
	/**
	 * 详细地址
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
	 * 上个租户的退租时间 如果有的话
	 */
	private long takeBackTime;
	/**
	 * 上个租户的退租时间 如果有的话
	 */
	private String takeBackTimeStr;
	/**
	 * 首付
	 */
	private String firstMonthMoney;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public boolean isUserJoin() {
		return userJoin;
	}

	public void setUserJoin(boolean userJoin) {
		this.userJoin = userJoin;
	}

	public boolean isUserInfoComplete() {
		return userInfoComplete;
	}

	public void setUserInfoComplete(boolean userInfoComplete) {
		this.userInfoComplete = userInfoComplete;
	}

	public boolean isContractComplete() {
		return contractComplete;
	}

	public void setContractComplete(boolean contractComplete) {
		this.contractComplete = contractComplete;
	}

	public boolean isInfoComplete() {
		return infoComplete;
	}

	public void setInfoComplete(boolean infoComplete) {
		this.infoComplete = infoComplete;
	}

	public int getPayDay() {
		return payDay;
	}

	public void setPayDay(int payDay) {
		this.payDay = payDay;
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

	public String getAgencyFee() {
		return agencyFee;
	}

	public void setAgencyFee(String agencyFee) {
		this.agencyFee = agencyFee;
	}

	public String getHeatingFeesMoney() {
		return heatingFeesMoney;
	}

	public void setHeatingFeesMoney(String heatingFeesMoney) {
		this.heatingFeesMoney = heatingFeesMoney;
	}

	public String getPropertyFeesMoney() {
		return propertyFeesMoney;
	}

	public void setPropertyFeesMoney(String propertyFeesMoney) {
		this.propertyFeesMoney = propertyFeesMoney;
	}

	public boolean isHeatingFees() {
		return heatingFees;
	}

	public void setHeatingFees(boolean heatingFees) {
		this.heatingFees = heatingFees;
	}

	public boolean isPropertyFees() {
		return propertyFees;
	}

	public void setPropertyFees(boolean propertyFees) {
		this.propertyFees = propertyFees;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getLandlordEndTimeStr() {
		return landlordEndTimeStr;
	}

	public void setLandlordEndTimeStr(String landlordEndTimeStr) {
		this.landlordEndTimeStr = landlordEndTimeStr;
	}

	public String getMinMonthMoney() {
		return minMonthMoney;
	}

	public void setMinMonthMoney(String minMonthMoney) {
		this.minMonthMoney = minMonthMoney;
	}

	public int getViolateDeduct() {
		return violateDeduct;
	}

	public void setViolateDeduct(int violateDeduct) {
		this.violateDeduct = violateDeduct;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUserJoinCode() {
		return userJoinCode;
	}

	public void setUserJoinCode(String userJoinCode) {
		this.userJoinCode = userJoinCode;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getIndexImgUrl() {
		return indexImgUrl;
	}

	public void setIndexImgUrl(String indexImgUrl) {
		this.indexImgUrl = indexImgUrl;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
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

	public long getTakeBackTime() {
		return takeBackTime;
	}

	public void setTakeBackTime(long takeBackTime) {
		this.takeBackTime = takeBackTime;
	}

	public String getTakeBackTimeStr() {
		return takeBackTimeStr;
	}

	public void setTakeBackTimeStr(String takeBackTimeStr) {
		this.takeBackTimeStr = takeBackTimeStr;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getFirstMonthMoney() {
		return firstMonthMoney;
	}

	public void setFirstMonthMoney(String firstMonthMoney) {
		this.firstMonthMoney = firstMonthMoney;
	}

}
