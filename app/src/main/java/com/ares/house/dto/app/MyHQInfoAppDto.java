package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.List;

public class MyHQInfoAppDto implements Serializable {

	private static final long serialVersionUID = 3943826467774020701L;
	/**
	 * 累计收益
	 */
	private String totalEarnings;
	/**
	 * 昨日收益
	 */
	private String yesterdayEarnings;
	/**
	 * 投资总额
	 */
	private String buyMoney;
	/**
	 * 年受益率
	 */
	private String rate;
	/**
	 * 可售金额
	 */
	private String surplusMoney;
	/**
	 * 总金额
	 */
	private String totalMoney;
	/**
	 * 最少购买金额
	 */
	private int minBuy;
	/**
	 * 债权标示
	 */
	private int debtId;
	/**
	 * 反息时间提示
	 */
	private String earningDayStr;
	/**
	 * 账户可购买余额 下一个页面就不用查询用户余额了
	 */
	private String userMoney;
	/**
	 * 已为多少人完成优质债权
	 */
	private int totalCount;
	/**
	 * 活期投资金额
	 */
	private String totalSellMoney;
	/**
	 * 对比
	 */
	private List<RateCompareAppDto> rateCompare1;
	/**
	 * 对比
	 */
	private List<RateCompareAppDto> rateCompare2;
	/**
	 * 没有投资的时候跳转地址
	 */
	private String hintUrl;

	public String getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(String totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	public String getYesterdayEarnings() {
		return yesterdayEarnings;
	}

	public void setYesterdayEarnings(String yesterdayEarnings) {
		this.yesterdayEarnings = yesterdayEarnings;
	}

	public String getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(String surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getMinBuy() {
		return minBuy;
	}

	public void setMinBuy(int minBuy) {
		this.minBuy = minBuy;
	}

	public int getDebtId() {
		return debtId;
	}

	public void setDebtId(int debtId) {
		this.debtId = debtId;
	}

	public String getEarningDayStr() {
		return earningDayStr;
	}

	public void setEarningDayStr(String earningDayStr) {
		this.earningDayStr = earningDayStr;
	}

	public String getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(String userMoney) {
		this.userMoney = userMoney;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalSellMoney() {
		return totalSellMoney;
	}

	public void setTotalSellMoney(String totalSellMoney) {
		this.totalSellMoney = totalSellMoney;
	}

	public List<RateCompareAppDto> getRateCompare1() {
		return rateCompare1;
	}

	public void setRateCompare1(List<RateCompareAppDto> rateCompare1) {
		this.rateCompare1 = rateCompare1;
	}

	public List<RateCompareAppDto> getRateCompare2() {
		return rateCompare2;
	}

	public void setRateCompare2(List<RateCompareAppDto> rateCompare2) {
		this.rateCompare2 = rateCompare2;
	}

	public String getHintUrl() {
		return hintUrl;
	}

	public void setHintUrl(String hintUrl) {
		this.hintUrl = hintUrl;
	}

}
