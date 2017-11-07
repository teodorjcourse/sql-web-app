package com.juja.webapp.teodor.utils;

/**
 * Thanks to Ivan Golovach Courses
 * Taken from https://youtu.be/lepwubmYGQc?t=357
 */
public final class ClassNameUtil {
	public static String getCurrentClassName() {
		try {
			throw new RuntimeException();
		} catch (RuntimeException exc) {
			return exc.getStackTrace()[1].getClassName();
		}
	}

	private ClassNameUtil() {}
}
