package com.juja.webapp.teodor.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    static final String JDBC_DRIVER = "jdbc.driver";
    static final String JDBC_HOST = "jdbc.host";
    static final String JDBC_PORT = "jdbc.port";
    static final String JDBC_CONNECTION_STRING = "jdbc.connection_string";
    static final String JDBC_USERNAME = "jdbc.username";
    static final String JDBC_PASSWORD = "jdbc.password";
    static final String JDBC_DATABASE = "jdbc.database";

    private Properties props;
    private StringBuilder connectionString;

    public Configuration(String fileName) throws IOException {
        this(new Loader(fileName).properties());
    }

    public Configuration(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException();
        }

        props = properties;
    }

    public Configuration() {
        this(PostgreSQLProperties.properties());
    }



    public String driver() {
        return props.getProperty(JDBC_DRIVER);
    }

    public String host() {
        return props.getProperty(JDBC_HOST);
    }

    public String port() {
        return props.getProperty(JDBC_PORT);
    }

    public String connectionString() {
        if (connectionString == null) {
            connectionString = new StringBuilder();
            connectionString.append(props.getProperty(JDBC_CONNECTION_STRING));
            connectionString.append(host());
            connectionString.append(":");
            connectionString.append(port());
            connectionString.append("/");
        }

        return connectionString.toString();
    }

    public String username() {
        return props.getProperty(JDBC_USERNAME);
    }

    public String password() {
        return props.getProperty(JDBC_PASSWORD);
    }

    public String database() {
        return props.getProperty(JDBC_DATABASE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Database configuartion: ");

        sb.append("%n\t\t").append("Driver: ").append(driver());
        sb.append("%n\t\t").append("Connection string: ").append(props.getProperty(JDBC_CONNECTION_STRING));
        sb.append("%n\t\t").append("Host: ").append(host());
        sb.append("%n\t\t").append("Port: ").append(port());
        sb.append("%n\t\t").append("Username: ").append(username());
        sb.append("%n\t\t").append("Password: ").append("********");

        return String.format(sb.toString());
    }
}


class PostgreSQLProperties {
    private PostgreSQLProperties() {}

    public static Properties properties() {
        Properties props = new Properties();

        props.setProperty("jdbc.driver", "org.postgresql.Driver");
        props.setProperty("jdbc.connection_string", "jdbc:postgresql://");
        props.setProperty("jdbc.host", "localhost");
        props.setProperty("jdbc.port", "5432");
        props.setProperty("jdbc.username", "postgres");
        props.setProperty("jdbc.password", "qwerty");
        props.setProperty("jdbc.database", "test");

        return props;
    }
}

class Loader {
    private String filePath;

    public Loader(String filePath) {
        this.filePath = filePath;
    }

    public Properties properties()
            throws IOException
    {
        InputStream is = this.getClass().getResourceAsStream(filePath);

        Configuration conf = null;
        Properties props = new Properties();

        props.load(is);

        return props;
    }
}