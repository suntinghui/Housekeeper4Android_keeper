package com.ares.house.dto.app;

import java.util.List;

public class UserAppDto extends MyAppDto {

	private static final long serialVersionUID = -313098347653466002L;
	/**
	 * 租房列表
	 */
	private List<UserHouseListAppDto> houses;
	/**
	 * 是否开启代扣 true开启
	 */
	private boolean autoPay;
	/**
	 * 看房记录条数
	 */
	private int reserveCount;

	public List<UserHouseListAppDto> getHouses() {
		return houses;
	}

	public void setHouses(List<UserHouseListAppDto> houses) {
		this.houses = houses;
	}

	public boolean isAutoPay() {
		return autoPay;
	}

	public void setAutoPay(boolean autoPay) {
		this.autoPay = autoPay;
	}

	public int getReserveCount() {
		return reserveCount;
	}

	public void setReserveCount(int reserveCount) {
		this.reserveCount = reserveCount;
	}

}
