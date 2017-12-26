package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.components.listeneres.ContextListener;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.dao.DataBaseManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

import static com.juja.webapp.teodor.utils.Logger.*;
import static com.juja.webapp.teodor.utils.Logger.error;

public abstract class Command {
	private final String ID;

    HttpServletRequest httpRequest;
    HttpServletResponse httpResponse;

	Command(String id, HttpServletRequest req, HttpServletResponse resp) {
		ID = id;

        httpRequest = req;
        httpResponse = resp;
	}

	public final String id() {
		return ID;
	}

	public final void execute()
            throws DataBaseRequestException, ServletException, IOException {
		executeInternal();
	}

	protected abstract void executeInternal() throws DataBaseRequestException, ServletException, IOException;

	@Override
	public boolean equals(Object obj) {
		if (obj == null ||
				!(obj instanceof String))
		{
			return false;
		}

		return ID.equals((String)obj);
	}

    public final ConnectionManager connectionManager() {
        return connectionManager(httpRequest.getSession());
    }

    public final ConnectionManager connectionManager(HttpSession httpSession) {
        return (ConnectionManager) httpSession.getServletContext()
                .getAttribute(ContextListener.CONNECTION_MANAGER_CONTEXT_NAME);
    }

    public final Connection connection() {
		return connectionManager().getSessionConnectionInfo(httpRequest.getSession()).connection();
    }

    public final DataBaseManager databaseManager() {
        return dataBaseManager(httpRequest.getSession());
    }

    public final DataBaseManager dataBaseManager(HttpSession httpSession) {
        return (DataBaseManager) httpSession.getServletContext()
                .getAttribute(ContextListener.DATABASE_MANAGER_CONTEXT_NAME);
    }

    public final ResponseProcessor createResponseProcessor() {
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
