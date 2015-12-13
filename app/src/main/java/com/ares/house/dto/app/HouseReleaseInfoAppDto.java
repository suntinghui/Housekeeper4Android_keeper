package com.ares.house.dto.app;

import com.housekeeper.model.EquipmentAppDtoEx;

import java.io.Serializable;
import java.util.List;

public class HouseReleaseInfoAppDto implements Serializable {

	private static final long serialVersionUID = -3689807603512853385L;

	private int houseId;
	/**
	 * 顶部图片
	 */
	private List<ImageAppDto> topImages;

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
	 * 推荐标签
	 */
	private List<String> tags;
	/**
	 * 一室一厅一卫
	 */
	private String houseType;
	/**
	 * 装修
	 */
	private String decorate;
	/**
	 * 朝向
	 */
	private String orientation;
	/**
	 * 楼层
	 */
	private String floor;
	/**
	 * 入住时间
	 */
	private String leaseTimeStr;
	/**
	 * 房间配置
	 */
	private List<EquipmentAppDtoEx> equipments;
	/**
	 * 租金包含
	 */
	private List<RentContainAppDto> rentContains;
	/**
	 * 取暖费
	 */
	private String heatingFeesMoney;
	/**
	 * false 房东交取暖费
	 */
	private boolean heatingFees;
	/**
	 * 物业费
	 */
	private String propertyFeesMoney;
	/**
	 * false 房东交物业费
	 */
	private boolean propertyFees;
	/**
	 * 公交
	 */
	private String bus;
	/**
	 * 地铁
	 */
	private String subway;
	/**
	 * 是否可以添加到选房清单 true可以
	 */
	private boolean add;
	/**
	 * 已预约看房数量
	 */
	private int reserveCount;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public List<ImageAppDto> getTopImages() {
		return topImages;
	}

	public void setTopImages(List<ImageAppDto> topImages) {
		this.topImages = topImages;
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

	public String getDecorate() {
		return decorate;
	}

	public void setDecorate(String decorate) {
		this.decorate = decorate;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getLeaseTimeStr() {
		return leaseTimeStr;
	}

	public void setLeaseTimeStr(String leaseTimeStr) {
		this.leaseTimeStr = leaseTimeStr;
	}

	public List<EquipmentAppDtoEx> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentAppDtoEx> equipments) {
		this.equipments = equipments;
	}

	public String getHeatingFeesMoney() {
		return heatingFeesMoney;
	}

	public void setHeatingFeesMoney(String heatingFeesMoney) {
		this.heatingFeesMoney = heatingFeesMoney;
	}

	public boolean isHeatingFees() {
		return heatingFees;
	}

	public void setHeatingFees(boolean heatingFees) {
		this.heatingFees = heatingFees;
	}

	public String getPropertyFeesMoney() {
		return propertyFeesMoney;
	}

	public void setPropertyFeesMoney(String propertyFeesMoney) {
		this.propertyFeesMoney = propertyFeesMoney;
	}

	public boolean isPropertyFees() {
		return propertyFees;
	}

	public void setPropertyFees(boolean propertyFees) {
		this.propertyFees = propertyFees;
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

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public int getReserveCount() {
		return reserveCount;
	}

	public void setReserveCount(int reserveCount) {
		this.reserveCount = reserveCount;
	}

	public List<RentContainAppDto> getRentContains() {
		return rentContains;
	}

	public void setRentContains(List<RentContainAppDto> rentContains) {
		this.rentContains = rentContains;
	}
}
