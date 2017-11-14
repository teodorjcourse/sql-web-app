package com.juja.webapp.teodor.model.dao.requests;

import com.juja.webapp.teodor.model.dao.AbstractQueryAction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertQueryAction extends AbstractQueryAction {
    private static final String[] NO_RETURNING_COLUMNS = new String[0];

    private QueryActionResult actionResult = QueryActionResult.getEmptyQueryResult();
    private String[] columnNames;

    /**
     * @param columnNames an array of the names of the columns in the inserted
     *        row that should be made available for retrieval by a call to the
     *        method <code>getGeneratedKeys</code>1
     */
    public InsertQueryAction(Connection connection, String queryString, String[] columnNames) {
        super(connection, queryString);

        this.columnNames = columnNames;
    }

    public InsertQueryAction(Connection connection, String queryString) {
        this(connection, queryString, NO_RETURNING_COLUMNS);
    }

    @Override
    protected void executeInternal(Statement statement) throws SQLException {
        statement.execute(queryString(), columnNames);

        try(ResultSet rs = statement.getGeneratedKeys()) {
            if (rs != null) {
                if (rs.next()) {
                    int nextUID = rs.getInt(1);
                    actionResult = new QueryActionResult(nextUID);
                }
            }
        }
    }

    @Override
    public QueryActionResult queryResult() {
        return actionResult;
    }
}
