package com.juja.webapp.teodor.model.dao;

import com.juja.webapp.teodor.model.dao.requests.QueryActionResult;
import com.juja.webapp.teodor.model.dao.table.Table;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import java.sql.Connection;
import java.util.List;

public abstract class DataBaseManager {
	public abstract QueryActionResult insertRow(Connection connection, String tableName, String[] keyValue) throws DataBaseRequestException;
  	public abstract QueryActionResult insertRow(Connection connection, String tableName, String[] keyValue, String[] columnNames) throws DataBaseRequestException;

	public abstract int deleteRows(Connection connection, String tableName, String[] params) throws DataBaseRequestException;

	public abstract List<String> getTables(Connection connection) throws DataBaseRequestException;

	public abstract Table selectTable(Connection connection, String tableName) throws DataBaseRequestException;

	public abstract int updateRows(Connection connection, String tableName, String column, String value, String[] keyValue) throws DataBaseRequestException;
}
