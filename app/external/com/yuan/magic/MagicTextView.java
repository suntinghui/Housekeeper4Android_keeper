package com.yuan.magic;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yuan.magic.MagicScrollView.ScrollListener;

public class MagicTextView extends TextView implements ScrollListener {
	// view ����߶�
	private int mHeight;
	// view ����scrollView��˸߶�
	private int locHeight;
	// �ݼ�/���� �ı���ֵ
	private double mRate;
	// view ���õ�ֵ
	private double mValue;
	// ��ǰ��ʾ��ֵ
	private double mCurValue;
	// ��ǰ�仯������״̬��Ŀ��ֵ
	private double mGalValue;
	// ���ƼӼ���
	private int rate = 1;
	// ��ǰ�仯״̬(��/��/����)
	private int mState = 0;
	private boolean refreshing;
	private static final int REFRESH = 1;
	private static final int SCROLL = 2;
	// ƫ���� ��Ҫ��������У�����롣
	private static final int OFFSET = 20;
	DecimalFormat fnum = new DecimalFormat("0.00");

	private static int minHeight = 0;
	
	private int largeFontSize = 22;
	private int smallFontSize = 18;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case REFRESH:
				if (rate * mCurValue < mGalValue) {
					refreshing = true;
					setMagicText(fnum.format(mCurValue));
					mCurValue += mRate * rate;
					mHandler.sendEmptyMessageDelayed(REFRESH, 40);
				} else {
					refreshing = false;
					setMagicText(fnum.format(mGalValue));
				}
				break;

			case SCROLL:
				doScroll(msg.arg1, msg.arg2);
				break;

			default:
				break;
			}
		};
	};

	public MagicTextView(Context context) {
		super(context);

		initHeight(context);
	}

	public MagicTextView(Context context, AttributeSet set) {
		super(context, set);

		initHeight(context);
	}

	public MagicTextView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);

		initHeight(context);
	}

	private void initHeight(Context context) {
		Rect fram = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(fram);
		minHeight = fram.height();
	}

	public void setLocHeight(int height) {
		locHeight = height;
	}

	public void setValue(double value) {
		mCurValue = 0.00;
		mGalValue = isShown() ? value : 0;
		mValue = value;
		mRate = (double) (mValue / 20.00);
		BigDecimal b = new BigDecimal(mRate);
		mRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public void onScrollChanged(int state, int scroll) {
		Message msg = mHandler.obtainMessage();
		msg.what = SCROLL;
		msg.arg1 = state;
		msg.arg2 = scroll;
		mHandler.sendMessage(msg);
	}

	private void doScroll(int state, int scroll) {
		if (mState == state && refreshing)
			return;

		mState = state;
		if (doMinus(scroll)) {
			rate = -1;
			mGalValue = 0;
		} else if (doPlus(scroll)) {
			rate = 1;
			mGalValue = mValue;
		}
		mHandler.sendEmptyMessage(REFRESH);
	}

	private boolean doPlus(int scroll) {
		if (isShown() && (scroll + minHeight > locHeight + OFFSET) && (mState == MagicScrollView.UP))
			return true;
		if (isShown() && (scroll < locHeight) && mState == MagicScrollView.DOWN)
			return true;

		return false;
	}

	private boolean doMinus(int scroll) {
		if (isShown() && (scroll > locHeight) && (mState == MagicScrollView.UP))
			return true;

		if (isShown() && (scroll + minHeight - mHeight < locHeight - OFFSET) && (mState == MagicScrollView.DOWN))
			return true;

		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mHeight = getMeasuredHeight();
	}
	
	public void setLargeFontSize(int largeFontSize){
		this.largeFontSize = largeFontSize;
	}
	
	public void setSmallFontSize(int smallFontSize){
		this.smallFontSize = smallFontSize;
	}

	private void setMagicText(String text) {
		SpannableString ss = new SpannableString(text);
		int loc = text.indexOf(".");
		ss.setSpan(new AbsoluteSizeSpan(this.largeFontSize, true), 0, loc, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new AbsoluteSizeSpan(this.smallFontSize, true), loc + 1, loc + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		this.setText(ss);
	}
}
