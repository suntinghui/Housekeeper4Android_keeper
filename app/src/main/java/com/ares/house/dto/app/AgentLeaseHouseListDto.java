package com.ares.house.dto.app;

import java.io.Serializable;

public class AgentLeaseHouseListDto implements Serializable {

	private static final long serialVersionUID = -6935265678284496553L;
	/**
	 * 租赁标示
	 */
	private int leaseId;
	/**
	 * 房屋首页图片
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
	 * 代理开始时间
	 */
	private String agentBeginTimeStr;
	/**
	 * 代理结束时间
	 */
	private String agentEndTimeStr;
	/**
	 * 当前月数
	 */
	private int month;
	/**
	 * 总月数
	 */
	private int totalMonth;
	/**
	 * 佣金
	 */
	private String commission;
	/**
	 * 交租时间
	 */
	private String timeStr;

	public String getIndexImg() {
		return indexImg;
	}

	public void setIndexImg(String indexImg) {
		this.indexImg = indexImg;
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

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public String getAgentBeginTimeStr() {
		return agentBeginTimeStr;
	}

	public void setAgentBeginTimeStr(String agentBeginTimeStr) {
		this.agentBeginTimeStr = agentBeginTimeStr;
	}

	public String getAgentEndTimeStr() {
		return agentEndTimeStr;
	}

	public void setAgentEndTimeStr(String agentEndTimeStr) {
		this.agentEndTimeStr = agentEndTimeStr;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getTotalMonth() {
		return totalMonth;
	}

	public void setTotalMonth(int totalMonth) {
		this.totalMonth = totalMonth;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}
}
