package com.juja.webapp.teodor.components.listeneres;
import com.juja.webapp.teodor.utils.Logger;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 *
 * When user session is expired SessionListener will close connection
 */
public class SessionListener implements HttpSessionListener {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	@Override
	public void sessionCreated(HttpSessionEvent se) {
        Logger.info(logger, "Session created: id = " + se.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();

        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        ConnectionManager connectionManager = (ConnectionManager) webApplicationContext.getBean("connectionManager");

        Logger.info(logger, "Destroy session: " + session.getId());

		try {
			Logger.info(logger, session.getId() + "Close conection");
			connectionManager.closeConnection(session);
			connectionManager.removeConnection(session);
			Logger.info(logger, session.getId() + "Close conection done");
		} catch (DataBaseRequestException e) {
			Logger.error(logger, "error on destroy session", e);
		}

		Logger.info(logger, "Destroy session: " + session.getId() + " done");
	}
}
