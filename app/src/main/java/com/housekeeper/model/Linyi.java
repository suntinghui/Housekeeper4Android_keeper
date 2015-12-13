package com.housekeeper.model;

import java.util.ArrayList;

/**
 * Created by sth on 10/29/15.
 */
public class Linyi {

    private static ArrayList<CityModel> list = new ArrayList<CityModel>();

    private static void initData() {
        if (list.isEmpty()) {
            list.add(new CityModel("临沂", "5"));
            list.add(new CityModel("兰山区", "12"));
            list.add(new CityModel("北城新区", "29"));
            list.add(new CityModel("罗庄区", "37"));
        }
    }

    public static String[] getCityNameList() {
        initData();

        String name[] = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            name[i] = list.get(i).getName();
        }

        return name;
    }

    public static String getCode(int location) {
        initData();

        return list.get(location).getCode();
    }
}
