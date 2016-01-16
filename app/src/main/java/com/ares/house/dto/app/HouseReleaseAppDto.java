package com.ares.house.dto.app;

import com.housekeeper.model.RentContainAppDtoEx;

import java.io.Serializable;
import java.util.List;

public class HouseReleaseAppDto implements Serializable {

	private static final long serialVersionUID = 1772341167612972814L;
	/**
	 * 房屋标示
	 */
	private int houseId;
	/**
	 * 最低月租金
	 */
	private String money;
	/**
	 * 租金多余部分 百分之几作为中介佣金
	 */
	private int proportion;
	/**
	 * 推荐标签
	 */
	private List<HouseTagAppDto> tags;

	/**
	 * 最早入住时间
	 */
	private long beginTime;
	private String beginTimeStr;

	/**
	 * 房租包含
	 */
	private List<RentContainAppDtoEx> rentContains;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getProportion() {
		return proportion;
	}

	public void setProportion(int proportion) {
		this.proportion = proportion;
	}

	public List<HouseTagAppDto> getTags() {
		return tags;
	}

	public void setTags(List<HouseTagAppDto> tags) {
		this.tags = tags;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public List<RentContainAppDtoEx> getRentContains() {
		return rentContains;
	}

	public void setRentContains(List<RentContainAppDtoEx> rentContains) {
		this.rentContains = rentContains;
	}

}
