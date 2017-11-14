package com.juja.webapp.teodor.model.dao;

import com.juja.webapp.teodor.model.dao.requests.QueryActionResult;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import com.juja.webapp.teodor.model.exceptions.RequestError;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractQueryAction {

    private final String queryString;
    private Connection connection;

    public AbstractQueryAction(Connection connection, String queryString) {
        this.connection = connection;
        this.queryString = queryString;
    }

    public String queryString() {
        return queryString;
    }

    protected Connection connection() {
        return connection;
    }

    final void execute() throws SQLException, DataBaseRequestException {
        checkConnection(connection);

        try (Statement statement = connection().createStatement()) {
            executeInternal(statement);
        } catch (SQLException e) {
            DataBaseRequestException requestException =
                    new DataBaseRequestException(RequestError.CREATE_STATEMENT_ERROR);
            requestException.addSuppressed(e);

            throw requestException;
        }

    }

    abstract protected void executeInternal(Statement statement) throws SQLException, DataBaseRequestException;

    public QueryActionResult queryResult() {
        return new QueryActionResult();
    }

    private void checkConnection(Connection connection)
            throws DataBaseRequestException
    {
        if (connection == null) {
            throw new DataBaseRequestException(RequestError.CONNECTION_DOESNT_SET);
        }
    }
}
