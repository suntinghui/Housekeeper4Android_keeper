package com.ares.house.dto.app;

import java.io.Serializable;

public class ImageAppDto implements Serializable {

	private static final long serialVersionUID = -2376439758585483000L;
	/**
	 * 图片标示
	 */
	private int id;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 图片路径 前面拼服务器地址 不带rpc
	 */
	private String url;
	/**
	 * 原图地址
	 */
	private String sourceUrl;
	/**
	 * 点击链接地址
	 */
	private String linkUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

}
