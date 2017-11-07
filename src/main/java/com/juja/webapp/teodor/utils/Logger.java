package com.juja.webapp.teodor.utils;

public class Logger {

    public static void info(org.apache.log4j.Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void debug(org.apache.log4j.Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void error(org.apache.log4j.Logger logger, String message) {
        logger.error(message);
    }

    public static void error(org.apache.log4j.Logger logger, String message, Throwable t) {
        logger.error(message, t);
    }

    public static void warn(org.apache.log4j.Logger logger, String message) {
        logger.warn(message);
    }
}
