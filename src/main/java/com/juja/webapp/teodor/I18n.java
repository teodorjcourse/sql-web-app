package com.juja.webapp.teodor;

public class I18n {
    private static TranslationBase translationBase;

    static {
        translationBase = new TranslationBase();
    }

    public static String text(String key) {
        return "[" + key + "]";
//        return translationBase.getTranslation(key);
    }
}
