package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 交易记录
 * 
 * @author sunshuai
 * 
 */
public class MoneyHistoryAppDto implements Serializable {

	private static final long serialVersionUID = -9097338904632361164L;
	/**
	 * 显示名称 购买债权 取现等等
	 */
	private String display;
	/**
	 * 金额
	 */
	private String money;
	/**
	 * true 收入
	 */
	private boolean income;
	/**
	 * 交易时间
	 */
	private String time;
	/**
	 * 购买的债权编号
	 */
	private String dpnum;

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public boolean isIncome() {
		return income;
	}

	public void setIncome(boolean income) {
		this.income = income;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDpnum() {
		return dpnum;
	}

	public void setDpnum(String dpnum) {
		this.dpnum = dpnum;
	}

}
