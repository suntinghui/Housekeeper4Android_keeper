package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 债权包列表数据
 * 
 * @author sunshuai
 * 
 */
public class DebtPackageAppDto implements Serializable {

	private static final long serialVersionUID = -3967064482303095441L;

	/**
	 * 标示
	 */
	private int id;
	/**
	 * 编号
	 */
	private String num;
	/**
	 * 最大周期
	 */
	private int maxPeriod;
	/**
	 * 最小周期
	 */
	private int minPeriod;
	/**
	 * 最大利率
	 */
	private String maxRate;
	/**
	 * 最小利率
	 */
	private String minRate;
	/**
	 * 限额
	 */
	private String limitMoney;
	/**
	 * 每人限购几份 0不限制
	 */
	private int limitCount;
	/**
	 * 状态 a发行中 b已满返息中 c已完成
	 */
	private char status;
	/**
	 * 剩余可售金额
	 */
	private String surplus;
	/**
	 * 债权总额
	 */
	private String totalMoney;
	/**
	 * 债权总额
	 */
	private String totalMoney2;
	/**
	 * 是否是今天发行的标
	 */
	private boolean today;
	/**
	 * 订单来源标示
	 */
	private String sourceTypeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getMaxPeriod() {
		return maxPeriod;
	}

	public void setMaxPeriod(int maxPeriod) {
		this.maxPeriod = maxPeriod;
	}

	public int getMinPeriod() {
		return minPeriod;
	}

	public void setMinPeriod(int minPeriod) {
		this.minPeriod = minPeriod;
	}

	public String getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(String maxRate) {
		this.maxRate = maxRate;
	}

	public String getMinRate() {
		return minRate;
	}

	public void setMinRate(String minRate) {
		this.minRate = minRate;
	}

	public String getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(String limitMoney) {
		this.limitMoney = limitMoney;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getSurplus() {
		return surplus;
	}

	public void setSurplus(String surplus) {
		this.surplus = surplus;
	}

	public boolean isToday() {
		return today;
	}

	public void setToday(boolean today) {
		this.today = today;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public void setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getTotalMoney2() {
		return totalMoney2;
	}

	public void setTotalMoney2(String totalMoney2) {
		this.totalMoney2 = totalMoney2;
	}
}
