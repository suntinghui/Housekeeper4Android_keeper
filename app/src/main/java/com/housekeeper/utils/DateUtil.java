package com.housekeeper.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String second2Time(long second) {
        try {
            if (second < 0)
                return "-- : -- : --";

            long hour = second / 3600;
            second = second % 3600;
            long min = second / 60;
            second = second % 60;
            long sec = second;

            String temp = String.format("%02d : %02d : %02d", hour, min, sec);
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "-- : -- : --";
    }

    public static String getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(today);
    }

    public static String getCurrentDate2() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    public static String getDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String getDate2(long time) {
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 如果出现异常则返回当前日期
     *
     * @param yyyyMMdd
     * @return
     */
    public static long string2MilliSec(String yyyyMMdd) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
            long milliseconds = date.getTime();
            return milliseconds;
        } catch (Exception e) {
            e.printStackTrace();

            return System.currentTimeMillis();
        }
    }

    public static ArrayList<String> getYaoYAO(String currentDate, int yearCount, int addDays) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(currentDate);

        try {
            for (int i = 1; i < yearCount; i++) {
                Calendar cal = Calendar.getInstance();

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i - 1));
                cal.setTime(date);

                cal.add(Calendar.YEAR, 1);
                cal.add(Calendar.DAY_OF_YEAR, addDays);

                list.add(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


}
