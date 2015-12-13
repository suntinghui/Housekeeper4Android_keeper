package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 我的页面数据
 * 
 * @author sunshuai
 * 
 */
public class MyAppDto implements Serializable {

	private static final long serialVersionUID = -5488602907883432113L;
	/**
	 * 用户头像
	 */
	private String logoUrl;
	/**
	 * 加密后的手机号
	 */
	private String encryptTelphone;
	/**
	 * 剩余可用/可提现金额
	 */
	private String surplusMoney;
	/**
	 * 活期金额
	 */
	private String hqMoney;
	/**
	 * 活期收益率
	 */
	private String hqRate;
	/**
	 * 活期昨日收益
	 */
	private String hqYesterday;

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getEncryptTelphone() {
		return encryptTelphone;
	}

	public void setEncryptTelphone(String encryptTelphone) {
		this.encryptTelphone = encryptTelphone;
	}

	public String getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(String surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public String getHqMoney() {
		return hqMoney;
	}

	public void setHqMoney(String hqMoney) {
		this.hqMoney = hqMoney;
	}

	public String getHqRate() {
		return hqRate;
	}

	public void setHqRate(String hqRate) {
		this.hqRate = hqRate;
	}

	public String getHqYesterday() {
		return hqYesterday;
	}

	public void setHqYesterday(String hqYesterday) {
		this.hqYesterday = hqYesterday;
	}

}
