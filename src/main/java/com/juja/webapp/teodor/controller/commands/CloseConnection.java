package com.juja.webapp.teodor.controller.commands;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

public class CloseConnection extends Command {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	public CloseConnection(HttpServletRequest req, HttpServletResponse resp) {
		super("close", req, resp);
	}

	@Override
	protected void executeInternal() throws DataBaseRequestException, ServletException, IOException {
		trInfo(logger, httpRequest.getSession().getId() + ": close database connection");

		try {
			if (userSession().connected()) {
				try {
					trInfo(logger, httpRequest.getSession().getId() + ": try to close database connection");
					connectionManager().closeConnection(httpRequest.getSession());
				} catch (DataBaseRequestException e) {
					trError(logger, "", e);
				}
				sendResponseRedirectInfo(httpResponse);
			} else {
				trInfo(logger, httpRequest.getSession().getId() + ": connection doesn't set");
				sendResponseRedirectInfo(httpResponse);
			}
		} catch (SQLException e) {
			trError(logger, "", e);
		}
		trInfo(logger, httpRequest.getSession().getId() + ": close database connection done");
	}

    private void sendResponseRedirectInfo(HttpServletResponse resp) {
        ResponseProcessor responseProcessor = createResponseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();
        json.setKeyValue("url", Links.ROOT_WEB_APP);

        responseProcessor.send(resp, json);
    }
}
