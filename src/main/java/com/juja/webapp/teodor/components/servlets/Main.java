package com.juja.webapp.teodor.components.servlets;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.components.listeneres.ContextListener;
import com.juja.webapp.teodor.controller.commands.CommandsManager;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;
import com.juja.webapp.teodor.utils.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

public class Main extends HttpServletBase  {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

    private ConnectionManager connectionManager;
    private CommandsManager commandsManager;

    @Override
    public void init() throws ServletException {
        connectionManager = (ConnectionManager) getServletContext().getAttribute(ContextListener.CONNECTION_MANAGER_CONTEXT_NAME);
        commandsManager = (CommandsManager) getServletContext().getAttribute(ContextListener.COMMANDS_MANAGER_CONTEXT_NAME);
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        trInfo(logger, session.getId() + ": got request; uri = " + req.getRequestURI());

        ConnectionInfo connectionInfo = connectionManager.getSessionConnectionInfo(session);

        try {
            if (connectionInfo != null && connectionInfo.connected()) {
                onGotRequest(req, resp);
            } else {
                trInfo(logger, session.getId() + ": doesn't connected. Redirect to Connect.jsp");

                RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CONNECT_JSP);
                dispatcher.forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        trInfo(logger, session.getId() + ": got post request; uri = " + req.getRequestURI());

        onGotRequest(req, resp);
    }

    private void onGotRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionType = req.getPathInfo();

        if (actionType != null) {
            String cmd = StringUtils.removeSlashes(actionType);
            if (cmd != null) {
                try {
                    commandsManager.tryToExecute(cmd, req, resp);
                } catch (DataBaseRequestException e) {
                    processDatabaseRequestError(e, req, resp);
                } catch (Exception e) {
                    trError(logger, e.getMessage());
                    new ResponseProcessor().sendErrorResponse(new DataBaseRequestException(RequestError.REQUEST_ERROR), req, resp, "Something went wrong!");
                }
            }
        } else {
            RequestDispatcher dispatcher = req.getRequestDispatcher(Links.MAIN_JSP);
            dispatcher.forward(req, resp);
        }

    }

    private void processDatabaseRequestError(DataBaseRequestException e, HttpServletRequest req, HttpServletResponse resp) {
	    if (req.getMethod().equals("POST")) {
	        new ResponseProcessor().sendErrorResponse(e, req, resp, e.getMessage());
        } else {
	        trError(logger, e.getMessage());
        }
    }
}


