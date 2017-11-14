package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.data.view.HTMLTableFormatter;
import com.juja.webapp.teodor.model.dao.table.Column;
import com.juja.webapp.teodor.model.dao.table.Table;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

public class SelectTableCommand extends Command {

	public SelectTableCommand(HttpServletRequest req, HttpServletResponse resp) {
		super("select", req, resp);
	}

    @Override
    protected void executeInternal()
            throws DataBaseRequestException, ServletException, IOException
	{
        String method = httpRequest.getMethod();
        String p = httpRequest.getParameter("name");

        if (method.equals("GET")) {
            httpRequest.setAttribute("tableName", p);
            httpRequest.getRequestDispatcher("/jsp/Table.jsp").forward(httpRequest, httpResponse);
        } else {
            Table table = databaseManager().selectTable(connectionManager().getSessionConnectionInfo(httpRequest.getSession()).connection(), p);

            ResponseProcessor responseProcessor = createResponseProcessor();
            JSONResponse json = responseProcessor.buildSuccessJSON();

            json.setKeyValue("table_data", new HTMLTableFormatter(table).HTMLString());

            StringBuilder cols = new StringBuilder();
            Iterator<Column> it = table.columns().iterator();

            for (Column c : table.columns()) {
                // FIXME
                if (c.name().equals("uid")) {
                    continue;
                }

                if (cols.length() > 0) {
                    cols.append(",");
                }
                cols.append(c.name());
            }

            json.setKeyValue("columns", cols.toString());

            responseProcessor.send(httpResponse, json);
        }
    }
}
