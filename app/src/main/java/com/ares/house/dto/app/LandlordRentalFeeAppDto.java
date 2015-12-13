package com.ares.house.dto.app;

import com.housekeeper.model.RentContainAppDtoEx;

import java.io.Serializable;
import java.util.List;

public class LandlordRentalFeeAppDto implements Serializable {

	private static final long serialVersionUID = 1278993958392981808L;
	/**
	 * 房屋标示
	 */
	private int houseId;
	/**
	 * 租期开始时间
	 */
	private long beginTime;
	/**
	 * 租赁年数
	 */
	private int yearCount;
	/**
	 * 年租金
	 */
	private String yearMoney;
	/**
	 * 每年让租期
	 */
	private int letLeaseDay;
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
	 * 违约扣除房东几个月租金
	 */
	private int violateMonth;
	/**
	 * 房租包含
	 */
	private List<RentContainAppDtoEx> rentContains;

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

	public int getYearCount() {
		return yearCount;
	}

	public void setYearCount(int yearCount) {
		this.yearCount = yearCount;
	}

	public String getYearMoney() {
		return yearMoney;
	}

	public void setYearMoney(String yearMoney) {
		this.yearMoney = yearMoney;
	}

	public int getLetLeaseDay() {
		return letLeaseDay;
	}

	public void setLetLeaseDay(int letLeaseDay) {
		this.letLeaseDay = letLeaseDay;
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

	public int getViolateMonth() {
		return violateMonth;
	}

	public void setViolateMonth(int violateMonth) {
		this.violateMonth = violateMonth;
	}

	public List<RentContainAppDtoEx> getRentContains() {
		return rentContains;
	}

	public void setRentContains(List<RentContainAppDtoEx> rentContains) {
		this.rentContains = rentContains;
	}

}
