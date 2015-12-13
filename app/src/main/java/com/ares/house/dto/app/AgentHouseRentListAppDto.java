package com.ares.house.dto.app;

import java.io.Serializable;

public class AgentHouseRentListAppDto implements Serializable {

	private static final long serialVersionUID = -9130562089312943951L;
	/**
	 * 月数
	 */
	private int month;
	/**
	 * 总月数
	 */
	private int totalMonth;
	/**
	 * 支付金额
	 */
	private String monthMoney;
	/**
	 * 佣金
	 */
	private String commission;
	/**
	 * 交租时间
	 */
	private String timeStr;
	/**
	 * 支付状态 a等待支付 b支付失败 c支付确认中 d支付成功
	 */
	private char paymentStatus;

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

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
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

	public char getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
