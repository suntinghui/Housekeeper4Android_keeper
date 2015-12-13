package com.ares.house.dto.app;

import java.io.Serializable;

/**
 * 付款信息
 * 
 * @author sunshuai
 * 
 */
public class PaymentTypeAppDto implements Serializable {

	private static final long serialVersionUID = 5291837646706176704L;
	/**
	 * 可用余额
	 */
	private String surplus;
	/**
	 * 不使用余额支付时 需要银行卡支付金额
	 */
	private String noUseSurplus;
	/**
	 * 使用余额支付时 需要银行卡支付金额
	 */
	private String useSurplus;

	public String getSurplus() {
		return surplus;
	}

	public void setSurplus(String surplus) {
		this.surplus = surplus;
	}

	public String getNoUseSurplus() {
		return noUseSurplus;
	}

	public void setNoUseSurplus(String noUseSurplus) {
		this.noUseSurplus = noUseSurplus;
	}

	public String getUseSurplus() {
		return useSurplus;
	}

	public void setUseSurplus(String useSurplus) {
		this.useSurplus = useSurplus;
	}

}
