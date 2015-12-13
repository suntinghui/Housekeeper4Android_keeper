package com.housekeeper.client;

import java.io.Serializable;
import java.text.DecimalFormat;

public class TransferInfo implements Serializable {

	private static final long serialVersionUID = 3178250554361779080L;

	private int id = 0; // 债权包标⽰
	private double transferMoney = 0.0; // 投资金额
	private double balanceAmount = 0.0; // 账户余额
	private boolean useBalance = false; // 是否使用余额支付

	// 银行名称与尾号，冗余信息，纯粹是为了取值方便
	private String bankName = "";
	private String tailNum = "";

	/**
	 * 
	 * @param id
	 *            债权包标⽰
	 * @param transferMoney
	 *            投资金额
	 * @param balanceAmount
	 *            账户余额
	 */
	public TransferInfo(int id, double transferMoney, double balanceAmount) {
		this.id = id;
		this.transferMoney = transferMoney;
		this.balanceAmount = balanceAmount;
	}

	public boolean shouldUserBankCard() {
		// 如果不使用余额支付，则肯定是直接银行卡支付
		if (!useBalance)
			return true;

		// 如果使用余额支付，判断投资金额与余额的大小。
		return transferMoney > balanceAmount;
	}

	public String getId() {
		return id + "";
	}

	public boolean isUseBalance() {
		return useBalance;
	}

	public void setUseBalance(boolean useBalance) {
		this.useBalance = useBalance;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTailNum() {
		return tailNum;
	}

	public void setTailNum(String tailNum) {
		this.tailNum = tailNum;
	}

	/**
	 * 取得投资金额
	 * 
	 * @return
	 */
	public String getTransferMoney() {
		return double2Money(transferMoney);
	}

	/**
	 * 取得账户余额
	 * 
	 * @return
	 */
	public String getBalanceMoney() {
		return double2Money(balanceAmount);
	}

	/**
	 * 计算还需要银行卡支付多少元
	 * 
	 * @return
	 */
	public double getSurplusMoney(boolean mUseBalance) {
		double surplus = 0.0;
		if (mUseBalance) {
			if (balanceAmount >= transferMoney) {
				surplus = 0.00;
			} else {
				surplus = (transferMoney - balanceAmount);
			}
		} else {
			surplus = transferMoney;
		}

		return surplus;
	}
	
	/**
	 * 账户余额支付金额
	 * @param mUseBalance
	 * @return
	 */
	public double getNeedBalance(boolean mUseBalance){
		double need = 0.00;
		if (mUseBalance){
			if (transferMoney >= balanceAmount){
				need = balanceAmount;
			} else {
				need = transferMoney;
			}
		} else {
			need = 0.00;
		}
		
		return need;
	}
	
	/**
	 * 账户余额支付金额
	 * @param mUseBalance
	 * @return
	 */
	public String getNeedBalanceStr(boolean mUseBalance){
		double need = this.getNeedBalance(mUseBalance);
		return double2Money(need);
	}
	
	

	/**
	 * 返回还需要银行卡支付多少元
	 * 
	 * @return
	 */
	public String getSurplusMoneyStr(boolean mUseBalance) {
		double surplus = this.getSurplusMoney(mUseBalance);
		return double2Money(surplus);
	}

	/**
	 * 计算投资后还有多少余额
	 * 
	 * @return
	 */
	public double getBalance(boolean mUseBalance) {
		double balance = 0.00;
		if (mUseBalance) {
			if (transferMoney >= balanceAmount) {
				balance = 0.00;
			} else {
				balance = (balanceAmount - transferMoney);
			}

		} else {
			balance = balanceAmount;
		}
		
		return balance;
	}
	
	/**
	 * 计算投资后还有多少余额
	 * 
	 * @return
	 */
	public String getBalanceStr(boolean mUseBalance){
		double balance = this.getBalance(mUseBalance);
		return double2Money(balance);
	}

	private String double2Money(double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(d);
	}

}
