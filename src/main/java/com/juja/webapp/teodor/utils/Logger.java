package com.juja.webapp.teodor.utils;

public class Logger {
    private org.apache.log4j.Logger logger;

    public Logger(String name) {
        logger = org.apache.log4j.Logger.getLogger(name);
    }

    public void info(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public void warn(String message) {
        logger.warn(message);
    }
}
