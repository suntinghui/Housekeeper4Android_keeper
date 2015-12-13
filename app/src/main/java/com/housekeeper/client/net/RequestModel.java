package com.housekeeper.client.net;

import com.android.volley.Request;

public class RequestModel {

	private String id = null;
	private int method = Request.Method.POST;
	private String url = null;
	
	public RequestModel() {

	}

	public RequestModel(String id, String url) {
		this(id, Request.Method.POST, url);
	}

	public RequestModel(String id, int method, String url) {
		this.id = id;
		this.method = method;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
