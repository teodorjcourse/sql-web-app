package com.juja.webapp.teodor;

public class TranslationBase {

	public String getTranslation(String key) {
		return String.format("Translation for key [%s] doesnt exist", key);
	}
}
