package com.juja.webapp.teodor.model.exceptions;

public interface RequestError {
    // ошибка загрузки драйвера
    short DRIVER_LOAD_ERROR = 2;

    // Ошибка соединения с БД, во время выполнения sql запроса
    short CONNECTION_ERROR = 4;

    //Ошибка создания соединение с БД
    short CONNECTION_SET_ERROR = 5;

    short CONNECTION_DOESNT_SET = 6;

    short CONNECTION_ALREADY_SET = 7;

    //Ощибка выполнения запроса
    short REQUEST_ERROR = 8;
    short SYNTAX_ERROR = 9;

    // ошибка создания стейтмента
    short CREATE_STATEMENT_ERROR = 10;

    // ошибка недопустимого значения переменной или его отсутствия
    short CMD_ARG_ERROR = 11;
    // ошибка во время закрытия соединения
    short CLOSE_CONNECTION_ERROR = 12;


    short DATABASE_ALREADY_EXISTS_ERROR = 16;
    short DATABASE_DOESNT_EXISTS_ERROR = 17;
    short DATABASE_IN_USE_DROP_ERROR = 18;

    short TABLE_ALREADY_EXISTS_ERROR = 20;
    short TABLE_DOESNT_EXISTS_ERROR = 21;
}
