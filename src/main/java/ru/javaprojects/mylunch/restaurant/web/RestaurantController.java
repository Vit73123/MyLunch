package ru.javaprojects.mylunch.restaurant.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/api/restaurants";

    @GetMapping
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(LocalDate.now());
    }
}
