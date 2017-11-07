package com.juja.webapp.teodor.components.servlets;

import com.juja.webapp.teodor.I18n;
import com.juja.webapp.teodor.Keys;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.utils.StringUtils;

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
public class DatabaseConnectionService extends HttpServletBase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.removeSlashes(req.getServletPath());

        if (action.equals("connect")) {
            setDatabaseConnection(req, resp);
        }

        if (action.equals("close")) {
            closeDatabaseConnection(req, resp);
        }
    }

    private void setDatabaseConnection(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession httpSessoin = req.getSession();
        UserSession userSession = userSession(req);

        trInfo(logger, httpSessoin.getId() + ": seting up database connection");

        if (userSession != null) {
            ResponseProcessor responseProcessor = responseProcessor();
            try {
                if (userSession.connected()) {

                    trInfo(logger, httpSessoin.getId() + ": is connected. Redirect to main.jsp");

                    resp.sendRedirect(Links.MAIN_JSP);
                } else {
                    String username = req.getParameter(USERNAME);
                    String password = req.getParameter(PASSWORD);

                    ConnectionManager connectionManager = connectionManager(httpSessoin);
                    try {
                        trInfo(logger, httpSessoin.getId() + ": trying to create connection with params: username = " + username + ", password = " + password);

                        connectionManager.createConnection(httpSessoin, username, password);

                        trInfo(logger, httpSessoin.getId() + ": connection has been successfully created");

                        sendResponseRedirectInfo(resp);
                    } catch (DataBaseRequestException e) {
                        trError(logger, httpSessoin.getId() + ": error happend while create connection attempt", e);

                        responseProcessor.sendErrorResponse(e, req, resp, I18n.text(Keys.DATABASE_CONNECT_ERROR_TXT));
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

    private void closeDatabaseConnection(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        trInfo(logger, req.getSession().getId() + ": close database connection");

        try {
            if (userSession(req).connected()) {
                try {
                    trInfo(logger, req.getSession().getId() + ": try to close database connection");
                    connectionManager(req).closeConnection(req.getSession());
                } catch (DataBaseRequestException e) {
                    trError(logger, "", e);
                }
                sendResponseRedirectInfo(resp);
            } else {
                trInfo(logger, req.getSession().getId() + ": connection doesn't set");
                sendResponseRedirectInfo(resp);
            }
        } catch (SQLException e) {
            trError(logger, "", e);
        }
        trInfo(logger, req.getSession().getId() + ": close database connection done");
    }

    private void sendResponseRedirectInfo(HttpServletResponse resp) {
        ResponseProcessor responseProcessor = responseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();
        json.setKeyValue("url", Links.ROOT_WEB_APP);

        responseProcessor.send(resp, json);
    }
}
