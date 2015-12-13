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
	 * 一室一厅一卫
	 */
	private String houseType;
	/**
	 * 门牌号
	 */
	private String houseNum;
	/**
	 * 房子首页图片
	 */
	private String indexImg;
	/**
	 * 小区名称
	 */
	private String community;
	/**
	 * 区名称
	 */
	private String areaStr;
	/**
	 * 月租金
	 */
	private String monthMoney;
	/**
	 * 距离交租日还剩几天
	 */
	private int surplusDay;
	/**
	 * 是否需要支付 true可以支付 false不用支付
	 */
	private boolean pay;
	/**
	 * b正常 c退租中 d已完成
	 */
	private char status;
	/**
	 * 多少平米
	 */
	private String size;
	/**
	 * 合租或整租
	 */
	private String leaseType;
	/**
	 * 租期总月数
	 */
	private int totalMonth;
	/**
	 * 当前要支付的月数 month=1的时候说明是第一次支付 中介费、押金都要支付
	 */
	private int month;
	/**
	 * 提前多少天交租
	 */
	private int inAdvanceDay;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public int getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public String getIndexImg() {
		return indexImg;
	}

	public void setIndexImg(String indexImg) {
		this.indexImg = indexImg;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public int getSurplusDay() {
		return surplusDay;
	}

	public void setSurplusDay(int surplusDay) {
		this.surplusDay = surplusDay;
	}

	public boolean isPay() {
		return pay;
	}

	public void setPay(boolean pay) {
		this.pay = pay;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(String leaseType) {
		this.leaseType = leaseType;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public int getTotalMonth() {
		return totalMonth;
	}

	public void setTotalMonth(int totalMonth) {
		this.totalMonth = totalMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getInAdvanceDay() {
		return inAdvanceDay;
	}

	public void setInAdvanceDay(int inAdvanceDay) {
		this.inAdvanceDay = inAdvanceDay;
	}

}
