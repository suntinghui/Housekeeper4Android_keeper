package com.ares.house.dto.app;

import java.io.Serializable;

public class MessageListAppDto implements Serializable {

	private static final long serialVersionUID = -8283481622252934586L;

	/**
	 * 标示
	 */
	private int id;
	/**
	 * 类型分组 a系统消息 b投资消息 c好友消息
	 */
	private char type;
	/**
	 * 标题
	 */
	private java.lang.String title;
	/**
	 * 内容
	 */
	private java.lang.String content;
	/**
	 * 时间字符串
	 */
	private String time;
	/**
	 * 是否已读
	 */
	private boolean read;
	/**
	 * 跳转数据 json格式 a系统消息的时候是{"url":"{type}/{id}/html.app"}
	 */
	private String functionData;
	/**
	 * 跳转标示1定投列表 2抢投列表 3转让列表 4打开链接 5好友列表 6我的债权列表
	 */
	private int functionType;
	/**
	 * 消息logo
	 */
	private String logo;
	/**
	 * 消息图片
	 */
	private String img;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getContent() {
		return content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getFunctionData() {
		return functionData;
	}

	public void setFunctionData(String functionData) {
		this.functionData = functionData;
	}

	public int getFunctionType() {
		return functionType;
	}

	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
