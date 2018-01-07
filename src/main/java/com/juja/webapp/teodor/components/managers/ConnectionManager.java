package com.juja.webapp.teodor.components.managers;

import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.dao.DatabaseConfiguration;
import com.juja.webapp.teodor.model.dao.SqlErrorHandler;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

@Component(value = "connectManager")
public class ConnectionManager {
    StringBuilder connectionString;
	private SqlErrorHandler errorHandler;
	private DatabaseConfiguration configuration;

	private HashMap<HttpSession, ConnectionInfo> activeConnections;

	public ConnectionManager(DatabaseConfiguration configuration, SqlErrorHandler errorHandler) {
		this.activeConnections = new HashMap<>();
		this.errorHandler = errorHandler;
		this.configuration = configuration;

        this.connectionString = new StringBuilder();
        this.connectionString.append(configuration.connectionString());
        this.connectionString.append(configuration.host());
        this.connectionString.append(":");
        this.connectionString.append(configuration.port());
        this.connectionString.append("/");
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
		sb.append(connectionString).append(database);

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
                connectionInfo.connection().close();
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

	public void destroy() {
		activeConnections.clear();
	}

	public ConnectionInfo getSessionConnectionInfo(HttpSession session) {
		return activeConnections.get(session);
	}
}

class DatabaseConnectionInfo extends ConnectionInfo {


	public void setUsername(String value) {
		username = value;
	}

	public void setDatabase(String value) {
		database = value;
	}

	public void setPassword(String value) {
		password = value;
	}

	public void setConnection(Connection value) {
		connection = value;
	}


	DatabaseConnectionInfo() {}

}
