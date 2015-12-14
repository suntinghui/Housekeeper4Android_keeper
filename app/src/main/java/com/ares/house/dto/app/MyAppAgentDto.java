package com.ares.house.dto.app;

public class MyAppAgentDto extends MyAppDto {

	private static final long serialVersionUID = -9060328223929093337L;
	/**
	 * 管理房源总数
	 */
	private int houseCount;
	/**
	 * 共收佣金
	 */
	private String totalCommission;

	public int getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(int houseCount) {
		this.houseCount = houseCount;
	}

	public String getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(String totalCommission) {
		this.totalCommission = totalCommission;
	}

}
