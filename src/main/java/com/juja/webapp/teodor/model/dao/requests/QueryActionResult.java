package com.juja.webapp.teodor.model.dao.requests;

public class QueryActionResult {
    private static final Object EMPTY_RESULT = new Object();
    private Object result;

    public QueryActionResult() {
        this(EMPTY_RESULT);
    }

    public QueryActionResult(Object result) {
        this.result = result;
    }

    public boolean isEmpty() {
        return this.result == EMPTY_RESULT;
    }

    public Object result() {
        return this.result;
    }

    public static QueryActionResult getEmptyQueryResult() {
        return new QueryActionResult();
    }
}
