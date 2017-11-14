package com.juja.webapp.teodor.components.servlets;
import javax.servlet.http.HttpServlet;

import static com.juja.webapp.teodor.utils.Logger.*;

public abstract class HttpServletBase extends HttpServlet {

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
