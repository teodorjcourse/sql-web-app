package com.juja.webapp.teodor.controller.response;
import org.json.simple.JSONObject;

public class JSONResponse {
	private JSONObject jsonObject;

	public JSONResponse() {
		jsonObject = new JSONObject();
	}

	public JSONResponse setResult(ResponseResult result) {
		jsonObject.put("result", result.value());

		return this;
	}

	public JSONResponse setErrCode(short code) {
		jsonObject.put("error_code", code);

		return this;
	}

	public JSONResponse setSystemMessage(String message) {
		jsonObject.put("system_message", message);

		return this;
	}

	public JSONResponse setMessage(String message) {
		jsonObject.put("message", message);

		return this;
	}

	public JSONResponse setClientRequestURL(String url) {
		jsonObject.put("client_request", "\"" + url + "\"");

		return this;
	}

	public JSONResponse setKeyValue(Object key, Object value) {
		jsonObject.put(key, value);

		return this;
	}

	public String JSONString() {
		return jsonObject.toJSONString();
	}
}
