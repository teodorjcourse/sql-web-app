package com.juja.webapp.teodor.controller.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Commands {

    public void tryToExecute(String command, HttpServletRequest req, HttpServletResponse resp)
            throws Exception
    {
        switch (command) {
            case "connect":
                new ConnectCommand(req, resp).execute();
                break;
            case "close":
                new CloseConnection(req, resp).execute();
                break;
            case "get_tables":
                new GetTablesCommand(req, resp).execute();
                break;
            case "select":
                new SelectTableCommand(req, resp).execute();
                break;
            case "insert":
                new InsertRowCommand(req, resp).execute();
                break;
            case "update":
                new UpdateTableCommand(req, resp).execute();
                break;
            case "delete":
                new DeleteRowCommand(req, resp).execute();
                break;

        }
    }
}
