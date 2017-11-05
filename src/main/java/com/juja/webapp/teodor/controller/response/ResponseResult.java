package com.juja.webapp.teodor.controller.response;

enum ResponseResult {
	SUCCESS("ok"),
	ERROR("error"),
	ARGUMENTS_ERROR("arguments_error");

	private String result;

	ResponseResult(String result) {
		this.result = result;
	}

	public String value() {
		return this.result;
	}
}
