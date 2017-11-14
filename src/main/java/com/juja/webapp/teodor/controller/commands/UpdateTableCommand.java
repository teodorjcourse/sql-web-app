package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class UpdateTableCommand extends Command {

	public UpdateTableCommand(HttpServletRequest req, HttpServletResponse resp) {
		super("update", req, resp);
	}

	@Override
	protected void executeInternal() throws DataBaseRequestException, ServletException, IOException {
		String tableName = httpRequest.getParameter("name");
		String uidColumn = httpRequest.getParameter("uid_column");
		String uid = httpRequest.getParameter("uid");
		String updateData = httpRequest.getParameter("updateData");

		int result = databaseManager().updateRows(connection(), tableName, uidColumn, uid, updateData.split("&|="));
//
//		String key = result > 0 ? Keys.UPDATE_ROW_SUCCESS.toString() : Keys.UPDATE_ROW_SUCCESS_ZERO.toString();
	}
}
