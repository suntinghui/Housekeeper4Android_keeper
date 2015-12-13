package com.housekeeper.client.net;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * 此类是为了将 重定向登录及完善个人信息等业务逻辑“异常”忽略
 * 
 * @author sth
 * 
 */
@SuppressWarnings("serial")
public class IgnoreError extends VolleyError {

	public IgnoreError() {
	}

	public IgnoreError(NetworkResponse networkResponse) {
		super(networkResponse);
	}

	public IgnoreError(Throwable cause) {
		super(cause);
	}

}
