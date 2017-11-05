package com.juja.webapp.teodor.app.servlets;

import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class HttpServletBase extends HttpServlet {

    public final UserSession userSession(HttpServletRequest servletRequest) {
        return userSession(servletRequest.getSession());
    }

    public final UserSession userSession(HttpSession httpSession) {
        return (UserSession) httpSession.getAttribute(WebAppAttributes.USER_SESSION);
    }

    public final ConnectionManager connectionManager(HttpServletRequest servletRequest) {
        return connectionManager(servletRequest.getSession());
    }

    public final ConnectionManager connectionManager(HttpSession httpSession) {
        return (ConnectionManager) httpSession.getServletContext()
                .getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);
    }

    public final ResponseProcessor responseProcessor() {
        return new ResponseProcessor();
    }

}
