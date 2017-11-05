package com.juja.webapp.teodor.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionInfo {
	String database;
	String username;
	String password;

	Connection connection;

	public String database() {
		return this.database;
	}

	public String username() {
		return this.username;
	}

	public String password() {
		return this.password;
	}

	public Connection connection() {
		return this.connection;
	}

	public boolean connected() throws SQLException {
		return this.connection != null && !this.connection.isClosed();
	}
}
