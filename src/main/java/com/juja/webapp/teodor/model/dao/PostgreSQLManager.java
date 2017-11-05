package com.juja.webapp.teodor.model.dao;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.postgresql.org/docs/10/static/sql-commands.html
 */
public class PostgreSQLManager extends DataBaseManager {
	private SqlErrorHandler errorHandler;

	private final String CREATE_TABLE_REQUEST_TEMPLATE = "CREATE TABLE %s(%s);";
	private final String DROP_TABLE_REQUEST_TEMPLATE = "DROP TABLE %s;";
	private final String DELETE_TABLE_REQUEST_TEMPLATE = "DELETE FROM %s;";
	private final String SELECT_TABLE_REQUEST_TEMPLATE = "SELECT * FROM %s;";
	private final String INSERT_ROW_REQUEST_TEMPLATE = "INSERT INTO %s(%s) VALUES(%s);";
	private final String DELETE_RECORD_REQUEST_TEMPLATE = "DELETE FROM %s WHERE %s;";
	private final String UPDATE_RECORD_REQUEST_TEMPLATE = "UPDATE %s SET %s WHERE %s;";

	private final String DEFAULT_VALUE = "DEFAULT";

	public PostgreSQLManager(SqlErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

//
//	@Override
//	public void createTable(String tableName, String... columns)
//			throws DataBaseRequestException
//	{
//		StringBuilder sb = new StringBuilder();
//
//		for (int index = 0; index < columns.length; index++) {
//			if (index > 0) {
//				sb.append(", ");
//			}
//			sb.append(columns[index]).append(" TEXT");
//		}
//
//		String sqlQuery = String.format(CREATE_TABLE_REQUEST_TEMPLATE, tableName, sb.toString());
//		execute(sqlQuery);
//	}
//
//	@Override
//	public void dropTable(String tableName)
//			throws DataBaseRequestException
//	{
//		String sqlQuery = String.format(DROP_TABLE_REQUEST_TEMPLATE, tableName);
//		execute(sqlQuery);
//	}
//
//	@Override
//	public void clearTable(String tableName)
//			throws DataBaseRequestException
//	{
//		String sqlQuery = String.format(DELETE_TABLE_REQUEST_TEMPLATE, tableName);
//		execute(sqlQuery);
//	}
//
//	@Override
//	public void insertRow(String tableName, String... keyValue)
//			throws DataBaseRequestException
//	{
//		List<String> keys = extractKeys(keyValue);
//		List<String> values = extractValues(keyValue);
//
//		String sqlQuery = String.format(INSERT_ROW_REQUEST_TEMPLATE, tableName, String.join(",", keys), String.join(",", values));
//		execute(sqlQuery);
//	}
//
//	@Override
//	public int updateRows(String tableName, String column, String value, String[] keyValue)
//			throws DataBaseRequestException
//	{
//		StringBuilder set = prepareSet(keyValue);
//		StringBuilder condition = new StringBuilder();
//		condition.append(column).append("=").append("'").append(value).append("'");
//
//		String sqlQuery = String.format(UPDATE_RECORD_REQUEST_TEMPLATE, tableName,  set.toString(), condition.toString());
//		return executeUpdate(sqlQuery);
//	}
//
//	private StringBuilder prepareSet(String[] keyValue)
//			throws DataBaseRequestException
//	{
//		StringBuilder result = new StringBuilder();
//
//		for (int index = 0; index < keyValue.length; index = index + 2) {
//			if (nullOrEmpty(keyValue[index])) {
//				throw new DataBaseRequestException(RequestError.CMD_ARG_ERROR);
//			}
//
//			result.append(keyValue[index]);
//			if (nullOrEmpty(keyValue[index + 1])) {
//				result.append(DEFAULT_VALUE);
//			} else {
//				result.append("=");
//				result.append("'").append(keyValue[index + 1]).append("'");
//			}
//		}
//
//		return result;
//	}
//
//	@Override
//	public int deleteRows(String tableName, String[] params)
//			throws DataBaseRequestException
//	{
//		String sqlQuery = String.format(
//				DELETE_RECORD_REQUEST_TEMPLATE,
//				tableName,
//				prepareDeleteRowConditions(params)
//		);
//
//		return executeUpdate(sqlQuery);
//	}
//
//	private String prepareDeleteRowConditions(String[] params)
//			throws DataBaseRequestException
//	{
//		StringBuilder preparedString = new StringBuilder();
//
//		for (int index = 0; index < params.length; index = index + 2) {
//			if (index > 0) {
//				preparedString.append(" OR ");
//			}
//			if (index % 2 == 0) {
//				if (nullOrEmpty(params[index])) {
//					throw new DataBaseRequestException(RequestError.CMD_ARG_ERROR);
//				}
//
//				preparedString.append(params[index]);
//				if (nullOrEmpty(params[index + 1])) {
//					preparedString.append(" IS NULL");
//				} else {
//					preparedString.append("=");
//					preparedString.append("'").append(params[index + 1]).append("'");
//				}
//			}
//		}
//
//		return preparedString.toString();
//	}
//
//	@Override
//	public ArrayList<ArrayList<String>> selectTable(String tableName)
//			throws DataBaseRequestException {
//        checkConnection();
//
//        ArrayList<ArrayList<String>> result = new ArrayList<>();
//        String sqlQuery = String.format(SELECT_TABLE_REQUEST_TEMPLATE, tableName);
//
//        try (Statement st = connection.createStatement()) {
//            try (ResultSet rset = st.executeQuery(sqlQuery)) {
//                ResultSetMetaData md = rset.getMetaData();
//
//                int columnsCount = md.getColumnCount();
//
//                if (columnsCount > 0) {
//                    ArrayList<String> columns = new ArrayList<>();
//
//                    for (int i = 1; i <= columnsCount; i++) {
//                        columns.add(md.getColumnName(i));
//                    }
//                    result.add(columns);
//                }
//
//                while (rset.next()) {
//                    ArrayList<String> row = new ArrayList<>();
//                    for (int i = 1; i <= columnsCount; i++) {
//                        row.add(rset.getString(i));
//                    }
//                    result.add(row);
//                }
//            }
//        } catch (SQLException e) {
//            handleSqlError(e, sqlQuery);
//        }
//
//        return result;
//    }
//
//	private int executeUpdate(String sqlQuery)
//			throws DataBaseRequestException
//	{
//		checkConnection();
//
//		int result = 0;
//
//		try (Statement statement = connection.createStatement()) {
//			try {
//				result = statement.executeUpdate(sqlQuery);
//			} catch (SQLException e) {
//				handleSqlError(e, sqlQuery);
//			}
//
//		} catch (SQLException e) {
//			DataBaseRequestException requestException =
//					new DataBaseRequestException(RequestError.CREATE_STATEMENT_ERROR);
//			requestException.addSuppressed(e);
//
//			throw requestException;
//		}
//
//		return result;
//	}
//
//
//	private void execute(String sqlQuery)
//			throws DataBaseRequestException
//	{
//		checkConnection();
//
//		try (Statement statement = connection.createStatement()) {
//			try {
//				statement.execute(sqlQuery);
//			} catch (SQLException e) {
//				handleSqlError(e, sqlQuery);
//			}
//
//		} catch (SQLException e) {
//			DataBaseRequestException requestException =
//					new DataBaseRequestException(RequestError.CREATE_STATEMENT_ERROR);
//			requestException.addSuppressed(e);
//
//			throw requestException;
//		}
//	}
//
//	@Override
//	public List<String> getTables()
//			throws DataBaseRequestException
//	{
//		checkConnection();
//
//		final String TABLE_TYPE = "TABLE";
//		final String COULMN_LABEL = "TABLE_NAME";
//
//		List<String> result = new ArrayList<>();
//
//		try {
//			DatabaseMetaData md = connection.getMetaData();
//			try (ResultSet rs =
//                         md.getTables(null, null, "%", new String[]{TABLE_TYPE}))
//            {
//				while (rs.next()) {
//					result.add(rs.getString(COULMN_LABEL));
//				}
//			}
//		} catch (SQLException e) {
//			DataBaseRequestException exc = new DataBaseRequestException(
//					RequestError.REQUEST_ERROR,
//					"Get tables request error"
//			);
//
//			exc.addSuppressed(exc);
//
//			throw exc;
//		}
//
//		return result;
//	}

	private boolean nullOrEmpty(String str) {
		return str == null || str.equals("");
	}

//	private void checkConnection()
//			throws DataBaseRequestException
//	{
//		if (connection == null) {
//			throw new DataBaseRequestException(RequestError.CONNECTION_DOESNT_SET);
//		}
//	}

	private List<String> extractKeys(String[] keyValue)
			throws DataBaseRequestException
	{
		List<String> result = new ArrayList<>();

		for (int index = 0; index < keyValue.length; index = index + 2) {
			if (nullOrEmpty(keyValue[index])) {
				throw new DataBaseRequestException(RequestError.CMD_ARG_ERROR);
			}

			result.add(keyValue[index]);
		}

		return result;
	}

	private List<String> extractValues(String[] keyValue) {
		List<String> result = new ArrayList<>();

		for (int index = 1; index < keyValue.length; index = index + 2) {
			if(nullOrEmpty(keyValue[index])) {
				result.add(DEFAULT_VALUE);
			} else {
				result.add("'" + keyValue[index] + "'");
			}
		}

		if (keyValue.length % 2 != 0) {
			result.add(DEFAULT_VALUE);
		}

		return result;
	}
}