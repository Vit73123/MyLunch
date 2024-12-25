package ru.javaprojects.mylunch.common.error;

import static ru.javaprojects.mylunch.common.error.ErrorType.DATA_CONFLICT;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg, DATA_CONFLICT);
    }
}