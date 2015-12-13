package com.housekeeper.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ares.house.dto.app.TreeNodeAppDto;
import com.wufriends.housekeeper.keeper.R;

import java.util.List;

/**
 * Created by sth on 8/16/15.
 */
public class SelectCityLayout extends LinearLayout implements View.OnClickListener {

    private Context context;

    private TextView provinceTextView;
    private TextView cityTextView;

    private Spinner provinceSpinner;
    private Spinner citySpinner;

    private int provinceSelectedIndex = 0;

    private int citySelectedIndex = 0;

    private int selectedAreaId = 0; // 服务器需要的选择的城市Id

    private List<TreeNodeAppDto> dtoList = null;

    public SelectCityLayout(Context context) {
        super(context);

        this.initView(context);
    }

    public SelectCityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_select_city, this);

        provinceTextView = (TextView) this.findViewById(R.id.provinceTextView);
        provinceTextView.setOnClickListener(this);
        provinceTextView.setText("北京");

        cityTextView = (TextView) this.findViewById(R.id.cityTextView);
        cityTextView.setOnClickListener(this);
        cityTextView.setText("北京");

        provinceSpinner = (Spinner) this.findViewById(R.id.provinceSpinner);

        citySpinner = (Spinner) this.findViewById(R.id.citySpinner);
    }

    public void setData(TreeNodeAppDto dto) {
        this.dtoList = dto.getChilds();

        provinceTextView.setText(dtoList.get(0).getName());

        cityTextView.setText(dtoList.get(0).getChilds().get(0).getName());

        selectedAreaId = dtoList.get(0).getChilds().get(0).getId();
    }

    public void setSelectedInfo(String provinceId, String cityId) {
        if (this.dtoList == null)
            return;

        try {
            for (int i = 0; i < this.dtoList.size(); i++) {
                if (this.dtoList.get(i).getId() == Integer.parseInt(provinceId)) {
                    provinceSpinner.setSelection(i);
                    provinceTextView.setText(this.dtoList.get(i).getName());

                    provinceSelectedIndex = i;

                    List<TreeNodeAppDto> childList = this.dtoList.get(i).getChilds();
                    for (int j = 0; j < childList.size(); j++) {
                        if (childList.get(j).getId() == Integer.parseInt(cityId)) {
                            citySelectedIndex = j;

                            citySpinner.setSelection(j);
                            cityTextView.setText(childList.get(j).getName());

                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSelectedAreaId() {
        return this.selectedAreaId;
    }

    // 选择省份
    private void chooseProvince() {
        provinceSpinner.setPrompt("请选择省份");
        final SpinnerAdapter adapter = new SpinnerAdapter(this.context, dtoList);
        provinceSpinner.setAdapter(adapter);
        adapter.setSelectedIndex(0);
        provinceSpinner.setSelection(provinceSelectedIndex);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceTextView.setText(dtoList.get(position).getName());
                cityTextView.setText(dtoList.get(position).getChilds().get(0).getName());

                adapter.setSelectedIndex(position);

                provinceSelectedIndex = position;
                citySelectedIndex = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        provinceSpinner.performClick();
    }

    // 选择城市
    private void chooseCity() {
        citySpinner.setPrompt("请选择城市");
        final SpinnerAdapter adapter = new SpinnerAdapter(this.context, dtoList.get(provinceSelectedIndex).getChilds());
        citySpinner.setAdapter(adapter);
        try {
            citySpinner.setSelection(citySelectedIndex);
        } catch (Exception e) {
        }
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityTextView.setText(dtoList.get(provinceSelectedIndex).getChilds().get(position).getName());

                selectedAreaId = dtoList.get(provinceSelectedIndex).getChilds().get(position).getId();

                adapter.setSelectedIndex(position);

                citySelectedIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        citySpinner.performClick();
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView imageView;
    }

    public class SpinnerAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        private List<TreeNodeAppDto> mList;
        private Context mContext;

        private int selectedIndex = 0;

        public SpinnerAdapter(Context pContext, List<TreeNodeAppDto> pList) {
            this.mContext = pContext;
            this.mList = pList;

            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (null == convertView) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.spinner_item, null);

                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(mList.get(position).getName());
            holder.imageView.setSelected(selectedIndex == position);
            holder.imageView.setPressed(selectedIndex == position);

            return convertView;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.provinceTextView:
                if (dtoList == null)
                    return;

                chooseProvince();
                break;

            case R.id.cityTextView:
                if (dtoList == null)
                    return;

                chooseCity();
                break;
        }
    }
}