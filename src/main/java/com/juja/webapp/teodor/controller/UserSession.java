package com.juja.webapp.teodor.controller;

import com.juja.webapp.teodor.WebAppAttributes;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.dao.ConnectionManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class UserSession {
	private HttpSession httpSession;

	public UserSession(HttpSession httpSession) {
		if (httpSession == null) {
			throw new IllegalArgumentException(
					"<httpSession> argument cannot be set as NULL.");
		}

		this.httpSession = httpSession;
	}

	public boolean connected() throws SQLException {
		ServletContext servletContext = httpSession.getServletContext();
		ConnectionManager connectionManager = (ConnectionManager) servletContext
				.getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

		ConnectionInfo connectionInfo = connectionManager.getSessionConnectionInfo(httpSession);
		return connectionInfo != null ? connectionInfo.connected() : false;
	}
}
