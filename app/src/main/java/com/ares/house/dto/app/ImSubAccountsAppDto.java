package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * im 子账号信息
 * 
 * @author sunshuai
 * 
 */
public class ImSubAccountsAppDto implements Serializable {

	private static final long serialVersionUID = -6469198529039737501L;

	/**
	 * 子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 */
	private String subAccountSid;

	/**
	 * 子账户的授权令牌。由32个英文字母和阿拉伯数字组成
	 */
	private String subToken;

	/**
	 * VoIP号码。由14位数字组成；若应用为语音类，voipAccount 不为空；若应用为短信类，voipAccount 为空
	 */
	private String voipAccount;

	/**
	 * VoIP密码。由8位数字和字母组成；若应用为语音类，voipPwd不为空；若应用为短信类，voipPwd为空
	 */
	private String voipPwd;
	/**
	 * 用户真实姓名
	 */
	private String realName;

	/**
	 * 用户62进制标示
	 */
	private String userId;

	public String getSubAccountSid() {
		return subAccountSid;
	}

	public void setSubAccountSid(String subAccountSid) {
		this.subAccountSid = subAccountSid;
	}

	public String getSubToken() {
		return subToken;
	}

	public void setSubToken(String subToken) {
		this.subToken = subToken;
	}

	public String getVoipAccount() {
		return voipAccount;
	}

	public void setVoipAccount(String voipAccount) {
		this.voipAccount = voipAccount;
	}

	public String getVoipPwd() {
		return voipPwd;
	}

	public void setVoipPwd(String voipPwd) {
		this.voipPwd = voipPwd;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
