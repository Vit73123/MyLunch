package ru.javaprojects.mylunch.common.error;

import static ru.javaprojects.mylunch.common.error.ErrorType.APP_ERROR;

public class ApplicationErrorException extends AppException {
    public ApplicationErrorException(String msg) {
        super(msg, APP_ERROR);
    }
}