package com.ares.house.dto.app;

import java.io.Serializable;

public class LinkArticle implements Serializable {

	private static final long serialVersionUID = 768113002317751767L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图片地址
	 */
	private String imgUrl;

	/**
	 * 链接地址
	 */
	private String link;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
