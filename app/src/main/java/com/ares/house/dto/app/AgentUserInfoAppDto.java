package com.ares.house.dto.app;

import java.io.Serializable;

public class AgentUserInfoAppDto implements Serializable {

	private static final long serialVersionUID = 5931157901712555524L;
	/**
	 * 中介姓名
	 */
	private String realName;
	/**
	 * 中介身份证号
	 */
	private String idCard;
	/**
	 * 中介公司名称
	 */
	private String companyName;
	/**
	 * 中介手机号
	 */
	private String telphone;
	/**
	 * 中介头像
	 */
	private String logoUrl;
	/**
	 * 公司地址
	 */
	private String companyAddress;
	/**
	 * 公司logo
	 */
	private String companyLogo;
	/**
	 * 身份证正面
	 */
	private String idCardFacade;
	/**
	 * 身份证正面
	 */
	private String idCardFacadeSource;
	/**
	 * 身份证反面
	 */
	private String idCardObverse;
	/**
	 * 身份证反面
	 */
	private String idCardObverseSource;
	/**
	 * 工作证件
	 */
	private String idCardHand;
	/**
	 * 工作证件
	 */
	private String idCardHandSource;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getIdCardFacade() {
		return idCardFacade;
	}

	public void setIdCardFacade(String idCardFacade) {
		this.idCardFacade = idCardFacade;
	}

	public String getIdCardObverse() {
		return idCardObverse;
	}

	public void setIdCardObverse(String idCardObverse) {
		this.idCardObverse = idCardObverse;
	}

	public String getIdCardHand() {
		return idCardHand;
	}

	public void setIdCardHand(String idCardHand) {
		this.idCardHand = idCardHand;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getIdCardFacadeSource() {
		return idCardFacadeSource;
	}

	public void setIdCardFacadeSource(String idCardFacadeSource) {
		this.idCardFacadeSource = idCardFacadeSource;
	}

	public String getIdCardObverseSource() {
		return idCardObverseSource;
	}

	public void setIdCardObverseSource(String idCardObverseSource) {
		this.idCardObverseSource = idCardObverseSource;
	}

	public String getIdCardHandSource() {
		return idCardHandSource;
	}

	public void setIdCardHandSource(String idCardHandSource) {
		this.idCardHandSource = idCardHandSource;
	}

}
