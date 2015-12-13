package com.housekeeper.utils;

import java.math.BigDecimal;

public final class RateUtil {
	
	public static final BigDecimal PERCENTAGE = new BigDecimal(100);
	public static final BigDecimal MONTH_COUNT = new BigDecimal(12);
	public static final BigDecimal YEAR_DAY_COUNT = new BigDecimal(365);
	
	public static final int SCALE_MONEY_SAVE = 6;
	public static final int SCALE_RATE = 4;

	/**
	 * 计算年收益率
	 * 
	 * @param monthEarnings
	 *            月收益
	 * @param principal
	 *            本金
	 * @param month
	 *            周期月数
	 * @return
	 */
	// public static BigDecimal getYearReate(BigDecimal monthEarnings,
	// BigDecimal principal, int month) {
	// if (month > Constants.MONTH_COUNT.intValue()) {
	// monthEarnings = MathUtil.divDown(monthEarnings,
	// BigDecimal.valueOf(month), 20);
	// monthEarnings = MathUtil.mul(monthEarnings, Constants.MONTH_COUNT);
	// month = Constants.MONTH_COUNT.intValue();
	// }
	// // 年收益率=利息*100*12/投资月数/本金
	// BigDecimal tmp = MathUtil.mul(monthEarnings, Constants.PERCENTAGE);
	// tmp = MathUtil.mul(tmp, Constants.MONTH_COUNT);
	// tmp = MathUtil.div(tmp, BigDecimal.valueOf(month), 20);
	// return MathUtil.div(tmp, principal, Constants.SCALE_RATE);
	// }

	/**
	 * 计算月利息
	 * 
	 * @param yearRate
	 *            年收益率
	 * @param principal
	 *            本金
	 * @return
	 */
	public static BigDecimal getMonthEarnings(BigDecimal yearRate,
			BigDecimal principal) {
		// 月利息=年收益率*本金/12/100
		BigDecimal tmp = MathUtil.mul(yearRate, principal);
		tmp = MathUtil.div(tmp, MONTH_COUNT, 20);
		return MathUtil.divDown(tmp, PERCENTAGE,
				SCALE_MONEY_SAVE);
	}

	/**
	 * 计算天利息
	 * 
	 * @param yearRate
	 *            年收益率
	 * @param principal
	 *            本金
	 * @return
	 */
	public static BigDecimal getDayEarnings(BigDecimal yearRate,
			BigDecimal principal) {
		// 天利息=本金*年利率/365/100
		BigDecimal tmp = MathUtil.mul(principal, yearRate);
		tmp = MathUtil.div(tmp, YEAR_DAY_COUNT, 20);
		return MathUtil.divDown(tmp, PERCENTAGE,
				SCALE_MONEY_SAVE);
	}

	/**
	 * 计算总利息利息
	 * 
	 * @param yearRate
	 *            年利率
	 * @param principal
	 *            本金
	 * @param dayCount
	 *            投资天数
	 * @return
	 */
	public static BigDecimal getTotalEarnings(BigDecimal yearRate,
			BigDecimal principal, int dayCount) {
		// 天利息=本金*年利率*指定天数/365/100
		BigDecimal tmp = MathUtil.mul(principal, yearRate);
		tmp = MathUtil.mul(tmp, BigDecimal.valueOf(dayCount));
		tmp = MathUtil.div(tmp, YEAR_DAY_COUNT, 20);
		return MathUtil.divDown(tmp, PERCENTAGE,
				SCALE_MONEY_SAVE);
	}

	/**
	 * 获取年利率
	 * 
	 * @param totalEarnings
	 *            总利息
	 * @param principal
	 *            本金
	 * @param totalDay
	 *            投资天数
	 * @return
	 */
	public static BigDecimal getYearRate(BigDecimal totalEarnings,
			BigDecimal principal, int totalDay) {
		// 年利率=总利息*365*100/投资天数/本金
		BigDecimal tmp = MathUtil.mul(totalEarnings, YEAR_DAY_COUNT);
		tmp = MathUtil.mul(tmp, PERCENTAGE);
		tmp = MathUtil.div(tmp, BigDecimal.valueOf(totalDay), 20);
		return MathUtil.div(tmp, principal, SCALE_RATE);
	}

	public static void main(String[] args) {
		System.out.println(getYearRate(BigDecimal.valueOf(49.29),
				BigDecimal.valueOf(2999), 100));
		System.out.println(getYearRate(BigDecimal.valueOf(2.46),
				BigDecimal.valueOf(100), 150));

	}
}
