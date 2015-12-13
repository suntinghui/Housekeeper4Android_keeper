package com.housekeeper.client;

public class TokenExpiredException extends Exception {
	
	private static final long serialVersionUID = 2101015460309515027L;

	public TokenExpiredException() {
		super();
	}

	public TokenExpiredException(String detailMessage) {
		super(detailMessage);
	}


}
