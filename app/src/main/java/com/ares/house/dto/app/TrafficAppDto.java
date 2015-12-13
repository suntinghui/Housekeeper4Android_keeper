package com.ares.house.dto.app;

import java.io.Serializable;

public class TrafficAppDto  implements Serializable {

	private static final long serialVersionUID = 5589242058570114061L;
	/**
	 * 房屋标示
	 */
	private int houseId;
	/**
	 * 公交
	 */
	private String bus;
	/**
	 * 地铁
	 */
	private String subway;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public String getSubway() {
		return subway;
	}

	public void setSubway(String subway) {
		this.subway = subway;
	}

}
