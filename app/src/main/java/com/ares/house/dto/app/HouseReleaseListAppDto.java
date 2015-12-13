package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.List;

public class HouseReleaseListAppDto implements Serializable {

	private static final long serialVersionUID = 2936163667831882278L;

	private int houseId;

	/**
	 * 首页图片
	 */
	private String indexImgUrl;
	/**
	 * 小区名称
	 */
	private String community;
	/**
	 * 区名称
	 */
	private String areaStr;
	/**
	 * 合租或整租
	 */
	private String leaseType;
	/**
	 * 100平米
	 */
	private String size;
	/**
	 * 月租金
	 */
	private String monthMoney;
	/**
	 * 距离您10米
	 */
	private String distanceStr;
	/**
	 * 推荐标签
	 */
	private List<String> tags;
	/**
	 * 三室两厅一卫
	 */
	private String houseType;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public String getIndexImgUrl() {
		return indexImgUrl;
	}

	public void setIndexImgUrl(String indexImgUrl) {
		this.indexImgUrl = indexImgUrl;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	public String getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(String leaseType) {
		this.leaseType = leaseType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public String getDistanceStr() {
		return distanceStr;
	}

	public void setDistanceStr(String distanceStr) {
		this.distanceStr = distanceStr;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

}
