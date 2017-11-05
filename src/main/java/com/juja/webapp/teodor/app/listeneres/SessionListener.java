package com.juja.webapp.teodor.app.listeneres;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.utils.DebugLoger;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListener will create userSession object and store it at session attributes to handle connection state all over the
 * servlets.
 * When user session is expired SessionListener will close connection and remove all attributes associated with session.
 */
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        DebugLoger.tr("Create session: " + session.getId());

        UserSession userSession = new UserSession(session);
        session.setAttribute(WebAppAttributes.USER_SESSION, userSession);
        DebugLoger.tr("Create session: " + session.getId() + " done");
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
        DebugLoger.tr("Destroy session: " + session.getId());
		ServletContext servletContext = session.getServletContext();

		ConnectionManager connectionManager = (ConnectionManager) servletContext
				.getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

		try {
			DebugLoger.tr("closeConnection: " + session.getId() + " done");
			connectionManager.closeConnection(session);
			DebugLoger.tr("removeConnection: " + session.getId() + " done");
			connectionManager.removeConnection(session);
		} catch (DataBaseRequestException e) {
			DebugLoger.tr(e.getMessage() + ": " + session.getId() + " error on sessionDestroyed");
			// NOP
		}

		session.removeAttribute(WebAppAttributes.USER_SESSION);
        DebugLoger.tr("Destroy session: " + session.getId() + " done");
	}
}
