package com.juja.webapp.teodor.model.dao;

import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import java.sql.SQLException;

public class PostgreSqlErrorHandler implements SqlErrorHandler {
    public void handleSqlError(SQLException exception, String sqlQuery, short errCodeByDefault)
            throws DataBaseRequestException {
        if (exception != null) {
            short errorCode;

            switch (exception.getSQLState()) {
                case "3D000":
                    errorCode = DATABASE_DOESNT_EXISTS_ERROR;
                    break;
                case "42601":
                    errorCode = SYNTAX_ERROR;
                    break;
                case "42P04":
                    errorCode = DATABASE_ALREADY_EXISTS_ERROR;
                    break;
                case "42P01":
                    errorCode = TABLE_DOESNT_EXISTS_ERROR;
                    break;
                case "42P07":
                    errorCode = TABLE_ALREADY_EXISTS_ERROR;
                    break;
                case "55006":
                    errorCode = DATABASE_IN_USE_DROP_ERROR;
                    break;
                default:
                    errorCode = errCodeByDefault;
            }

            DataBaseRequestException requestException;
            if (sqlQuery != null) {
                requestException =
                        new DataBaseRequestException(
                                errorCode,
                                String.format("SQL query error: %s", sqlQuery)
                        );
            } else {
                requestException =
                        new DataBaseRequestException(
                                errorCode);
            }

            requestException.addSuppressed(exception);

            throw requestException;
        }
    }

    public void handleSqlError(SQLException exception, short errCodeByDefault)
            throws DataBaseRequestException
    {
        handleSqlError(exception, null, errCodeByDefault);
    }

    public void handleSqlError(SQLException exception, String sqlQuery)
            throws DataBaseRequestException
    {
        handleSqlError(exception, sqlQuery, REQUEST_ERROR);
    }
}
