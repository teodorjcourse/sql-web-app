package com.juja.webapp.teodor.app.servlets;

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

/**
 * If connection is already set up - do redirect to Main servlet.
 * Otherwise try set connection with database (see postgresql.properties file configuration)
 * ConnectionManager saves connection object and corresponding to it HTTPSession if connection was set successfully.
 */
public class DatabaseConnectionService extends HttpServletBase {

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


        if (userSession != null) {
            ResponseProcessor responseProcessor = responseProcessor();
            try {
                if (userSession.connected()) {

                    resp.sendRedirect(Links.MAIN_JSP);
                } else {
                    String username = req.getParameter(USERNAME);
                    String password = req.getParameter(PASSWORD);

                    ConnectionManager connectionManager = connectionManager(httpSessoin);
                    try {
                        connectionManager.createConnection(httpSessoin, username, password);

                        sendResponseRedirectInfo(resp);
                    } catch (DataBaseRequestException e) {
                        responseProcessor.sendErrorResponse(e, req, resp, I18n.text(Keys.DATABASE_CONNECT_ERROR_TXT));
                    }

                }
            } catch (SQLException e) {
                // Do nothing if exception was coused do to userSession.connected() operation
            }
        } else {
            // todo надо происследовать кейс, ели сессия юзера вылшла, все анные обнулилсь и он шлет реквест коннект
            // todo в теории должен обработать SessionListener с последующим вызовом процедуры конекта. Но надо проверить!
        }
    }

    private void closeDatabaseConnection(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            if (userSession(req).connected()) {
                try {
                    connectionManager(req).closeConnection(req.getSession());
                } catch (DataBaseRequestException e) {
                    e.printStackTrace();
                }
                sendResponseRedirectInfo(resp);
            } else {
                sendResponseRedirectInfo(resp);
            }
        } catch (SQLException e) {
            //NOP
        }
    }

    private void sendResponseRedirectInfo(HttpServletResponse resp) {
        ResponseProcessor responseProcessor = responseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();
        json.setKeyValue("url", Links.ROOT_WEB_APP);

        responseProcessor.send(resp, json);
    }
}
