package com.ares.house.dto.app;

import java.io.Serializable;

public class LeasedListAppDto implements Serializable {

	private static final long serialVersionUID = 6340563254128972602L;
	private int houseId;
	/**
	 * 租赁标示
	 */
	private int leaseId;
	/**
	 * 首页图片
	 */
	private String indexImgUrl;
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
	 * 租户头像
	 */
	private String userLogo;
	/**
	 * 用户手机号
	 */
	private String userTelphone;
	/**
	 * 租户名称
	 */
	private String userName;
	/**
	 * 租户工作地点
	 */
	private String workAddress;
	/**
	 * 房东手机号
	 */
	private String landlordTelphone;
	/**
	 * b 正常 c退租中
	 */
	private char status;
	/**
	 * 实缴押金
	 */
	private String mortgageMoney;
	/**
	 * 退还押金
	 */
	private String takeBackMortgageMoney;
	/**
	 * 退租时间
	 */
	private String takeBackTime;
	/**
	 * 剩多少天 -1说明不需要再交房租了
	 */
	private int surplusDay;
	/**
	 * 房租交至哪天 没交租之前为null
	 */
	private String lastPaidTimeStr;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
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

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getTakeBackMortgageMoney() {
		return takeBackMortgageMoney;
	}

	public void setTakeBackMortgageMoney(String takeBackMortgageMoney) {
		this.takeBackMortgageMoney = takeBackMortgageMoney;
	}

	public String getTakeBackTime() {
		return takeBackTime;
	}

	public void setTakeBackTime(String takeBackTime) {
		this.takeBackTime = takeBackTime;
	}

	public String getUserTelphone() {
		return userTelphone;
	}

	public void setUserTelphone(String userTelphone) {
		this.userTelphone = userTelphone;
	}

	public String getLandlordTelphone() {
		return landlordTelphone;
	}

	public void setLandlordTelphone(String landlordTelphone) {
		this.landlordTelphone = landlordTelphone;
	}

	public int getSurplusDay() {
		return surplusDay;
	}

	public void setSurplusDay(int surplusDay) {
		this.surplusDay = surplusDay;
	}

	public String getLastPaidTimeStr() {
		return lastPaidTimeStr;
	}

	public void setLastPaidTimeStr(String lastPaidTimeStr) {
		this.lastPaidTimeStr = lastPaidTimeStr;
	}

	public String getMortgageMoney() {
		return mortgageMoney;
	}

	public void setMortgageMoney(String mortgageMoney) {
		this.mortgageMoney = mortgageMoney;
	}

}
