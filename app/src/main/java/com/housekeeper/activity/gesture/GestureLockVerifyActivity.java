package com.housekeeper.activity.gesture;

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
import com.housekeeper.activity.LoginActivity;
import com.housekeeper.activity.keeper.KeeperMainActivity;
import com.wufriends.housekeeper.keeper.R;
import com.housekeeper.activity.BaseActivity;
import com.housekeeper.activity.gesture.LockPatternView.Cell;
import com.housekeeper.activity.gesture.LockPatternView.DisplayMode;

public class GestureLockVerifyActivity extends BaseActivity implements LockPatternView.OnPatternListener, OnClickListener {
	private static final String TAG = "GestureLockVerifyActivity";

	private int type = TYPE_LOGIN;
	/**
	 * 进行登录验证
	 */
	public static final int TYPE_LOGIN = 0x2001;

	/**
	 * 关闭手势密码的验证
	 */
	public static final int TYPE_OFF = 0x2002;

	private List<Cell> lockPattern;
	private LockPatternView lockPatternView;

	private Button backBtn;
	private TextView tipTextView;
	private TextView forgetTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String patternString = GestureLockUtil.getGestureLock();
		if (patternString == null) {
			// 如果没有密码，则去设置手势密码
			Intent intent = new Intent(this, GestureLockSetupActivity.class);
			this.startActivity(intent);
			this.finish();
			return;
		}

		setContentView(R.layout.activity_gesture_lock_verify);

		type = this.getIntent().getIntExtra("TYPE", TYPE_LOGIN);

		initView();
	}

	private void initView() {
		lockPattern = LockPatternView.stringToPattern(GestureLockUtil.getGestureLock());

		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);

		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);

		tipTextView = (TextView) this.findViewById(R.id.tipTextView);

		forgetTextView = (TextView) this.findViewById(R.id.forgetTextView);
		forgetTextView.setOnClickListener(this);

		if (type == TYPE_LOGIN) {
			backBtn.setVisibility(View.INVISIBLE);
		}
	}

	public void onBackPressed() {
		if (type == TYPE_OFF) {
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
	}

	@Override
	public void onPatternStart() {

	}

	@Override
	public void onPatternCleared() {

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {

	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {

		if (pattern.equals(lockPattern)) {
			// 手势密码正确
			if (type == TYPE_LOGIN) {
				Intent intent = new Intent(this, KeeperMainActivity.class);
				this.startActivity(intent);

				this.finish();

			} else if (type == TYPE_OFF) {
				GestureLockUtil.clearGestureLock();

				this.setResult(RESULT_OK);
				this.finish();
			}

		} else {
			// 手势密码错误
			lockPatternView.setDisplayMode(DisplayMode.Wrong);

			this.clearWrongMode();

			tipTextView.setText("手势密码错误，请重试");
			shakeTip();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.backBtn:
			this.setResult(RESULT_CANCELED);
			this.finish();
			break;

		case R.id.forgetTextView:
			// 进行登录验证
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("FROM", LoginActivity.FROM_FORGET_GESTURE);
			this.startActivity(intent);
			this.finish();

			break;

		default:
			break;
		}

	}

	private void shakeTip() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				tipTextView.setVisibility(View.VISIBLE);
				YoYo.with(Techniques.Shake).duration(500).playOn(tipTextView);
			}
		}, 0);
	}

	private void clearWrongMode() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				lockPatternView.clearPattern();
			}
		}, 1000);
	}

}
