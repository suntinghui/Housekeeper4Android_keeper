package com.ares.house.dto.app;

import java.io.Serializable;

public class RentContainAppDto implements Serializable {

	private static final long serialVersionUID = -4457870533808138148L;
	/**
	 * 标示
	 */
	private int id;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 图片路径
	 */
	private String img;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
