package com.ares.house.dto.app;

import java.io.Serializable;

public class MessageAppDto implements Serializable {

	private static final long serialVersionUID = 2620721089887536885L;

	/**
	 * 未读数量
	 */
	private int noReadCount;

	/**
	 * 消息列表数据
	 */
	private Paginable<MessageListAppDto> data;

	public int getNoReadCount() {
		return noReadCount;
	}

	public void setNoReadCount(int noReadCount) {
		this.noReadCount = noReadCount;
	}

	public Paginable<MessageListAppDto> getData() {
		return data;
	}

	public void setData(Paginable<MessageListAppDto> data) {
		this.data = data;
	}

}
