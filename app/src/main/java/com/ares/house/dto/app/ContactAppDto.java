package com.ares.house.dto.app;

import java.util.List;

public class ContactAppDto {

	/**
	 * 公司电话
	 */
	private String telphone;

	private List<OpenCityAppDto> openCitys;

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public List<OpenCityAppDto> getOpenCitys() {
		return openCitys;
	}

	public void setOpenCitys(List<OpenCityAppDto> openCitys) {
		this.openCitys = openCitys;
	}

}
