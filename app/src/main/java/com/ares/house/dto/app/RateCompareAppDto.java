package com.ares.house.dto.app;

import java.io.Serializable;

public class RateCompareAppDto implements Serializable {

	private static final long serialVersionUID = 6155047547602658250L;

	/**
	 * 日期
	 */
	private String date;

	/**
	 * 收益率
	 */
	private String value;
	/**
	 * 对比名称
	 */
	private String name;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
