package com.ares.house.dto.app;

/**
 * 服务器返回状态码
 * 
 * @author sunshuai
 * 
 */
public enum AppResponseStatus {
	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 失败
	 */
	ERROR,
	/**
	 * 出现此种状态客户端直接跳到登录
	 */
	LOGIN,
	/**
	 * 完善个人信息
	 */
	SETINFO,
	/**
	 * 设置管家注册码
	 */
	SET_AGENT_CODE,
	/**
	 * 出现此种状态客户端直接跳到设置头像
	 */
	LOGO;

}
