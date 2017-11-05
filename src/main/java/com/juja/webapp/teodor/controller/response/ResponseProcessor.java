package com.juja.webapp.teodor.controller.response;

import com.juja.webapp.teodor.utils.DebugLoger;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseProcessor {

	public void sendErrorResponse(
			DataBaseRequestException error, HttpServletRequest req, HttpServletResponse resp, String message) {

		send(resp, buildErrorJSON(error, req, message));
	}

	public void sendSuccessResponse(HttpServletResponse resp, String message) {
		JSONResponse json = new JSONResponse();
		json.setResult(ResponseResult.SUCCESS);
		send(resp, json);
	}

	public JSONResponse buildErrorJSON(DataBaseRequestException error, HttpServletRequest req, String message) {
		return new ErrorResponse(new JSONResponse()).build(error, req, message);
	}

	public JSONResponse buildSuccessJSON() {
	    return new JSONResponse().setResult(ResponseResult.SUCCESS);
	}

	public void send(HttpServletResponse resp, JSONResponse json) {
		resp.setCharacterEncoding("UTF-8");

		try {
			PrintWriter writer = resp.getWriter();

			DebugLoger.tr("JSON" + json.JSONString());
			writer.print(json.JSONString() );
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}

	private static class ErrorResponse {
		private JSONResponse jsonResponse;

		public ErrorResponse(JSONResponse jsonResponse) {
			this.jsonResponse = jsonResponse;
		}

		public JSONResponse build(DataBaseRequestException exc, HttpServletRequest request, String message) {
			jsonResponse
					.setResult(ResponseResult.ERROR)
					.setErrCode(exc.error())
					.setSystemMessage(exc.getMessage())
					.setMessage(message)
					.setClientRequestURL(request.getRequestURL().toString());

			return jsonResponse;
		}
	}
}
