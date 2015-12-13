package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ares.house.dto.app.ImageAppDto;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.ImageZoomActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;
import com.housekeeper.client.net.MyImageRequest;
import com.wufriends.housekeeper.keeper.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sth on 9/28/15.
 */
public class DiaoyuView extends LinearLayout implements View.OnClickListener {

    private BaseActivity context;
    private PacificView pacificView;

    private CustomNetworkImageView houseImageView;
    private LinearLayout contentLayout;
    private ImageView deleteImageView;

    private ImageAppDto imageDto = null;
    public String getImageType = PacificView.TYPE_GETIMG_ADDRESS;
    private boolean editable = true;

    public DiaoyuView(Context context) {
        super(context);

        this.initView(context);
    }

    public DiaoyuView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.initView(context);
    }

    private void initView(Context context) {
        this.context = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_diaoyu, this);

        houseImageView = (CustomNetworkImageView) this.findViewById(R.id.houseImageView);
        houseImageView.setOnClickListener(this);

        contentLayout = (LinearLayout) this.findViewById(R.id.contentLayout);
        contentLayout.setOnClickListener(this);

        deleteImageView = (ImageView) this.findViewById(R.id.deleteImageView);
        deleteImageView.setOnClickListener(this);
    }

    public void setValue(PacificView pacificView, ImageAppDto imageDto, String getImageType) {
        this.setValue(pacificView, imageDto, getImageType, true);
    }

    public void setValue(PacificView pacificView, ImageAppDto imageDto, String getImageType, boolean editable) {
        this.pacificView = pacificView;
        this.imageDto = imageDto;
        this.getImageType = getImageType;
        this.editable = editable;

        deleteImageView.setVisibility(this.editable ? View.VISIBLE : View.GONE);

        if (this.getImageType.equals(PacificView.TYPE_GETIMG_ADDRESS)) {
            houseImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            houseImageView.setDefaultImageResId(R.drawable.default_image_good_detail);
            houseImageView.setErrorImageResId(R.drawable.default_image_good_detail);

            houseImageView.setImageUrl(Constants.HOST_IP + imageDto.getUrl(), ImageCacheManager.getInstance().getImageLoader());
            int height = (int) (((BaseActivity) context).getWindowManager().getDefaultDisplay().getWidth() / 1.7);
            houseImageView.setMaxHeight(height);
            houseImageView.setMinimumHeight(height);
        } else {
            requestImage();
        }
    }

    private void requestImage() {
        MyImageRequest request = new MyImageRequest(Constants.HOST_IP + imageDto.getUrl(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                houseImageView.setBackground(new BitmapDrawable(context.getResources(), response));
            }
        }, houseImageView.getWidth(), houseImageView.getHeight(), ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        context.addToRequestQueue(request, null);
    }

    private void delete() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("\n您确定要删除该图片吗？").setContentText("一旦删除，不可恢复。").setCancelText("取消").setConfirmText("确定").showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();

            }
        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();

                pacificView.getPacificListener().deleteImage(imageDto.getId() + "");


            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contentLayout:

                break;

            case R.id.houseImageView: {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("DTO", imageDto);
                this.context.startActivity(intent);
            }
            break;

            case R.id.deleteImageView:
                delete();
                break;
        }
    }
}
