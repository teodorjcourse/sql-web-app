package com.juja.webapp.teodor.model.dao;

import com.juja.webapp.teodor.model.Configuration;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionManager {
	private SqlErrorHandler errorHandler;
	private Configuration configuration;

	private HashMap<HttpSession, ConnectionInfo> activeConnections;

	public ConnectionManager(Configuration configuration, SqlErrorHandler errorHandler) {
		this.activeConnections = new HashMap<>();
		this.errorHandler = errorHandler;
		this.configuration = configuration;
	}

	public void createConnection(HttpSession session, String username, String password)
			throws DataBaseRequestException {

		loadDriver(configuration.driver());
		setConnection(session, configuration.database(), username, password);
	}

	private void loadDriver(String driver)
			throws DataBaseRequestException
	{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {

			DataBaseRequestException exc =
					new DataBaseRequestException(
							RequestError.DRIVER_LOAD_ERROR,
							String.format("Driver \"%s\" load error", driver)
					);

			exc.addSuppressed(e);

			throw exc;
		}
	}

	private void setConnection(HttpSession session, String database, String username, String password)
			throws DataBaseRequestException
	{

		StringBuilder sb = new StringBuilder();
		sb.append(configuration.connectionString()).append(database);

		try {
			Connection connection = DriverManager.getConnection(sb.toString(), username, password);
			DatabaseConnectionInfo connectionInfo = new DatabaseConnectionInfo();

			connectionInfo.setDatabase(database);
			connectionInfo.setUsername(username);
			connectionInfo.setPassword(username);
			connectionInfo.setConnection(connection);

			activeConnections.put(session, connectionInfo);

		} catch (SQLException e) {
			errorHandler.handleSqlError(e, RequestError.CONNECTION_SET_ERROR);
		}
	}


	public void closeConnection(HttpSession session)
			throws DataBaseRequestException

	{
        ConnectionInfo connectionInfo = (ConnectionInfo) activeConnections.get(session);


		if (connectionInfo != null) {
			try {
                connectionInfo.connection.close();
			} catch (SQLException e) {
				DataBaseRequestException requestException =
						new DataBaseRequestException(RequestError.CLOSE_CONNECTION_ERROR);
				requestException.addSuppressed(e);

				throw new DataBaseRequestException(RequestError.CLOSE_CONNECTION_ERROR);
			}
		}
	}

	public void removeConnection(HttpSession session) {
		activeConnections.remove(session);
	}

	public void removeAll() {
		activeConnections.clear();
	}

	public ConnectionInfo getSessionConnectionInfo(HttpSession session) {
		return activeConnections.get(session);
	}
}

class DatabaseConnectionInfo extends ConnectionInfo {
	public void setUsername(String value) {
		this.username = value;
	}

	public void setDatabase(String value) {
		this.database = value;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public void setConnection(Connection value) {
		this.connection = value;
	}


	DatabaseConnectionInfo() {}

}
