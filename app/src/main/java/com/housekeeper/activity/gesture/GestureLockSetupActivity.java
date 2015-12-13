package com.housekeeper.activity.gesture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.wufriends.housekeeper.keeper.R;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.gesture.LockPatternView.Cell;
import com.housekeeper.activity.gesture.LockPatternView.DisplayMode;

/**
 * 设置手势密码
 * 
 * 有两个通道可以进入本页面，一是首次注册成功后如果没有设置则自动弹出本页面进行设置，在右上角有跳过按纽可以忽略; 另一个通道是在个人中心里的管理手势密码来开启设置。
 * 
 * 
 * 
 * @author sth
 * 
 */
public class GestureLockSetupActivity extends BaseActivity implements LockPatternView.OnPatternListener, OnClickListener {

	private static final String TAG = "GestureLockSetupActivity";

	private int type = TYPE_REGISTER;
	// 注册成功时设置手势密码
	public final static int TYPE_REGISTER = 0x1990;
	// 从个人中心进入的主动设置手势密码
	public final static int TYPE_MANAGE = 0x1991;

	private LockPatternView lockPatternView;
	private Button backBtn;
	private TextView skipTextView;
	private TextView tipTextView;
	private TextView resetTextView;

	private int step = STEP_1;
	/**
	 * 初如化绘制
	 */
	private static final int STEP_1 = 1;

	/**
	 * 一次手势设置完成，自动触发第二次绘制。
	 */
	private static final int STEP_2 = 2;

	/**
	 * 第二次设置手势完成
	 */
	private static final int STEP_3 = 3;

	private List<Cell> choosePattern;

	private boolean confirm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gesture_lock_setup);

		type = this.getIntent().getIntExtra("TYPE", TYPE_REGISTER);

		initView();

		updateView();
	}

	private void initView() {
		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);

		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);

		skipTextView = (TextView) this.findViewById(R.id.skipTextView);
		skipTextView.setOnClickListener(this);

		tipTextView = (TextView) this.findViewById(R.id.tipTextView);

		resetTextView = (TextView) this.findViewById(R.id.resetTextView);
		resetTextView.setOnClickListener(this);
		resetTextView.setVisibility(View.INVISIBLE);

		if (type == TYPE_MANAGE) {
			skipTextView.setVisibility(View.INVISIBLE);
		}
		if (type == TYPE_REGISTER) {
			backBtn.setVisibility(View.INVISIBLE);
		}
	}

	private void updateView() {
		switch (step) {
		case STEP_1: {
			choosePattern = null;
			confirm = false;
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			resetTextView.setVisibility(View.INVISIBLE);
			tipTextView.setText("绘制解锁图案");
			tipTextView.setVisibility(View.VISIBLE);

		}
			break;

		case STEP_2: {
			tipTextView.setText("再次绘制解锁图案");
			tipTextView.setVisibility(View.VISIBLE);
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
		}
			break;

		case STEP_3: {
			// 与第一次绘制的图案相同，成功！
			if (confirm) {
				lockPatternView.disableInput();
				
				GestureLockUtil.setGestureLock(LockPatternView.patternToString(choosePattern));
				
				// 跳转到别的界面
				if (type == TYPE_MANAGE) {
					this.setResult(RESULT_OK);
					this.finish();
					
				} else if (type == TYPE_REGISTER) {
					Intent intent = new Intent(this, KeeperMainActivity.class);
					this.startActivity(intent);
					this.finish();
				}
				
			} else {
				// 与第一次绘制的图案不同，
				resetTextView.setVisibility(View.VISIBLE);
				tipTextView.setText("与上一次绘制不一致，请重新绘制");
				tipTextView.setVisibility(View.VISIBLE);
				shakeTip();

				lockPatternView.setDisplayMode(DisplayMode.Wrong);
				clearWrongMode();
				
				lockPatternView.enableInput();
			}
		}

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.backBtn:
			this.setResult(RESULT_CANCELED);
			this.finish();
			break;

		case R.id.skipTextView:
			Intent intent = new Intent(this, KeeperMainActivity.class);
			this.startActivity(intent);
			this.finish();

			break;

		case R.id.resetTextView:
			step = STEP_1;
			choosePattern = null;
			updateView();
			break;

		default:
			break;
		}

	}

	@Override
	public void onPatternStart() {
		Log.e(TAG, "onPatternStart");
	}

	@Override
	public void onPatternCleared() {
		Log.e(TAG, "onPatternCleared");
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		Log.e(TAG, "onPatternCellAdded");
	}

	// 绘制完成
	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.e(TAG, "onPatternDetected");

		// 小于四个点。
		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			tipTextView.setText("最少连接" + LockPatternView.MIN_LOCK_PATTERN_SIZE + "个点，请重新绘制");
			resetTextView.setVisibility(View.VISIBLE);
			resetTextView.setVisibility(View.INVISIBLE);
			lockPatternView.clearPattern();
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<Cell>(pattern);
			Log.e(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));

			step = STEP_2;
			updateView();
			return;
		}

		Log.e(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));
		Log.e(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

		// 如果两次绘制相同
		if (choosePattern.equals(pattern)) {
			Log.e(TAG, "pattern = " + Arrays.toString(pattern.toArray()));

			confirm = true;
		} else {
			confirm = false;
		}

		step = STEP_3;
		updateView();

	}

	private void shakeTip() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				tipTextView.setVisibility(View.VISIBLE);
				YoYo.with(Techniques.Shake).duration(500).playOn(tipTextView);
			}
		}, 0);
	}
	
	private void clearWrongMode(){
		new Handler().postDelayed(new Runnable() {
			public void run() {
				lockPatternView.clearPattern();
			}
		}, 1000);
	}

}
