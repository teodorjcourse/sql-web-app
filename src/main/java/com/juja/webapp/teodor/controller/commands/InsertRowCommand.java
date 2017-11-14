package com.juja.webapp.teodor.controller.commands;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.requests.QueryActionResult;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertRowCommand extends Command {

	public InsertRowCommand(HttpServletRequest req, HttpServletResponse resp) {
		super("insert", req, resp);
	}

    @Override
    protected void executeInternal()
            throws DataBaseRequestException
    {
        String tableName = httpRequest.getParameter("name");
        try {
            String columnsKP = URLDecoder.decode(httpRequest.getParameter("cols"), "UTF-8");
            String[] keyValuePairs = columnsKP.split("&|=");
            boolean uidTableExists = false;

            QueryActionResult result;

            try {
                DatabaseMetaData md = connection().getMetaData();
                ResultSet rs = md.getColumns(null, null, tableName, "uid");
                uidTableExists = rs.next();
                rs.close();
            } catch (SQLException e) {
                // NOP
            }

            if (uidTableExists) {
                result = databaseManager().insertRow(connection(), tableName, keyValuePairs, new String[] {"uid"});
            } else {
                result = databaseManager().insertRow(connection(), tableName, keyValuePairs);
            }

            ResponseProcessor rp = createResponseProcessor();
            JSONResponse json = rp.buildSuccessJSON();

            if (!result.isEmpty()) {
                json.setKeyValue("uid", "row_" + result.result());
                json.setKeyValue("class", "clickable-row");
            } else {
                json.setKeyValue("class", "not-clickable-row");
            }

            rp.send(httpResponse, json);
        } catch (UnsupportedEncodingException e) {
            // NOP
        }
    }
}
