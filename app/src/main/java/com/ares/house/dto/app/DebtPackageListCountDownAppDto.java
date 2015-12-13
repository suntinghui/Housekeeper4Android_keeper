package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 债权包列表倒计时
 * 
 * @author sunshuai
 * 
 */
public class DebtPackageListCountDownAppDto implements Serializable {

	private static final long serialVersionUID = -6448581596474207920L;
	/**
	 * 倒计时秒
	 */
	private int second;
	/**
	 * 发行时间毫秒值
	 */
	private long time;
	/**
	 * 是否添加过提醒
	 */
	private boolean isAdd;
	/**
	 * 已完成总数
	 */
	private int completeCount;
	/**
	 * 共计完成金额
	 */
	private String completeMoney;
	/**
	 * 服务器时间
	 */
	private long sysTime;
	/**
	 * 类型 a定投 b抢投 c转让
	 */
	private char type;

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public String getCompleteMoney() {
		return completeMoney;
	}

	public void setCompleteMoney(String completeMoney) {
		this.completeMoney = completeMoney;
	}

	public long getSysTime() {
		return sysTime;
	}

	public void setSysTime(long sysTime) {
		this.sysTime = sysTime;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

}
