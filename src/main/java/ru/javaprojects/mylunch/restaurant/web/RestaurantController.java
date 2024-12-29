package ru.javaprojects.mylunch.restaurant.web;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractRestaurantController {
    protected final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/restaurants";

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return super.get(id);
    }
}
