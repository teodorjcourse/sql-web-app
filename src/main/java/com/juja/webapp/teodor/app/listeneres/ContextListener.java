package com.juja.webapp.teodor.app.listeneres;

import com.juja.webapp.teodor.utils.DebugLoger;
import com.juja.webapp.teodor.model.Configuration;
import com.juja.webapp.teodor.model.dao.ConnectionManager;
import com.juja.webapp.teodor.model.dao.PostgreSQLManager;
import com.juja.webapp.teodor.model.dao.PostgreSqlErrorHandler;
import com.juja.webapp.teodor.model.dao.SqlErrorHandler;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@link ConnectionManager} and {@link PostgreSQLManager} will be created on contextInitialized event
 * and set as attributes to servlet context.
 *
 * When contextDestroyed event happens all managers will be removed from  servlet context as well as session connections
 * stored in {@link ConnectionManager}.
 */
public class ContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
        DebugLoger.tr("Initialize servlet contex");

		ServletContext servletContext = sce.getServletContext();
		SqlErrorHandler errorHandler = new PostgreSqlErrorHandler();
        Configuration configuration = loadDatabaseConfiguration(Links.DATABASE_PROPERTIES);

		servletContext.setAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER, new ConnectionManager(configuration, errorHandler));
		servletContext.setAttribute(WebAppAttributes.DATABASE_MANAGER, new PostgreSQLManager(errorHandler));

        DebugLoger.tr("Initialize servlet contex done");
	}

	public void contextDestroyed(ServletContextEvent sce) {
        DebugLoger.tr("Destroy servlet contex");
		ServletContext servletContext = sce.getServletContext();

		ConnectionManager connectionManager = (ConnectionManager) servletContext
				.getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

		connectionManager.removeAll();

		servletContext.removeAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);
		servletContext.removeAttribute(WebAppAttributes.DATABASE_MANAGER);
        DebugLoger.tr("Destroy servlet contex done");
	}

	private Configuration loadDatabaseConfiguration(String filePath) {
        InputStream is = this.getClass().getResourceAsStream(filePath);

        Configuration conf = null;
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(is);
            conf = new Configuration(dbProperties);
        } catch (Throwable any) {
            conf = new Configuration();
        }

        return conf;
    }
}
