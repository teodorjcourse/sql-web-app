package com.juja.webapp.teodor.model.dao.requests;

import com.juja.webapp.teodor.model.dao.AbstractQueryAction;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleQueryAction extends AbstractQueryAction {

    public SimpleQueryAction(Connection connection, String queryString) {
        super(connection, queryString);
    }

    @Override
    protected void executeInternal(Statement statement) throws SQLException, DataBaseRequestException {
        statement.execute(queryString());
    }

    @Override
    public QueryActionResult queryResult() {
        return null;
    }
}
