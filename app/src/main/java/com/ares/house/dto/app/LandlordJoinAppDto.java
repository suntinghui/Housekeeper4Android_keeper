package com.ares.house.dto.app;

import java.io.Serializable;

public class LandlordJoinAppDto implements Serializable {

	private static final long serialVersionUID = 5333913445489972969L;

	private int houseId;
	/**
	 * 房东头像
	 */
	private String logo;
	/**
	 * 房东电话
	 */
	private String telphone;
	/**
	 * 银行卡号
	 */
	private String bankCard;
	/**
	 * 房东是否已经扫码
	 */
	private boolean landlordJoin;
	/**
	 * 是否已经提交关联
	 */
	private boolean join;
	/**
	 * 房东关联码
	 */
	private String landlordJoinCode;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public boolean isLandlordJoin() {
		return landlordJoin;
	}

	public void setLandlordJoin(boolean landlordJoin) {
		this.landlordJoin = landlordJoin;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	public String getLandlordJoinCode() {
		return landlordJoinCode;
	}

	public void setLandlordJoinCode(String landlordJoinCode) {
		this.landlordJoinCode = landlordJoinCode;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
}
