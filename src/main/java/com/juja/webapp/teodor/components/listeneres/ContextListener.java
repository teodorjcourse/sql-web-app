package com.juja.webapp.teodor.components.listeneres;

import com.juja.webapp.teodor.utils.Logger;
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

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 * {@link ConnectionManager} and {@link PostgreSQLManager} will be created on contextInitialized event
 * and set as attributes to servlet context.
 *
 * When contextDestroyed event happens all managers will be removed from  servlet context as well as session connections
 * stored in {@link ConnectionManager}.
 */
public class ContextListener implements ServletContextListener {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	public void contextInitialized(ServletContextEvent sce) {
        Logger.info(logger, "On servlet context initialized");

		ServletContext servletContext = sce.getServletContext();
		SqlErrorHandler errorHandler = new PostgreSqlErrorHandler();
        Configuration configuration = loadDatabaseConfiguration(Links.DATABASE_PROPERTIES);

		servletContext.setAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER, new ConnectionManager(configuration, errorHandler));
		servletContext.setAttribute(WebAppAttributes.DATABASE_MANAGER, new PostgreSQLManager(errorHandler));

		Logger.info(logger, "On servlet context initialized done");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		Logger.info(logger, "On servlet context destroy");

		ServletContext servletContext = sce.getServletContext();

		ConnectionManager connectionManager = (ConnectionManager) servletContext
				.getAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);

		connectionManager.removeAll();

		servletContext.removeAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER);
		servletContext.removeAttribute(WebAppAttributes.DATABASE_MANAGER);

		Logger.info(logger, "On servlet context destroy done");
	}

	private Configuration loadDatabaseConfiguration(String filePath) {
		Logger.info(logger, "Get database configuration from file: " + filePath);

        InputStream is = this.getClass().getResourceAsStream(filePath);

        Configuration conf = null;
        Properties dbProperties = new Properties();

        try {
	        Logger.info(logger, "Load database configuration from: " + filePath);

            dbProperties.load(is);

            conf = new Configuration(dbProperties);

	        Logger.info(logger, "Database configuration has been successfully set");
	        Logger.info(logger,conf.toString());

        } catch (Throwable any) {

	        Logger.warn(logger, "Error happend during database configuration load, any");
	        Logger.warn(logger, "Apply default database configuration");

            conf = new Configuration();

	        Logger.info(logger,conf.toString());
        }

        return conf;
    }
}
