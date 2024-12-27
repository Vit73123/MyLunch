package ru.javaprojects.mylunch.menu.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaprojects.mylunch.menu.repository.MenuRepository;
import ru.javaprojects.mylunch.menu.to.MenuTo;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTo;

public abstract class AbstractMenuController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    public MenuTo get(int id, int restaurantId) {
        log.info("get with id={} of restaurant id={}", id, restaurantId);
        return createTo(menuRepository.getExistedByRestaurantId(id, restaurantId));
    }
}
