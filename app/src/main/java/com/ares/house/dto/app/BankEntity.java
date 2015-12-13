package com.ares.house.dto.app;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class BankEntity implements Serializable {

	private static final long serialVersionUID = 7127239024061411129L;
	/**
	 * 单笔限额
	 */
	private int single;
	/**
	 * 单日限额
	 */
	private int day;
	/**
	 * 月限额
	 */
	private int month;
	/**
	 * 银行名称
	 */
	private String name;
	/**
	 * 银行简码
	 */
	private String code;
	/**
	 * 银行图片名称
	 */
	private String img;
	/**
	 * 排序
	 */
	@JsonIgnore
	private int index;

	public int getSingle() {
		return single;
	}

	public void setSingle(int single) {
		this.single = single;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
