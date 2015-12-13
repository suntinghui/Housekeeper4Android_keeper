package com.housekeeper.activity.view;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ares.house.dto.app.AppMessageDto;
import com.ares.house.dto.app.AppResponseStatus;
import com.ares.house.dto.app.ReserveListAppDto;
import com.housekeeper.activity.keeper.KeeperHouseInfoPublishActivity;
import com.housekeeper.activity.tenant.TenantLookListActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.RequestEnum;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.JSONRequest;
import com.wufriends.housekeeper.keeper.R;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sth on 10/29/15.
 */
public class LookListAdapter extends BaseAdapter {

    private TenantLookListActivity context = null;
    private LayoutInflater layoutInflater = null;
    private List<ReserveListAppDto> list = new ArrayList<ReserveListAppDto>();

    public LookListAdapter(TenantLookListActivity context) {
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<ReserveListAppDto> list) {
        if (list == null)
            return;

        this.list = list;

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.layout_tenant_look, parent, false);

            holder.infoLayout = (LinearLayout) convertView.findViewById(R.id.infoLayout);
            holder.headImageView = (CustomNetworkImageView) convertView.findViewById(R.id.headImageView);
            holder.communityTextView = (TextView) convertView.findViewById(R.id.communityTextView);
            holder.areaTextView = (TextView) convertView.findViewById(R.id.areaTextView);
            holder.monthMoneyTextView = (TextView) convertView.findViewById(R.id.monthMoneyTextView);
            holder.contactTextView = (TextView) convertView.findViewById(R.id.contactTextView);
            holder.deleteTextView = (TextView) convertView.findViewById(R.id.deleteTextView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReserveListAppDto infoDto = list.get(position);

        holder.headImageView.setDefaultImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setErrorImageResId(R.drawable.head_tenant_default);
        holder.headImageView.setLocalImageBitmap(R.drawable.head_tenant_default);
        holder.headImageView.setImageUrl(Constants.HOST_IP + infoDto.getIndexImgUrl(), ImageCacheManager.getInstance().getImageLoader());

        holder.communityTextView.setText(infoDto.getCommunity() + "   " + infoDto.getHouseType());
        holder.areaTextView.setText(infoDto.getAreaStr());
        holder.monthMoneyTextView.setText(infoDto.getMonthMoney());

        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KeeperHouseInfoPublishActivity.class);
                intent.putExtra("houseId", infoDto.getHouseId() + "");
                intent.putExtra("hideall", true);
                context.startActivity(intent);
            }
        });

        holder.contactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + infoDto.getTelphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTip(infoDto);
            }
        });

        return convertView;
    }

    public static final class ViewHolder {
        private LinearLayout infoLayout;
        private CustomNetworkImageView headImageView;
        private TextView communityTextView;
        private TextView areaTextView;
        private TextView monthMoneyTextView;
        private TextView contactTextView;
        private TextView deleteTextView;
    }

    private void deleteTip(final ReserveListAppDto infoDto) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("\n您确定要删除该记录吗？").setContentText("").setCancelText("取消").setConfirmText("确定").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();

            }
        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();

                requestDelete(infoDto);
            }
        }).show();
    }

    private void requestDelete(ReserveListAppDto infoDto) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("id", infoDto.getId() + "");

        JSONRequest request = new JSONRequest(context, RequestEnum.HOUSE_RESERVE_DELETE, tempMap, new Response.Listener<String>() {

            @Override
            public void onResponse(String jsonObject) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(AppMessageDto.class, String.class);
                    AppMessageDto<String> dto = objectMapper.readValue(jsonObject, type);

                    if (dto.getStatus() == AppResponseStatus.SUCCESS) {

                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

                        context.requestPublishList();

                    } else {
                        Toast.makeText(context, dto.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        context.addToRequestQueue(request, "正在发送请稍候...");
    }
}
