package com.ares.house.dto.app;

import java.util.List;

public class UserAppDto extends MyAppDto {

	private static final long serialVersionUID = -313098347653466002L;
	/**
	 * 租房列表
	 */
	private List<UserHouseListAppDto> houses;

	public List<UserHouseListAppDto> getHouses() {
		return houses;
	}

	public void setHouses(List<UserHouseListAppDto> houses) {
		this.houses = houses;
	}

}
