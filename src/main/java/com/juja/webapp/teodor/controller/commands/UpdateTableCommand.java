package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

public class UpdateTableCommand extends Command {

	public UpdateTableCommand(HttpServletRequest req, HttpServletResponse resp) {
		super("update", req, resp);
	}

	@Override
	protected void executeInternal() throws DataBaseRequestException, ServletException, IOException {
		String tableName = httpRequest.getParameter("name");
		String uid = httpRequest.getParameter("uid");
		String updateData = URLDecoder.decode(httpRequest.getParameter("updateData"), "UTF-8");

		int result = databaseManager().updateRows(connection(), tableName, "uid", uid, updateData.split("&|="));
		createResponseProcessor().sendSuccessResponse(httpResponse, Integer.toString(result));
	}
}
