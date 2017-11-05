package com.juja.webapp.teodor.utils;

import org.apache.log4j.Logger;

public class DebugLoger {
    public static void tr(String message) {
        System.out.println(message);
    }

    public static void loggerInfo(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void loggerDebug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void loggerError(Logger logger, String message) {
        logger.error(message);
    }
}
