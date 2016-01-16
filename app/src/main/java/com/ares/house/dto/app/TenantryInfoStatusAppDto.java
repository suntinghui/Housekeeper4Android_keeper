package com.ares.house.dto.app;

public class TenantryInfoStatusAppDto {

	/**
	 * 身份证件
	 */
	private boolean idcard;
	/**
	 * 工作地址
	 */
	private boolean workAddress;

	public boolean isIdcard() {
		return idcard;
	}

	public void setIdcard(boolean idcard) {
		this.idcard = idcard;
	}

	public boolean isWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(boolean workAddress) {
		this.workAddress = workAddress;
	}

}
