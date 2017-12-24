package com.juja.webapp.teodor.components.listeneres;

import com.juja.webapp.teodor.model.FilePropertiesDatabaseConfiguration;
import com.juja.webapp.teodor.model.PropertiesLoader;
import com.juja.webapp.teodor.model.dao.*;
import com.juja.webapp.teodor.utils.Logger;
import com.juja.webapp.teodor.WebAppAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

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

		String dbConfigurationFileProperties = servletContext.getInitParameter("databaseProperties");
        DatabaseConfiguration databaseConfiguration;

        try {
            PropertiesLoader loader = new PropertiesLoader(dbConfigurationFileProperties);
            loader.load();

            databaseConfiguration = new FilePropertiesDatabaseConfiguration(loader.properties());
            servletContext.setAttribute(WebAppAttributes.DATABASE_CONNECTION_MANAGER, new ConnectionManager(databaseConfiguration, errorHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
