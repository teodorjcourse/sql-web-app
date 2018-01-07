var RequestErrors = {
    // ошибка загрузки драйвера
    DRIVER_LOAD_ERROR : 2,

    // Ошибка соединения с БД, во время выполнения sql запроса
    CONNECTION_ERROR : 4,

    //Ошибка создания соединение с БД
    CONNECTION_SET_ERROR : 5,

    CONNECTION_DOESNT_SET : 6,

    CONNECTION_ALREADY_SET : 7,

    //Ощибка выполнения запроса
    REQUEST_ERROR : 8,
    SYNTAX_ERROR : 9,

    // ошибка создания стейтмента
    CREATE_STATEMENT_ERROR : 10,

    // ошибка недопустимого значения переменной или его отсутствия
    CMD_ARG_ERROR : 11,
    // ошибка во время закрытия соединения
    CLOSE_CONNECTION_ERROR : 12,

    DATABASE_ALREADY_EXISTS_ERROR : 16,
    DATABASE_DOESNT_EXISTS_ERROR : 17,
    DATABASE_IN_USE_DROP_ERROR : 18,

    TABLE_ALREADY_EXISTS_ERROR : 20,
    TABLE_DOESNT_EXISTS_ERROR : 21
};

function processRequestError(errCode, $container) {

}