package com.ares.house.dto.app;

public class HouseInfoStatusAppDto {

	/**
	 * 房屋信息
	 */
	private boolean info;
	/**
	 * 房屋照片
	 */
	private boolean image;
	/**
	 * 配套设施
	 */
	private boolean equipment;
	/**
	 * 交通信息
	 */
	private boolean traffic;
	/**
	 * 位置
	 */
	private boolean position;

	public boolean isInfo() {
		return info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public boolean isEquipment() {
		return equipment;
	}

	public void setEquipment(boolean equipment) {
		this.equipment = equipment;
	}

	public boolean isTraffic() {
		return traffic;
	}

	public void setTraffic(boolean traffic) {
		this.traffic = traffic;
	}

	public boolean isPosition() {
		return position;
	}

	public void setPosition(boolean position) {
		this.position = position;
	}
}
