package com.housekeeper.model;

import java.util.ArrayList;

/**
 * Created by sth on 10/29/15.
 */
public class CityModel {

    private String name;
    private String code;

    public CityModel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
