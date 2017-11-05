package com.juja.webapp.teodor.utils;

public class StringUtils {
    public static String removeSlashes(String source) {
        return source.replaceAll("/", "");
    }


    private StringUtils() {}
}
