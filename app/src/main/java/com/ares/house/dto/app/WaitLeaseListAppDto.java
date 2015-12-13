package com.ares.house.dto.app;

import java.io.Serializable;

public class WaitLeaseListAppDto implements Serializable {

	private static final long serialVersionUID = 6874367920078942408L;
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
	 * 剩余让租期
	 */
	private int letLeaseDay;
	/**
	 * 是否已发布 true已发布
	 */
	private boolean release;
	/**
	 * 未处理预约人数
	 */
	private int reserveCount;
	/**
	 * 发布的月租金
	 */
	private String leaseMonthMoney;
	/**
	 * 合租或整租
	 */
	private String leaseType;
	/**
	 * 100平米
	 */
	private String size;
	/**
	 * 三室两厅一卫
	 */
	private String houseType;

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

	public int getLetLeaseDay() {
		return letLeaseDay;
	}

	public void setLetLeaseDay(int letLeaseDay) {
		this.letLeaseDay = letLeaseDay;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public boolean isRelease() {
		return release;
	}

	public void setRelease(boolean release) {
		this.release = release;
	}

	public int getReserveCount() {
		return reserveCount;
	}

	public void setReserveCount(int reserveCount) {
		this.reserveCount = reserveCount;
	}

	public String getLeaseMonthMoney() {
		return leaseMonthMoney;
	}

	public void setLeaseMonthMoney(String leaseMonthMoney) {
		this.leaseMonthMoney = leaseMonthMoney;
	}

	public String getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(String leaseType) {
		this.leaseType = leaseType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

}
