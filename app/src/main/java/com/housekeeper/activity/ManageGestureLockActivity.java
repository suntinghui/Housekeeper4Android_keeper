package com.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.housekeeper.activity.gesture.GestureLockModifyActivity;
import com.housekeeper.activity.gesture.GestureLockSetupActivity;
import com.housekeeper.activity.gesture.GestureLockUtil;
import com.housekeeper.activity.gesture.GestureLockVerifyActivity;
import com.wufriends.housekeeper.keeper.R;

/**
 * 管理手势密码
 * 
 * 1、设置手势密码 2、修改手势密码，先验证旧密码，再设置新密码
 * 
 * 3、验证手势密码进行登录 4、验证手势密码进行清除手势密码
 * 
 * @author sth
 * 
 */
public class ManageGestureLockActivity extends BaseActivity implements OnClickListener {

	private Button toggleGestureLockBtn; // 开启或关闭手势密码
	private LinearLayout resetLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_manage_gesture_lock);

		initView();
	}

	private void initView() {

		TextView titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleTextView.setText("管理手势密码");

		Button backButton = (Button) this.findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);

		toggleGestureLockBtn = (Button) this.findViewById(R.id.toggleGestureLockBtn);
		toggleGestureLockBtn.setOnClickListener(this);

		resetLayout = (LinearLayout) this.findViewById(R.id.resetLayout);
		resetLayout.setOnClickListener(this);

		if (GestureLockUtil.hasSetGestureUtil()) {
			// 如果已经设置了手势密码
			toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_on);
			resetLayout.setVisibility(View.VISIBLE);
		} else {
			toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_off);
			resetLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			this.finish();
			break;

		case R.id.toggleGestureLockBtn:
			if (GestureLockUtil.hasSetGestureUtil()) {
				// 如果设置了手势密码，则去关闭手势密码
				toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_off);

				// 要进行手势密码验证
				Intent intent = new Intent(this, GestureLockVerifyActivity.class);
				intent.putExtra("TYPE", GestureLockVerifyActivity.TYPE_OFF);
				this.startActivityForResult(intent, 1);

			} else {
				// 如果没有设置，则切换图片，并去设置
				toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_on);

				Intent intent = new Intent(this, GestureLockSetupActivity.class);
				intent.putExtra("TYPE", GestureLockSetupActivity.TYPE_MANAGE);
				this.startActivityForResult(intent, 0);
			}
			break;

		case R.id.resetLayout:
			Intent intent = new Intent(this, GestureLockModifyActivity.class);
			this.startActivityForResult(intent, 2);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (GestureLockUtil.hasSetGestureUtil()) {
			toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_on);
			resetLayout.setVisibility(View.VISIBLE);

		} else {
			toggleGestureLockBtn.setBackgroundResource(R.drawable.btn_toggle_off);
			resetLayout.setVisibility(View.GONE);
		}

		if (requestCode == 0) { // 设置手势密码
			if (resultCode == RESULT_CANCELED) {
			} else if (resultCode == RESULT_OK) {
				Toast.makeText(this, "手势密码设置成功", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 1) { // 关闭手势密码
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "手势密码已关闭", Toast.LENGTH_SHORT).show();
				
			}
		} else if (requestCode == 2){ // 修改手势密码
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "手势密码修改成功", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
