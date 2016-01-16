package com.ares.house.dto.app;

public class UserInfoStatusAppDto {

	/**
	 * 用户头像
	 */
	private boolean userLogo;
	/**
	 * 交易密码
	 */
	private boolean transactionPassword;
	/**
	 * 银行卡
	 */
	private boolean bankCard;
	/**
	 * 身份证照片
	 */
	private boolean idcard;
	/**
	 * 工作证
	 */
	private boolean work;

	public boolean isUserLogo() {
		return userLogo;
	}

	public void setUserLogo(boolean userLogo) {
		this.userLogo = userLogo;
	}

	public boolean isTransactionPassword() {
		return transactionPassword;
	}

	public void setTransactionPassword(boolean transactionPassword) {
		this.transactionPassword = transactionPassword;
	}

	public boolean isBankCard() {
		return bankCard;
	}

	public void setBankCard(boolean bankCard) {
		this.bankCard = bankCard;
	}

	public boolean isIdcard() {
		return idcard;
	}

	public void setIdcard(boolean idcard) {
		this.idcard = idcard;
	}

	public boolean isWork() {
		return work;
	}

	public void setWork(boolean work) {
		this.work = work;
	}

}
