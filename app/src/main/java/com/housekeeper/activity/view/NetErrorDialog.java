package com.housekeeper.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.wufriends.housekeeper.keeper.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class NetErrorDialog {

	private static final Configuration CONFIGURATION_INFINITE = new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build();
	private static final Configuration CONFIGURATION_SHORT = new Configuration.Builder().setDuration(Configuration.DURATION_SHORT).build();
	private static final Configuration CONFIGURATION_LONG = new Configuration.Builder().setDuration(Configuration.DURATION_LONG).build();

	private Crouton crouton = null;

	private static NetErrorDialog instance = null;

	public static NetErrorDialog getInstance() {
		if (instance == null) {
			instance = new NetErrorDialog();
		}

		return instance;
	}

	public void show(final Activity context) {
		if (Crouton.getQueue().size() > 0)
			return;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_net_eror, null);

		LinearLayout settingLayout = (LinearLayout) view.findViewById(R.id.settingLayout);
		settingLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingNet(context);

				Crouton.cancelAllCroutons();
			}
		});

		LinearLayout closeLayout = (LinearLayout) view.findViewById(R.id.closeLayout);
		closeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (crouton != null) {
					Crouton.cancelAllCroutons();
				}
			}
		});

		crouton = Crouton.make(context, view);
		// 一直显示，手动关闭
		//crouton.setConfiguration(CONFIGURATION_INFINITE);
		
		crouton.setConfiguration(CONFIGURATION_LONG);
		
		crouton.show();
	}
	
	public void hide(){
		if (crouton != null){
			Crouton.cancelAllCroutons();
		}
	}

	private void settingNet(Context context) {
		Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}

}
