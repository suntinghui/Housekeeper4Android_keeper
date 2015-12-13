package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 消息统计
 * 
 * @author sunshuai
 * 
 */
public class MessageCountAppDto implements Serializable {

	private static final long serialVersionUID = -7342084660221326999L;
	/**
	 * 消息记录总数
	 */
	private int total;
	/**
	 * 未读消息总数
	 */
	private int noRead;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNoRead() {
		return noRead;
	}

	public void setNoRead(int noRead) {
		this.noRead = noRead;
	}

}
