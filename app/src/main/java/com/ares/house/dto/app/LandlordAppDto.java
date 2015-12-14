package com.ares.house.dto.app;

import java.util.List;

public class LandlordAppDto extends MyAppDto {
	private static final long serialVersionUID = -6631011057467879546L;
	/**
	 * 共收租金
	 */
	private String totalRent;

	/**
	 * 房子
	 */
	private List<LandlordHouseListAppDto> houses;

	public String getTotalRent() {
		return totalRent;
	}

	public void setTotalRent(String totalRent) {
		this.totalRent = totalRent;
	}

	public List<LandlordHouseListAppDto> getHouses() {
		return houses;
	}

	public void setHouses(List<LandlordHouseListAppDto> houses) {
		this.houses = houses;
	}

}
