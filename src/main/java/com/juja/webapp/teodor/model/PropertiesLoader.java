package com.juja.webapp.teodor.model;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    private String filePath;
    private Properties properties;

    private boolean loaded = false;

    public PropertiesLoader(String filePath) throws IOException {
        this.filePath = filePath;

        load();
    }

    public void load() throws IOException {
        if (!loaded) {
            properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream(filePath));
            loaded = true;
        }
    }

    public Properties properties() {
        if (loaded) {
            return  properties;
        } else {
            throw new RuntimeException("You must load properties first. Use 'load' method.");
        }
    }
}
