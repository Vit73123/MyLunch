package ru.javaprojects.mylunch.restaurant.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import static org.slf4j.LoggerFactory.getLogger;

public class AbstractRestaurantController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    RestaurantRepository repository;

    @Autowired
    private UniqueRestaurantMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public Restaurant get(int id) {
        log.info("get restaurant {}", id);
        return repository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
