package com.housekeeper.activity.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {

	private EditTextChangeListener listener = null;

	public MyEditText(Context context) {
		super(context);

		initView(context);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
	}

	private void initView(Context context) {
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (listener != null) {
					listener.textChange(MyEditText.this, s.toString());
				}
			}
		});
	}

	public void setOnEditTextChangeListener(EditTextChangeListener listener) {
		this.listener = listener;
	}

	public interface EditTextChangeListener {
		public void textChange(MyEditText view, String text);
	}

}
