package com.housekeeper.activity.view;

import cn.pedant.SweetAlert.OptAnimationLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

public class CreditReviewItem extends RelativeLayout {

	private TextView titleTextView;
	private FrameLayout successFrame;
	private MySuccessTickView mSuccessTick;
	private View mSuccessLeftMask;
	private View mSuccessRightMask;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mSuccessBowAnim;

	public CreditReviewItem(Context context) {
		super(context);

		this.initView(context);
	}

	public CreditReviewItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_credit_review, this);

		titleTextView = (TextView) this.findViewById(R.id.titleTextView);

		successFrame = (FrameLayout) this.findViewById(R.id.success_frame);
		successFrame.setVisibility(View.INVISIBLE);
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);

		mSuccessTick = (MySuccessTickView) successFrame.findViewById(R.id.success_tick);
		mSuccessLeftMask = successFrame.findViewById(R.id.mask_left);
		mSuccessRightMask = successFrame.findViewById(R.id.mask_right);
		mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
		mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void playAnimation() {
		successFrame.setVisibility(View.VISIBLE);

		mSuccessTick.startTickAnim();
		mSuccessRightMask.startAnimation(mSuccessBowAnim);
	}

}
