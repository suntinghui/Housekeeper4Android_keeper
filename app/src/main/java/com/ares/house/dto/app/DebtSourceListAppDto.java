package com.ares.house.dto.app;

import java.io.Serializable;

public class DebtSourceListAppDto implements Serializable {

	private static final long serialVersionUID = 2122134067328121354L;
	/**
	 * 债权标示
	 */
	private int debtId;
	/**
	 * 用户头像
	 */
	private String logoImg;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 组织机构
	 */
	private String organization;
	/**
	 * 来源编号
	 */
	private String sourceNum;
	/**
	 * 债权来源类型
	 */
	private String sourceType;
	/**
	 * 债权来源类型名称
	 */
	private String sourceTypeStr;
	/**
	 * 债权来源类型说明
	 */
	private String sourceTypeInfo;
	/**
	 * 债权金额
	 */
	private String credit;
	/**
	 * true跳转可以查看债权信息的那个新页面 false还是以前旧的
	 */
	private boolean isNew;

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getSourceNum() {
		return sourceNum;
	}

	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}

	public int getDebtId() {
		return debtId;
	}

	public void setDebtId(int debtId) {
		this.debtId = debtId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceTypeStr() {
		return sourceTypeStr;
	}

	public void setSourceTypeStr(String sourceTypeStr) {
		this.sourceTypeStr = sourceTypeStr;
	}

	public String getSourceTypeInfo() {
		return sourceTypeInfo;
	}

	public void setSourceTypeInfo(String sourceTypeInfo) {
		this.sourceTypeInfo = sourceTypeInfo;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
