package com.juja.webapp.teodor.utils;

public class StringUtils {
    public static String removeSlashes(String source) {
        return source.replaceAll("/", "");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    private StringUtils() {}
}
