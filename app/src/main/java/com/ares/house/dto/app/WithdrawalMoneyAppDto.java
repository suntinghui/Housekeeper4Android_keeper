package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 提现记录
 * 
 * @author sunshuai
 * 
 */
public class WithdrawalMoneyAppDto implements Serializable {

	private static final long serialVersionUID = -4084373018299045179L;

	/**
	 * 订单号
	 */
	private String orderNum;

	/**
	 * 提现状态 a审核中 b提现失败 c提现确认中 d提现成功
	 */
	private char status;
	/**
	 * 申请时间
	 */
	private String beginTime;
	/**
	 * 支付接口调用成功时间
	 */
	private String sendTime;
	/**
	 * 最晚到账时间
	 */
	private String endTime;
	/**
	 * 提现金额
	 */
	private String money;
	/**
	 * 银行简码
	 */
	private String bankId;
	/**
	 * 银行卡4位尾号
	 */
	private String tailNumber;
	/**
	 * 备注
	 */
	private String remark;

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTailNumber() {
		return tailNumber;
	}

	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
