package com.ares.house.dto.app;

import com.housekeeper.model.EquipmentAppDtoEx;

import java.io.Serializable;
import java.util.List;

public class HouseInfoAppDto implements Serializable {

	private static final long serialVersionUID = -5304320919853543893L;
	/**
	 * 房屋标示
	 */
	private int id;
	/**
	 * 门牌号
	 */
	private String houseNum;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 小区名称
	 */
	private String community;
	/**
	 * 省份名称
	 */
	private String provinceStr;
	/**
	 * 省份标示
	 */
	private int provinceId;
	/**
	 * 城市名称
	 */
	private String cityStr;
	/**
	 * 城市标示
	 */
	private int cityId;
	/**
	 * 区名称
	 */
	private String areaStr;
	/**
	 * 区标示
	 */
	private int areaId;
	/**
	 * 房型
	 */
	private String houseType;
	/**
	 * 面积
	 */
	private int areaSize;
	/**
	 * 楼层
	 */
	private String floor;
	/**
	 * 装修
	 */
	private String decorate;
	/**
	 * 朝向
	 */
	private String orientation;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 头部图片
	 */
	private List<ImageAppDto> topImages;
	/**
	 * 房间配置
	 */
	private List<EquipmentAppDtoEx> equipments;
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
	 * 租金包含
	 */
	private List<RentContainAppDto> rentContains;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCityStr() {
		return cityStr;
	}

	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public int getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(int areaSize) {
		this.areaSize = areaSize;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProvinceStr() {
		return provinceStr;
	}

	public void setProvinceStr(String provinceStr) {
		this.provinceStr = provinceStr;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public List<ImageAppDto> getTopImages() {
		return topImages;
	}

	public void setTopImages(List<ImageAppDto> topImages) {
		this.topImages = topImages;
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

	public List<RentContainAppDto> getRentContains() {
		return rentContains;
	}

	public void setRentContains(List<RentContainAppDto> rentContains) {
		this.rentContains = rentContains;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
