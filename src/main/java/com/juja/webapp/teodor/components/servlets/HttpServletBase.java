package com.juja.webapp.teodor.components.servlets;

import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.juja.webapp.teodor.utils.Logger.*;

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

    public final void trInfo(org.apache.log4j.Logger logger, String message) {
        info(logger, message);
    }

    public final void trDebug(org.apache.log4j.Logger logger, String message) {
        debug(logger, message);
    }

    public final void trWarn(org.apache.log4j.Logger logger, String message) {
        warn(logger, message);
    }

    public final void trError(org.apache.log4j.Logger logger, String message) {
        error(logger, message);
    }

    public final void trError(org.apache.log4j.Logger logger, String message, Throwable t) {
        error(logger, message, t);
    }

}
