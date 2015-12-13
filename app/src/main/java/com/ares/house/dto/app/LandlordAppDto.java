package com.ares.house.dto.app;

import java.util.List;

public class LandlordAppDto extends MyAppDto {
	private static final long serialVersionUID = -6631011057467879546L;
	/**
	 * 共收租金
	 */
	private String totalRent;
	/**
	 * 租金租期
	 */
	private int totalMonth;
	/**
	 * 贷款剩余金额
	 */
	private String loanMoney;

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

	public int getTotalMonth() {
		return totalMonth;
	}

	public void setTotalMonth(int totalMonth) {
		this.totalMonth = totalMonth;
	}

	public String getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(String loanMoney) {
		this.loanMoney = loanMoney;
	}

}
