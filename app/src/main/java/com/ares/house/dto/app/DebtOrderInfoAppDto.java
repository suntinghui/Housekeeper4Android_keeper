package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.List;

public class DebtOrderInfoAppDto implements Serializable {

	private static final long serialVersionUID = -7169492229548064040L;
	/**
	 * 资料详情
	 */
	private String infoUrl;
	/**
	 * 还款保障
	 */
	private List<String> guarantee;
	/**
	 * 房管家名称
	 */
	private String agentUserName;
	/**
	 * 债权来源类型
	 */
	private String sourceType;
	/**
	 * 总金额
	 */
	private String totalMoney;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 公司logo
	 */
	private String companyLogo;
	/**
	 * 债权编码
	 */
	private String num;
	/**
	 * 债权来源类型名称
	 */
	private String sourceTypeStr;
	/**
	 * 债权来源类型说明
	 */
	private String sourceTypeInfo;

	public String getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public List<String> getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(List<String> guarantee) {
		this.guarantee = guarantee;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

}
