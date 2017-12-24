package com.juja.webapp.teodor.model.dao;

public interface DatabaseConfiguration {
    String connectionString();
    String driver();
    String host();
    String port();
    String database();
    String username();
    String password();
}
