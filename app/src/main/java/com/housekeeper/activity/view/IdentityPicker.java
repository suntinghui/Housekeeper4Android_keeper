package com.housekeeper.activity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ares.house.dto.app.TreeNodeAppDto;
import com.housekeeper.utils.IdentityUtil;
import com.wufriends.housekeeper.keeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户身份Picker
 * 
 * @author zd
 * 
 */
public class IdentityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;

	private List<TreeNodeAppDto> province_list = new ArrayList<TreeNodeAppDto>();
	private HashMap<Integer, List<TreeNodeAppDto>> city_map = new HashMap<Integer, List<TreeNodeAppDto>>();
	private HashMap<Integer, List<TreeNodeAppDto>> couny_map = new HashMap<Integer, List<TreeNodeAppDto>>();

	private int city_code_string;
	private String city_string;

	public IdentityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IdentityPicker(Context context) {
		super(context);
	}

	// 获取城市信息
	public void setData(TreeNodeAppDto dto) {
		if (dto == null)
			return;

		province_list = IdentityUtil.getInstance().getFirstList(dto);
		city_map = IdentityUtil.getInstance().getSecondMap(dto);
		couny_map = IdentityUtil.getInstance().getThirdList(city_map);

		init();
	}

	private void init() {

		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		provincePicker.setData(IdentityUtil.getInstance().getProvince(province_list));
		provincePicker.setDefault(0);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		cityPicker.setData(IdentityUtil.getInstance().getCity(city_map, IdentityUtil.getInstance().getProvince_list_code().get(0)));
		cityPicker.setDefault(0);

		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		counyPicker.setData(IdentityUtil.getInstance().getCouny(couny_map, IdentityUtil.getInstance().getCity_list_code().get(0)));
		counyPicker.setDefault(0);

		city_code_string = IdentityUtil.getInstance().getCouny_list_code().get(0);

		provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;

				if (tempProvinceIndex != id) {
					System.out.println("endselect");
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;

					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;

					// 城市数组
					cityPicker.setData(IdentityUtil.getInstance().getCity(city_map, IdentityUtil.getInstance().getProvince_list_code().get(id)));
					cityPicker.setDefault(0);

					counyPicker.setData(IdentityUtil.getInstance().getCouny(couny_map, IdentityUtil.getInstance().getCity_list_code().get(0)));
					counyPicker.setDefault(0);

					city_code_string = IdentityUtil.getInstance().getCouny_list_code().get(0);
					int lastDay = Integer.valueOf(provincePicker.getListSize());
					if (id > lastDay) {
						provincePicker.setDefault(lastDay - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});

		cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					counyPicker.setData(IdentityUtil.getInstance().getCouny(couny_map, IdentityUtil.getInstance().getCity_list_code().get(id)));
					counyPicker.setDefault(0);
					city_code_string = IdentityUtil.getInstance().getCouny_list_code().get(0);
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});

		counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub

				if (text.equals("") || text == null)
					return;
				if (tempCounyIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = cityPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
					city_code_string = IdentityUtil.getInstance().getCouny_list_code().get(id);
					int lastDay = Integer.valueOf(counyPicker.getListSize());
					if (id > lastDay) {
						counyPicker.setDefault(lastDay - 1);
					}
				}
				tempCounyIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public int getCity_code_string() {
		return city_code_string;
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}

	public void setSelect(int code) {
		city_code_string = code;

		int provinceId = 0;
		int cityId = 0;
		int countryId = code;

		first: for (Integer i : couny_map.keySet()) {
			List<TreeNodeAppDto> list = couny_map.get(i);
			for (TreeNodeAppDto tempDto : list) {
				if (tempDto.getId() == code) {
					cityId = i;
					break first;
				}
			}
		}

		second: for (Integer i : city_map.keySet()) {
			List<TreeNodeAppDto> list = city_map.get(i);
			for (TreeNodeAppDto tempDto : list) {
				if (tempDto.getId() == cityId) {
					provinceId = i;
					break second;
				}
			}
		}

		provincePicker.setData(IdentityUtil.getInstance().getProvince(province_list));
		int i = 0;
		for (i = 0; i < province_list.size(); i++) {
			if (province_list.get(i).getId() == provinceId) {
				provincePicker.setDefault(i);
				break;
			}

		}

		int j = 0;
		cityPicker.setData(IdentityUtil.getInstance().getCity(city_map, IdentityUtil.getInstance().getProvince_list_code().get(i)));
		List<TreeNodeAppDto> t_city = city_map.get(provinceId);
		for (j = 0; j < t_city.size(); j++) {
			if (t_city.get(j).getId() == cityId) {
				cityPicker.setDefault(j);
				break;
			}

		}

		List<TreeNodeAppDto> t_couny = couny_map.get(cityId);
		counyPicker.setData(IdentityUtil.getInstance().getCouny(couny_map, IdentityUtil.getInstance().getCity_list_code().get(j)));
		for (int k = 0; k < t_couny.size(); k++) {
			if (t_couny.get(k).getId() == countryId) {
				counyPicker.setDefault(k);
				break;
			}

		}
	}
}
