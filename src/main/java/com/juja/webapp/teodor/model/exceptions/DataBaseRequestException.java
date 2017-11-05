package com.juja.webapp.teodor.model.exceptions;

public class DataBaseRequestException extends Exception {
    private short error;
    private String description;

    public DataBaseRequestException(short errCode, String message) {
        super(message);

        this.error = errCode;
    }

    public DataBaseRequestException(short errCode) {
        this(errCode, "Request exception");
    }

    public short error() {
        return error;
    }
}
