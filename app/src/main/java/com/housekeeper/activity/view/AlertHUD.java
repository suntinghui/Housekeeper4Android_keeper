package com.housekeeper.activity.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wufriends.housekeeper.keeper.R;

public class AlertHUD extends Dialog {
	public AlertHUD(Context context) {
		super(context);
	}

	public AlertHUD(Context context, int theme) {
		super(context, theme);
	}

	public static AlertHUD show(Context context, CharSequence message, int duration) {
		final AlertHUD dialog = new AlertHUD(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.layout_alert_hud);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
		dialog.setCancelable(false);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.3f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				dialog.dismiss();
			}
		}, duration);
		
		return dialog;
	}
}