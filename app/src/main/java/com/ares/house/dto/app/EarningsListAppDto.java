package com.ares.house.dto.app;

public class EarningsListAppDto {

	/**
	 * 时间
	 */
	private String timeStr;

	/**
	 * 总收益
	 */
	private String total;

	/**
	 * 活期收益
	 */
	private String hq;

	/**
	 * 定期收益
	 */
	private String dq;

	/**
	 * 物业宝收益
	 */
	private String wyb;

	/**
	 * 是否显示提示
	 */
	private boolean show;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = hq;
	}

	public String getDq() {
		return dq;
	}

	public void setDq(String dq) {
		this.dq = dq;
	}

	public String getWyb() {
		return wyb;
	}

	public void setWyb(String wyb) {
		this.wyb = wyb;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

}
