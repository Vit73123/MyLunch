package ru.javaprojects.mylunch.common.error;

import static ru.javaprojects.mylunch.common.error.ErrorType.NOT_FOUND;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg, NOT_FOUND);
    }
}