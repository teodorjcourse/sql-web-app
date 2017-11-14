package com.juja.webapp.teodor.controller.commands;
import com.juja.webapp.teodor.WebAppAttributes;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetTablesCommand extends Command {

	public GetTablesCommand(HttpServletRequest req, HttpServletResponse resp) {
        super("tables", req, resp);
    }

    @Override
    protected void executeInternal()
			throws DataBaseRequestException, ServletException, IOException {

        ConnectionManager connectionManager = (ConnectionManager) httpRequest.getSession().getServletContext()
                .getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

        ConnectionInfo connectionInfo = connectionManager.getSessionConnectionInfo(httpRequest.getSession());

		List<String> tables = databaseManager().getTables(connectionInfo.connection());

		httpRequest.setAttribute("tables", tables);
		httpRequest.getRequestDispatcher("/jsp/Tables.jsp").forward(httpRequest, httpResponse);
    }


}
