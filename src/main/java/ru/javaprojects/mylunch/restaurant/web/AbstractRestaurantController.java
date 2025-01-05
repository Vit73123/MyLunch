package ru.javaprojects.mylunch.restaurant.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaprojects.mylunch.menu.repository.MenuRepository;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;
import ru.javaprojects.mylunch.restaurant.to.RestaurantDailyMenuTo;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.createTos;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.createWithDailyMenuTos;

public abstract class AbstractRestaurantController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    private UniqueRestaurantMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public List<RestaurantTo> getOnDate(LocalDate date) {
        log.info("getOnDate {}", date);
        return createTos(restaurantRepository.getOnDate(date));
    }

    @Transactional
    public List<RestaurantDailyMenuTo> getWithMenusOnDate(LocalDate date) {
        log.info("getWithMenusOnDate {}", date);
        return createWithDailyMenuTos(menuRepository.getWithItemsAndRestaurantsByDate(date));
    }
}
