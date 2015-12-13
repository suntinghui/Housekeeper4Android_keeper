package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DebtPackageInfoAppDto implements Serializable {

	private static final long serialVersionUID = 7001355238098406485L;
	/**
	 * 债权包标示
	 */
	private int id;
	/**
	 * 类型 a定投 b抢投 c转让
	 */
	private char type;
	/**
	 * 债权编号
	 */
	private String num;
	/**
	 * 总金额
	 */
	private String totalMoney;
	/**
	 * 已售金额
	 */
	private String soldMoney;
	/**
	 * 剩余金额
	 */
	private String surplusMoney;
	/**
	 * 最大投资周期
	 */
	private int maxPeriod;
	/**
	 * 最小投资周期
	 */
	private int minPeriod;
	/**
	 * 预期年化最大收益率
	 */
	private String maxRate;
	/**
	 * 预期年化最小收益率
	 */
	private String minRate;
	/**
	 * 奖励收益
	 */
	private String rewardRate;
	/**
	 * 限额
	 */
	private String limitMoney;
	/**
	 * 每人限购几份 0不限制
	 */
	private int limitCount;
	/**
	 * 最大购买金额
	 */
	private String maxMoney;
	/**
	 * 已投资人数
	 */
	private int people;

	/**
	 * 转让的收购奖励
	 */
	private String reward;

	/**
	 * 抢投的预期年化收益率 day=投资周期天数 rate=预期年收益率
	 */
	private List<Map<String, String>> rates;
	/**
	 * 提前赎回返回利息的advance%
	 */
	private int advance;
	/**
	 * 是否可以购买
	 */
	private boolean isCanBuy;
	/**
	 * 用户真实姓名
	 */
	private String realName;
	/**
	 * 用户头像
	 */
	private String logoImg;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品价格
	 */
	private String goodsPrice;
	/**
	 * 用户信用额度
	 */
	private String credit;
	/**
	 * 组织机构
	 */
	private String organization;
	/**
	 * 商品购买时间
	 */
	private String buyTime;
	/**
	 * 分期期数
	 */
	private int month;
	/**
	 * 可用红包抵消
	 */
	private String useBonus;
	/**
	 * 分享给红包
	 */
	private String shareBonus;
	/**
	 * 分享赠送第三方担保
	 */
	private boolean isShareGuarantee;
	/**
	 * 分期用户标示
	 */
	private int fqUserId;
	/**
	 * 是否已经是好友
	 */
	private boolean isFriend;
	/**
	 * 起息时间
	 */
	private int tn;
	/**
	 * 多少天后可以转让
	 */
	private int transfer;
	/**
	 * 反息时间提示
	 */
	private String earningDayStr;
	/**
	 * 房管家名称
	 */
	private String agentUserName;
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
	 * 债权订单编号
	 */
	private String sourceNum;
	/**
	 * true跳转可以查看债权信息的那个新页面 false还是以前旧的
	 */
	private boolean isNew;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSoldMoney() {
		return soldMoney;
	}

	public void setSoldMoney(String soldMoney) {
		this.soldMoney = soldMoney;
	}

	public String getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(String surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public String getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(String limitMoney) {
		this.limitMoney = limitMoney;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public List<Map<String, String>> getRates() {
		return rates;
	}

	public void setRates(List<Map<String, String>> rates) {
		this.rates = rates;
	}

	public String getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}

	public String getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(String maxRate) {
		this.maxRate = maxRate;
	}

	public int getMaxPeriod() {
		return maxPeriod;
	}

	public void setMaxPeriod(int maxPeriod) {
		this.maxPeriod = maxPeriod;
	}

	public int getMinPeriod() {
		return minPeriod;
	}

	public void setMinPeriod(int minPeriod) {
		this.minPeriod = minPeriod;
	}

	public String getMinRate() {
		return minRate;
	}

	public void setMinRate(String minRate) {
		this.minRate = minRate;
	}

	public int getAdvance() {
		return advance;
	}

	public void setAdvance(int advance) {
		this.advance = advance;
	}

	public boolean isCanBuy() {
		return isCanBuy;
	}

	public void setCanBuy(boolean isCanBuy) {
		this.isCanBuy = isCanBuy;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getUseBonus() {
		return useBonus;
	}

	public void setUseBonus(String useBonus) {
		this.useBonus = useBonus;
	}

	public String getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(String shareBonus) {
		this.shareBonus = shareBonus;
	}

	public boolean isShareGuarantee() {
		return isShareGuarantee;
	}

	public void setShareGuarantee(boolean isShareGuarantee) {
		this.isShareGuarantee = isShareGuarantee;
	}

	public int getFqUserId() {
		return fqUserId;
	}

	public void setFqUserId(int fqUserId) {
		this.fqUserId = fqUserId;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public int getTn() {
		return tn;
	}

	public void setTn(int tn) {
		this.tn = tn;
	}

	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

	public String getRewardRate() {
		return rewardRate;
	}

	public void setRewardRate(String rewardRate) {
		this.rewardRate = rewardRate;
	}

	public String getEarningDayStr() {
		return earningDayStr;
	}

	public void setEarningDayStr(String earningDayStr) {
		this.earningDayStr = earningDayStr;
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

	public String getSourceNum() {
		return sourceNum;
	}

	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
