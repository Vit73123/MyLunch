package ru.javaprojects.mylunch.common.validation;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.error.IllegalRequestDataException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkTimeLimit(LocalTime time, LocalTime timeLimit) {
        if (time.isAfter(timeLimit)) {
            throw new IllegalRequestDataException("Voting is not allowed after " + timeLimit + ". Now "
                    + time.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }
}