package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 提现记录
 * 
 * @author sunshuai
 * 
 */
public class WithdrawalAppDto implements Serializable {

	private static final long serialVersionUID = -2591053930436949096L;
	/**
	 * 账户余额
	 */
	private String totalMoney;
	/**
	 * 现可以提现金额
	 */
	private String money;
	/**
	 * 提现手续费 不收时为0.00
	 */
	private String serviceMoney;
	/**
	 * 不收手续费剩余次数 0为本次收手续费
	 */
	private int noServiceMoneyCount;
	/**
	 * 不收手续费总次数
	 */
	private int noServiceMoneyTotal;

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(String serviceMoney) {
		this.serviceMoney = serviceMoney;
	}

	public int getNoServiceMoneyCount() {
		return noServiceMoneyCount;
	}

	public void setNoServiceMoneyCount(int noServiceMoneyCount) {
		this.noServiceMoneyCount = noServiceMoneyCount;
	}

	public int getNoServiceMoneyTotal() {
		return noServiceMoneyTotal;
	}

	public void setNoServiceMoneyTotal(int noServiceMoneyTotal) {
		this.noServiceMoneyTotal = noServiceMoneyTotal;
	}

}
