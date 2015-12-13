package com.ares.house.dto.app;

import java.io.Serializable;

public class HouseAddListAppDto implements Serializable {

	private static final long serialVersionUID = 4204037144154529337L;

	/**
	 * 房屋标示
	 */
	private int id;
	/**
	 * 首页图片
	 */
	private String indexImgUrl;
	/**
	 * 门牌号
	 */
	private String houseNum;
	/**
	 * 街道位置
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
	 * 城市标示
	 */
	private int cityId;
	/**
	 * 区名称
	 */
	private String areaStr;
	/**
	 * 区标示
	 */
	private int areaId;
	/**
	 * 租期开始时间
	 */
	private String beginTimeStr;
	/**
	 * 租期结束时间
	 */
	private String endTimeStr;
	/**
	 * 租赁年数
	 */
	private int yearCount;
	/**
	 * 年租金
	 */
	private String yearMoney;
	/**
	 * 每年让租期
	 */
	private int letLeaseDay;
	/**
	 * 房屋信息是否已完成
	 */
	private boolean infoComplete;
	/**
	 * 房产证件是否已完成
	 */
	private boolean estateComplete;
	/**
	 * 代理合同是否已完成
	 */
	private boolean agentComplete;
	/**
	 * 租赁费用是否已完成
	 */
	private boolean rentalComplete;
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
	 * 违约扣除房东几个月租金
	 */
	private int violateMonth;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public int getYearCount() {
		return yearCount;
	}

	public void setYearCount(int yearCount) {
		this.yearCount = yearCount;
	}

	public String getYearMoney() {
		return yearMoney;
	}

	public void setYearMoney(String yearMoney) {
		this.yearMoney = yearMoney;
	}

	public int getLetLeaseDay() {
		return letLeaseDay;
	}

	public void setLetLeaseDay(int letLeaseDay) {
		this.letLeaseDay = letLeaseDay;
	}

	public boolean isInfoComplete() {
		return infoComplete;
	}

	public void setInfoComplete(boolean infoComplete) {
		this.infoComplete = infoComplete;
	}

	public boolean isEstateComplete() {
		return estateComplete;
	}

	public void setEstateComplete(boolean estateComplete) {
		this.estateComplete = estateComplete;
	}

	public boolean isAgentComplete() {
		return agentComplete;
	}

	public void setAgentComplete(boolean agentComplete) {
		this.agentComplete = agentComplete;
	}

	public boolean isRentalComplete() {
		return rentalComplete;
	}

	public void setRentalComplete(boolean rentalComplete) {
		this.rentalComplete = rentalComplete;
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

	public int getViolateMonth() {
		return violateMonth;
	}

	public void setViolateMonth(int violateMonth) {
		this.violateMonth = violateMonth;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
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

}
