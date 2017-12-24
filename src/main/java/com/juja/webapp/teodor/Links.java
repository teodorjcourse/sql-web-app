package com.juja.webapp.teodor;

public interface Links {

    String MAIN_SERVLET = "/main";


    String ROOT_WEB_APP = "/sql-web-app";

    String CONNECT_JSP = "/jsp/Connect.jsp";
    String MAIN_JSP = "/jsp/Main.jsp";

    String ACTION_CONNECT_REQUEST = "/sql-web-app/action/connect?";
    String ACTION_CLOSE_CONNECTION_REQUEST = "/sql-web-app/action/close?";
    String ACTION_GET_TABLES_REQUEST = "/sql-web-app/action/get_tables?";
    String ACTION_INSERT_REQUEST = "/sql-web-app/action/insert?";
}
