package com.housekeeper.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ares.house.dto.app.ImageAppDto;
import com.housekeeper.activity.view.TouchImageView;
import com.housekeeper.client.Constants;
import com.housekeeper.client.net.MyImageRequest;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 9/30/15.
 */
public class ImageZoomActivity extends BaseActivity implements View.OnClickListener {

    private TouchImageView imageView;

    private ImageAppDto imageDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_image_zoom);

        imageDto = (ImageAppDto) this.getIntent().getSerializableExtra("DTO");

        this.initView();

        requestImage();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("预览");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        imageView = (TouchImageView) this.findViewById(R.id.imageView);
    }

    private void requestImage() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        MyImageRequest request = new MyImageRequest(Constants.HOST_IP + imageDto.getSourceUrl(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(final Bitmap response) {

                hideProgress();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(response);
                        imageView.setZoom(1);
                    }
                });
            }
        }, width, height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        });

        this.addToRequestQueue(request, "正在加载图片...");
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
