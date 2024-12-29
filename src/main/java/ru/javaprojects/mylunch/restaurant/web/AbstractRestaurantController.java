package ru.javaprojects.mylunch.restaurant.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.createTos;

public abstract class AbstractRestaurantController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    private UniqueRestaurantMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public Restaurant get(int id) {
        log.info("get with id={}", id);
        return repository.getExisted(id);
    }

    public List<RestaurantTo> getOnDate(LocalDate date) {
        log.info("getOnDate {}", date);
        return createTos(repository.getOnDate(date));
    }

    public void delete(int id) {
        log.info("delete with id={}", id);
        repository.deleteExisted(id);
    }
}
