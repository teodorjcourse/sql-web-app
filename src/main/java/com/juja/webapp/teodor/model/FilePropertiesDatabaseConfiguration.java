package com.juja.webapp.teodor.model;

import com.juja.webapp.teodor.model.dao.DatabaseConfiguration;

import java.io.IOException;
import java.util.Properties;


public class FilePropertiesDatabaseConfiguration implements DatabaseConfiguration {

    static final String JDBC_DRIVER = "jdbc.driver";
    static final String JDBC_HOST = "jdbc.host";
    static final String JDBC_PORT = "jdbc.port";
    static final String JDBC_CONNECTION_STRING = "jdbc.connection_string";
    static final String JDBC_USERNAME = "jdbc.username";
    static final String JDBC_PASSWORD = "jdbc.password";
    static final String JDBC_DATABASE = "jdbc.database";

    private Properties properties;

    public FilePropertiesDatabaseConfiguration(String resourcePath) {
        if (resourcePath == null) {
            throw new NullPointerException("Resource file can not be set as NULL.");
        }

        try {
            PropertiesLoader loader = new PropertiesLoader(resourcePath);
            properties = loader.properties();
        } catch (IOException e) {
            throw new RuntimeException("Error while loading properties file: " + resourcePath);
        }
    }

    public FilePropertiesDatabaseConfiguration(Properties properties) {
        if (properties == null) {
            throw new NullPointerException("Properties can not be set as NULL.");
        }

        this.properties = properties;
    }

    @Override
    public String connectionString() {
//        if (connectionString == null) {
//            connectionString = new StringBuilder();
//            connectionString.append(properties.getProperty(JDBC_CONNECTION_STRING));
//            connectionString.append(host());
//            connectionString.append(":");
//            connectionString.append(port());
//            connectionString.append("/");
//        }

        return properties.getProperty(JDBC_CONNECTION_STRING);
    }

    @Override
    public String driver() {
        return properties.getProperty(JDBC_DRIVER);
    }

    @Override
    public String host() {
        return properties.getProperty(JDBC_HOST);
    }

    @Override
    public String port() {
        return properties.getProperty(JDBC_PORT);
    }

    @Override
    public String database() {
        return properties.getProperty(JDBC_DATABASE);
    }

    @Override
    public String username() {
        return properties.getProperty(JDBC_USERNAME);
    }

    @Override
    public String password() {
        return properties.getProperty(JDBC_PASSWORD);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Database configuartion: ");

        sb.append("%n\t\t").append("Driver: ").append(driver());
        sb.append("%n\t\t").append("Connection string: ").append(connectionString());
        sb.append("%n\t\t").append("Host: ").append(host());
        sb.append("%n\t\t").append("Port: ").append(port());
        sb.append("%n\t\t").append("Username: ").append(username());
        sb.append("%n\t\t").append("Password: ").append("********");

        return String.format(sb.toString());
    }
}
