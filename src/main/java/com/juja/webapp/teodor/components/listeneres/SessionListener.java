package com.juja.webapp.teodor.components.listeneres;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.utils.Logger;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 * SessionListener will create userSession object and store it at session attributes to handle connection state all over the
 * servlets.
 * When user session is expired SessionListener will close connection and remove all attributes associated with session.
 */
public class SessionListener implements HttpSessionListener {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	@Override
	public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        Logger.info(logger, "Create session: id = " + session.getId());

        UserSession userSession = new UserSession(session);
        session.setAttribute(WebAppAttributes.USER_SESSION, userSession);

		Logger.info(logger, "Create session: id = " + session.getId() + " done");
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();

		Logger.info(logger, "Destroy session: " + session.getId());

		ServletContext servletContext = session.getServletContext();

		ConnectionManager connectionManager = (ConnectionManager) servletContext
				.getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

		try {
			Logger.info(logger, session.getId() + "Close conection");
			connectionManager.closeConnection(session);
			connectionManager.removeConnection(session);
			Logger.info(logger, session.getId() + "Close conection done");
		} catch (DataBaseRequestException e) {
			Logger.error(logger, "error on destroy session", e);
		}

		session.removeAttribute(WebAppAttributes.USER_SESSION);


		Logger.info(logger, "Destroy session: " + session.getId() + " done");
	}
}
