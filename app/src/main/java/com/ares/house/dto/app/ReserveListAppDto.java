package com.ares.house.dto.app;

import java.io.Serializable;

public class ReserveListAppDto implements Serializable {

	private static final long serialVersionUID = -6295597732721899232L;
	/**
	 * 看房记录标示
	 */
	private int id;
	/**
	 * 房屋标示
	 */
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
	 * 三室两厅一卫
	 */
	private String houseType;
	/**
	 * 房管家或用户电话
	 */
	private String telphone;
	/**
	 * 预约时间
	 */
	private String addTimeStr;
	/**
	 * 用户头像
	 */
	private String userImg;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 房管家是否已处理
	 */
	private boolean handle;
	/**
	 * 意向星级
	 */
	private int stars;
	/**
	 * 用户真实姓名
	 */
	private String userName;

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

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDistanceStr() {
		return distanceStr;
	}

	public void setDistanceStr(String distanceStr) {
		this.distanceStr = distanceStr;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isHandle() {
		return handle;
	}

	public void setHandle(boolean handle) {
		this.handle = handle;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
