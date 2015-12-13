package com.ares.house.dto.app;

import java.io.Serializable;

public class HouseTagAppDto implements Serializable {

	private static final long serialVersionUID = 6724065017227497435L;

	/**
	 * 标签标示
	 */
	private int id;

	/**
	 * 标签名称
	 */
	private String name;
	/**
	 * 是否选中
	 */
	private boolean selected;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return name;
	}
}
