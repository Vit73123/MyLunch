package ru.javaprojects.mylunch.restaurant.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.restaurant.to.RestaurantDailyMenuTo;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @GetMapping
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(ClockHolder.getDate());
    }

    @GetMapping("/menus")
    public List<RestaurantDailyMenuTo> getWithMenuOnToday() {
        return super.getWithMenusOnDate(ClockHolder.getDate());
    }
}
