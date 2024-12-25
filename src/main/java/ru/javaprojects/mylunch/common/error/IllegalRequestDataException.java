package ru.javaprojects.mylunch.common.error;

import static ru.javaprojects.mylunch.common.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}