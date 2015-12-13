package com.housekeeper.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtil {

	public static final BigDecimal PERCENTAGE = new BigDecimal(100);
	public static final BigDecimal MONTH_COUNT = new BigDecimal(12);
	public static final BigDecimal YEAR_DAY_COUNT = new BigDecimal(365);

	public static final int SCALE_MONEY_SAVE = 6;
	public static final int SCALE_RATE = 4;
	public static final int SCALE_MONEY = 2;

	private MathUtil() {
	};

	/**
	 * @param v1
	 * @param v2
	 * @return BigDecimal，其值为 (v1 + v2+...)，其标度为 max(values.scale())。
	 */
	public static BigDecimal add(BigDecimal... values) {
		if (values == null || values.length == 0) {
			return new BigDecimal(0);
		} else if (values.length == 1) {
			return values[0];
		} else {
			BigDecimal res = values[0];
			for (int i = 1; i < values.length; i++) {
				res = res.add(values[i]);
			}
			return res;
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return BigDecimal，其值为 (v1 + v2)，其标度为 scale。
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null && v2 == null) {
			return new BigDecimal(0);
		} else if (v1 == null) {
			return v2.setScale(scale, RoundingMode.HALF_UP);
		} else if (v2 == null) {
			return v1.setScale(scale, RoundingMode.HALF_UP);
		} else {
			return v1.add(v2).setScale(scale, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 - v2)，其标度为 max(v1.scale(), v2.scale())。
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		if (v1 == null && v2 == null) {
			return new BigDecimal(0);
		} else if (v1 == null) {
			return new BigDecimal(0).subtract(v2);
		} else if (v2 == null) {
			return v1;
		} else {
			return v1.subtract(v2);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 - v2)，其标度为 scale。
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null && v2 == null) {
			return new BigDecimal(0);
		} else if (v1 == null) {
			return new BigDecimal(0).subtract(v2).setScale(scale, RoundingMode.HALF_UP);
		} else if (v2 == null) {
			return v1.setScale(scale, RoundingMode.HALF_UP);
		} else {
			return v1.subtract(v2).setScale(scale, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 × v2)，其标度为 (v1.scale() + v2.scale())。
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.multiply(v2);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 × v2)，其标度为 scale。
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.multiply(v2).setScale(scale, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 × v2)，其标度为 scale。
	 */
	public static BigDecimal mulDown(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.multiply(v2).setScale(scale, RoundingMode.DOWN);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 / v2)，其标度为 v1.scale()。
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.divide(v2, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 / v2)，其标度为 v1.scale() RoundingMode.DOWN
	 */
	public static BigDecimal divDown(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.divide(v2, RoundingMode.DOWN);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 / v2)，其标度为 scale RoundingMode.DOWN
	 */
	public static BigDecimal divDown(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.divide(v2, scale, RoundingMode.DOWN);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 / v2)，其标度为 scale。
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.divide(v2, scale, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回一个 BigDecimal，其值为 (v1 / v2)，其标度为 scale。
	 */
	public static BigDecimal divUp(BigDecimal v1, BigDecimal v2, int scale) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		} else {
			return v1.divide(v2, scale, RoundingMode.UP);
		}
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1小于v2 true
	 */
	public static boolean lt(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return false;
		}
		return v1.compareTo(v2) < 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1大于v2 true
	 */
	public static boolean gt(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return false;
		}
		return v1.compareTo(v2) > 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1等于v2 true
	 */
	public static boolean eq(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2 == null) {
			return false;
		}
		return v1.compareTo(v2) == 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1小于v2 true
	 */
	public static boolean lt(BigDecimal v1, int v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) < 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1大于v2 true
	 */
	public static boolean gt(BigDecimal v1, int v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) > 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1等于v2 true
	 */
	public static boolean eq(BigDecimal v1, int v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) == 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1小于v2 true
	 */
	public static boolean lt(BigDecimal v1, double v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) < 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1大于v2 true
	 */
	public static boolean gt(BigDecimal v1, double v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) > 0;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return v1等于v2 true
	 */
	public static boolean eq(BigDecimal v1, double v2) {
		if (v1 == null) {
			return false;
		}
		return v1.compareTo(new BigDecimal(v2)) == 0;
	}

	/**
	 * 货币类型数字格式化 小数点精确到Constants.SCALE_MONEY位
	 * 
	 * @param str
	 * @return
	 */
	public static BigDecimal moneyFormat(BigDecimal v) {
		if (v == null) {
			return new BigDecimal(0).setScale(SCALE_MONEY);
		}
		return v.setScale(SCALE_MONEY, RoundingMode.DOWN);
	}

	/**
	 * 货币类型数字格式化 小数点精确到SCALE_MONEY位
	 * 
	 * @param v
	 * @return
	 */
	public static BigDecimal moneyFormatDown(BigDecimal v) {
		if (v == null) {
			return new BigDecimal(0).setScale(SCALE_MONEY);
		}
		return v.setScale(SCALE_MONEY, RoundingMode.DOWN);
	}

	public static String money2String(BigDecimal v) {
		v = moneyFormat(v);
		if (lt(v, 9999.99)) {
			return v.toString();
		} else if (lt(v, 99999999.99)) {
			return div(v, new BigDecimal(10000)).toString() + "万";
		} else {
			return div(v, new BigDecimal(100000000)).toString() + "亿";
		}
	}

	public static BigDecimal rand(BigDecimal min, BigDecimal max) {
		int scale = Math.max(min.scale(), max.scale());
		max = mul(new BigDecimal(Math.random()), sub(max, min));
		max = add(max, min);
		max = max.setScale(scale, RoundingMode.HALF_UP);
		return max;
	}

	/**
	 * 取模
	 * 
	 * @param v1
	 * @param v2
	 * @return v1 % v2
	 * @throws Exception
	 */
	public static BigDecimal modulo(BigDecimal v1, BigDecimal v2) throws Exception {
		if (v1 == null || v2 == null) {
			throw new Exception("not null params");
		}
		return v1.divideAndRemainder(v2)[1];
	}

	// public static BigDecimal removeZero(BigDecimal value) {
	// return new BigDecimal(value.toString().replaceAll("[0]*$", ""));
	// }

	public static void main(String[] args) {
		BigDecimal bg = BigDecimal.valueOf(10.2);
		BigDecimal bg2 = BigDecimal.valueOf(20.5);
		System.out.println(bg2.divideAndRemainder(bg)[1]);
		// System.out.println(removeZero(new
		// BigDecimal("230023002000.000000")));
	}
}
