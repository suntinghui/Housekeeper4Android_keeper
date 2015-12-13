package com.housekeeper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class CalendarUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	// 获取本月第一天
	public static String getFirstDayOfThisMonth() {
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);//
		// lastDate.add(Calendar.MONTH,-1);//
		// lastDate.add(Calendar.DATE,-1);//

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取本月最后一天
	public static String getLastDayOfThisMonth() {
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);//
		lastDate.add(Calendar.MONTH, 1);//
		lastDate.add(Calendar.DATE, -1);//

		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return
	 */
	public static int getGapCount() {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(getFirstDayOfThisMonth());
			endDate = sdf.parse(getLastDayOfThisMonth());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	public static String getWeek(Date curDate) {
		String Week = "星期";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String pTime = format.format(curDate);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			Week += "天";
			break;
		case 2:
			Week += "一";
			break;
		case 3:
			Week += "二";
			break;
		case 4:
			Week += "三";
			break;
		case 5:
			Week += "四";
			break;
		case 6:
			Week += "五";
			break;
		case 7:
			Week += "六";
			break;
		default:
			break;
		}
		return Week;
	}

	/**
	 *  获取传过来的日期是周几
	 * @param curDate
	 * @return
	 */
	public static int getWeekInt(Date curDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 也可将此值当参数传进来
		String pTime = format.format(curDate);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.i("当前日期是周几：", c.get(Calendar.DAY_OF_WEEK) + "");
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
