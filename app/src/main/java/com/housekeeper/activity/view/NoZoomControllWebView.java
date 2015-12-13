/**
 * 解决WebView控件显示缩放控件的问题
 * 
 * From http://stackoverflow.com/questions/5125851/enable-disable-zoom-in-android-webview
 * 
 * Thanks Lukas Knuth.
 * 
 * 
 * @author sth
 */
package com.housekeeper.activity.view;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.wufriends.housekeeper.keeper.R;

public class NoZoomControllWebView extends WebView {

	private ZoomButtonsController zoom_controll = null;
	private ProgressBar progressbar = null;

	public NoZoomControllWebView(Context context) {
		super(context);

		disableControls();
		setProgressBar(context);
	}

	public NoZoomControllWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		disableControls();
		setProgressBar(context);
	}

	public NoZoomControllWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

		disableControls();
		setProgressBar(context);
	}

	private void setProgressBar(Context context) {
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 10, 0, 0));
		progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_bg_selector));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());
	}

	/**
	 * Disable the controls
	 */
	private void disableControls() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			// Use the API 11+ calls to disable the controls
			this.getSettings().setBuiltInZoomControls(true);
			this.getSettings().setDisplayZoomControls(false);
		} else {
			// Use the reflection magic to make it work on earlier APIs
			getControlls();
		}
	}

	/**
	 * This is where the magic happens :D
	 */
	private void getControlls() {
		try {
			Class webview = Class.forName("android.webkit.WebView");
			Method method = webview.getMethod("getZoomButtonsController");
			zoom_controll = (ZoomButtonsController) method.invoke(this, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		if (zoom_controll != null) {
			// Hide the controlls AFTER they where made visible by the default implementation.
			zoom_controll.setVisible(false);
		}
		return true;
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setProgress(100);
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE) {
					progressbar.setVisibility(VISIBLE);
				}

				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
