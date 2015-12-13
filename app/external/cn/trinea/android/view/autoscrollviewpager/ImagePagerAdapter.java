/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package cn.trinea.android.view.autoscrollviewpager;

import java.util.List;

import com.android.volley.toolbox.NetworkImageView;
import com.ares.house.dto.app.ImageAppDto;
import com.wufriends.housekeeper.keeper.R;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<ImageAppDto> imageURLList;

    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<ImageAppDto> imageURLList) {
        this.context = context;
        this.imageURLList = imageURLList;
        this.size = imageURLList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return imageURLList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        // Log.e("", "========"+position);
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new NetworkImageView(context);
            holder.imageView.setBackgroundResource(R.drawable.default_image_good_detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setScaleType(ScaleType.CENTER_CROP);
        holder.imageView.setBackgroundResource(R.drawable.default_image_good_detail);
        holder.imageView.setDefaultImageResId(R.drawable.default_image_good_detail);
        holder.imageView.setErrorImageResId(R.drawable.default_image_good_detail);
        holder.imageView.setImageUrl(Constants.HOST_IP + imageURLList.get(getPosition(position)).getUrl(), ImageCacheManager.getInstance().getImageLoader());
        return view;
    }

    private static class ViewHolder {
        NetworkImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
