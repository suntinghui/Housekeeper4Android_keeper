package com.housekeeper.client.net;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class ResponseErrorListener implements Response.ErrorListener {

	private Context context;

	public ResponseErrorListener(Context context) {
		this.context = context;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (error instanceof TimeoutError) {
			showToast("请求超时，请稍候重试");

		} else if (error instanceof AuthFailureError) {
			showToast("服务器验证失败，请重试");

		} else if (error instanceof NetworkError) {
			showToast("网络异常，请重试");

		} else if (error instanceof ParseError) {
			showToast("服务器响应数据异常，请重试");

		} else if (error instanceof ServerError) {
			if (error.networkResponse.statusCode == 404) {
				showToast("请求地址错误，请检查[404]");

			} else if (error.networkResponse.statusCode == 400) {
				showToast("请求参数异常，请检查[400]");

			} else {
				showToast("服务器异常，请重试[" + error.networkResponse.statusCode + "]");

			}
		} else if (error instanceof NoConnectionError) {
			showToast("网络异常，请重试");

		} else if (error instanceof IgnoreError) {
			// do nothing

		} else {
			showToast("未知异常");

		}

		todo();
	}

	public void todo() {

	}

	private void showToast(String msg) {
		try {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
