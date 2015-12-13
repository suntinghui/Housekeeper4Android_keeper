package com.housekeeper.utils;

import com.ares.house.dto.app.TreeNodeAppDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IdentityUtil {

	private static IdentityUtil instance = null;

	private ArrayList<String> province_list = new ArrayList<String>();
	private ArrayList<String> city_list = new ArrayList<String>();
	private ArrayList<String> couny_list = new ArrayList<String>();
	public ArrayList<Integer> province_list_code = new ArrayList<Integer>();
	public ArrayList<Integer> city_list_code = new ArrayList<Integer>();
	public ArrayList<Integer> couny_list_code = new ArrayList<Integer>();

	public static IdentityUtil getInstance() {
		if (instance == null) {
			instance = new IdentityUtil();
		}

		return instance;
	}

	// 得到第一层级列表
	public List<TreeNodeAppDto> getFirstList(TreeNodeAppDto dto) {
		return dto.getChilds();
	}

	// 取得所有的第二层级列表
	public HashMap<Integer, List<TreeNodeAppDto>> getSecondMap(TreeNodeAppDto dto) {
		HashMap<Integer, List<TreeNodeAppDto>> map = new HashMap<Integer, List<TreeNodeAppDto>>();

		List<TreeNodeAppDto> firstList = this.getFirstList(dto);

		for (TreeNodeAppDto tempDto : firstList) {
			map.put(tempDto.getId(), tempDto.getChilds());
		}
		return map;
	}

	// 取得所有的第三层级列表
	public HashMap<Integer, List<TreeNodeAppDto>> getThirdList(HashMap<Integer, List<TreeNodeAppDto>> secondMap) {
		HashMap<Integer, List<TreeNodeAppDto>> map = new HashMap<Integer, List<TreeNodeAppDto>>();

		for (Integer i : secondMap.keySet()) {
			List<TreeNodeAppDto> tmepList = secondMap.get(i);
			for (TreeNodeAppDto tempDto : tmepList) {
				map.put(tempDto.getId(), tempDto.getChilds());
			}
		}

		return map;
	}

	public ArrayList<Integer> getProvince_list_code() {
		return province_list_code;
	}

	public ArrayList<Integer> getCity_list_code() {
		return city_list_code;
	}

	public void setCity_list_code(ArrayList<Integer> city_list_code) {
		this.city_list_code = city_list_code;
	}

	public ArrayList<Integer> getCouny_list_code() {
		return couny_list_code;
	}

	public void setCouny_list_code(ArrayList<Integer> couny_list_code) {
		this.couny_list_code = couny_list_code;
	}

	public void setProvince_list_code(ArrayList<Integer> province_list_code) {

		this.province_list_code = province_list_code;
	}

	public ArrayList<String> getProvince(List<TreeNodeAppDto> provice) {
		if (province_list_code.size() > 0) {
			province_list_code.clear();
		}
		if (province_list.size() > 0) {
			province_list.clear();
		}
		for (int i = 0; i < provice.size(); i++) {
			province_list.add(provice.get(i).getName());
			province_list_code.add(provice.get(i).getId());
		}
		return province_list;

	}

	public ArrayList<String> getCity(HashMap<Integer, List<TreeNodeAppDto>> cityHashMap, int provicecode) {
		if (city_list_code.size() > 0) {
			city_list_code.clear();
		}
		if (city_list.size() > 0) {
			city_list.clear();
		}
		List<TreeNodeAppDto> city = new ArrayList<TreeNodeAppDto>();
		city = cityHashMap.get(provicecode);
		System.out.println("city--->" + city.toString());
		for (int i = 0; i < city.size(); i++) {
			city_list.add(city.get(i).getName());
			city_list_code.add(city.get(i).getId());
		}
		return city_list;

	}

	public ArrayList<String> getCouny(HashMap<Integer, List<TreeNodeAppDto>> cityHashMap, int citycode) {
		System.out.println("citycode" + citycode);
		List<TreeNodeAppDto> couny = null;
		if (couny_list_code.size() > 0) {
			couny_list_code.clear();

		}
		if (couny_list.size() > 0) {
			couny_list.clear();
		}
		if (couny == null) {
			couny = new ArrayList<TreeNodeAppDto>();
		} else {
			couny.clear();
		}

		couny = cityHashMap.get(citycode);
		System.out.println("couny--->" + couny.toString());
		for (int i = 0; i < couny.size(); i++) {
			couny_list.add(couny.get(i).getName());
			couny_list_code.add(couny.get(i).getId());
		}
		return couny_list;

	}

}
