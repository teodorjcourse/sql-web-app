package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.I18n;
import com.juja.webapp.teodor.Keys;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 * If connection is already set up - do redirect to Main servlet.
 * Otherwise try set connection with database (see postgresql.properties file configuration)
 * ConnectionManager saves connection object and corresponding to it HTTPSession if connection was set successfully.
 */
public class ConnectCommand extends Command {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    public ConnectCommand(HttpServletRequest req, HttpServletResponse resp) {
        super("connect", req, resp);
    }

    @Override
    protected void executeInternal()
            throws DataBaseRequestException, ServletException, IOException
    {
        HttpSession httpSessoin = httpRequest.getSession();
        UserSession userSession = userSession();

        trInfo(logger, httpSessoin.getId() + ": seting up database connection");

        if (userSession != null) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            try {
                if (userSession.connected()) {

                    trInfo(logger, httpSessoin.getId() + ": is connected. Redirect to Main.jsp");

                    httpResponse.sendRedirect(Links.MAIN_JSP);
                } else {
                    String username = httpRequest.getParameter(USERNAME);
                    String password = httpRequest.getParameter(PASSWORD);

                    ConnectionManager connectionManager = connectionManager();
                    try {
                        trInfo(logger, httpSessoin.getId() + ": trying to create connection with params: username = " + username + ", password = " + password);

                        connectionManager.createConnection(httpSessoin, username, password);

                        trInfo(logger, httpSessoin.getId() + ": connection has been successfully created");

                        sendResponseRedirectInfo(httpResponse);
                    } catch (DataBaseRequestException e) {
                        trError(logger, httpSessoin.getId() + ": error happend while create connection attempt", e);

                        responseProcessor.sendErrorResponse(e, httpRequest, httpResponse, I18n.text(Keys.DATABASE_CONNECT_ERROR_TXT));
                    }

                }
            } catch (SQLException e) {
                // Do nothing if exception was coused do to userSession.connected() operation
                trError(logger, "", e);
            }
        } else {
            trWarn(logger, "user sessoin doesn't exist");
        }
    }

    private void sendResponseRedirectInfo(HttpServletResponse resp) {
        ResponseProcessor responseProcessor = createResponseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();
        json.setKeyValue("url", Links.ROOT_WEB_APP);

        responseProcessor.send(resp, json);
    }
}
