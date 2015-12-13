package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 每条结果对应的状态码和消息
 * 
 * @author sunshuai
 * 
 */
public class AppMessageDto<T> implements Serializable {

	private static final long serialVersionUID = -7944603329525596553L;

	/**
	 * 状态码
	 */
	private AppResponseStatus status = AppResponseStatus.ERROR;
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 用于后台返回数据用
	 */
	private T data;

	public AppResponseStatus getStatus() {
		return status;
	}

	public void setStatus(AppResponseStatus status) {
		this.status = status;
	}

	public String getMsg() {
		if (msg == null) {
			msg = "";
		}
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
