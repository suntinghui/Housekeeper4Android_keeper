/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.housekeeper.activity.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.android.volley.toolbox.NetworkImageView;
import com.ares.house.dto.app.LinkArticle;
import com.housekeeper.activity.ShowWebViewActivity;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.ImageCacheManager;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.RecyclingPagerAdapter;

/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class MediaImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<LinkArticle> imageURLList;

    private int size;
    private boolean isInfiniteLoop;

    public MediaImagePagerAdapter(Context context, List<LinkArticle> imageURLList) {
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
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new NetworkImageView(context);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinkArticle media = imageURLList.get(getPosition(position));

                    if (!StringUtils.isBlank(media.getLink())) {
                        Intent intent = new Intent(context, ShowWebViewActivity.class);
                        intent.putExtra("title", media.getTitle());
                        intent.putExtra("url", media.getLink());
                        context.startActivity(intent);
                    }
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setScaleType(ScaleType.CENTER_CROP);
        holder.imageView.setImageUrl(Constants.HOST_IP + imageURLList.get(getPosition(position)).getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
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
    public MediaImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
