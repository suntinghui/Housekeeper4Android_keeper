package com.ares.house.dto.app;

public class MyAppAgentDto extends MyAppDto {

	private static final long serialVersionUID = -9060328223929093337L;
	/**
	 * 管理房源总数
	 */
	private int houseCount;
	/**
	 * 共收租金
	 */
	private String totalRent;
	/**
	 * 租赁中数量
	 */
	private int leaseCount;
	/**
	 * 代收租金
	 */
	private String totalWaitRent;
	/**
	 * 待租中
	 */
	private int waitLeaseCount;

	public int getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(int houseCount) {
		this.houseCount = houseCount;
	}

	public String getTotalRent() {
		return totalRent;
	}

	public void setTotalRent(String totalRent) {
		this.totalRent = totalRent;
	}

	public int getLeaseCount() {
		return leaseCount;
	}

	public void setLeaseCount(int leaseCount) {
		this.leaseCount = leaseCount;
	}

	public String getTotalWaitRent() {
		return totalWaitRent;
	}

	public void setTotalWaitRent(String totalWaitRent) {
		this.totalWaitRent = totalWaitRent;
	}

	public int getWaitLeaseCount() {
		return waitLeaseCount;
	}

	public void setWaitLeaseCount(int waitLeaseCount) {
		this.waitLeaseCount = waitLeaseCount;
	}

}
