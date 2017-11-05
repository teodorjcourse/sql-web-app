package com.juja.webapp.teodor.model.dao;

import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;

import java.sql.SQLException;

public interface SqlErrorHandler extends RequestError {
    void handleSqlError(SQLException exception, String sqlQuery, short errCodeByDefault) throws DataBaseRequestException;
    void handleSqlError(SQLException exception, short errCodeByDefault) throws DataBaseRequestException;
    void handleSqlError(SQLException exception, String sqlQuery) throws DataBaseRequestException;
}
