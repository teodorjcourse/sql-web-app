package com.juja.webapp.teodor.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionInfo {
	protected String database;
	protected String username;
	protected String password;

	protected Connection connection;

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

	public boolean connected() {
		try {
			return this.connection != null && !this.connection.isClosed();
		} catch (Throwable any) {
			return false;
		}
	}
}
