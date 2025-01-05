package ru.javaprojects.mylunch.menu.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.menu.repository.MenuRepository;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTo;

public abstract class AbstractMenuController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected MenuRepository menuRepository;

    @Transactional
    public MenuTo getOnDate(LocalDate date, int restaurantId) {
        log.info("getOnDate {} for restaurant id={}", date, restaurantId);
        return createTo(menuRepository.getExistedByDate(date, restaurantId));
    }
}
