package com.juja.webapp.teodor.components.listeneres;

import com.juja.webapp.teodor.model.dao.*;
import com.juja.webapp.teodor.utils.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 * {@link ConnectionManager} and {@link PostgreSQLManager} will be created on contextInitialized event
 * and set as attributes to servlet context.
 *
 * When contextDestroyed event happens all managers will be removed from  servlet context as well as session connections
 * stored in {@link ConnectionManager}.
 */
public class ContextListener implements ServletContextListener {
    public static final String CONNECTION_MANAGER_CONTEXT_NAME = "connectionManager";
    public static final String DATABASE_MANAGER_CONTEXT_NAME = "databaseManager";
    public static final String COMMANDS_MANAGER_CONTEXT_NAME = "commandsManager";

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	public void contextInitialized(ServletContextEvent sce) {
        Logger.info(logger, "On servlet context initialized");


        ServletContext servletContext = sce.getServletContext();
        XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(servletContext);
        webApplicationContext.registerShutdownHook();

        servletContext.setAttribute(CONNECTION_MANAGER_CONTEXT_NAME, webApplicationContext.getBean("connectionManager"));
        servletContext.setAttribute(DATABASE_MANAGER_CONTEXT_NAME, webApplicationContext.getBean("databaseManager"));
        servletContext.setAttribute(COMMANDS_MANAGER_CONTEXT_NAME, webApplicationContext.getBean("commandsManager"));

        Logger.info(logger, "On servlet context initialized done");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		Logger.info(logger, "On servlet context destroy");

        XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        webApplicationContext.destroy();

		Logger.info(logger, "On servlet context destroy done");
	}
}
