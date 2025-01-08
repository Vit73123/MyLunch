package ru.javaprojects.mylunch.restaurant.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.restaurant.to.RestaurantMenuTo;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant API")
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @GetMapping
    @Cacheable(value = "restaurants")
    @Operation(summary = "Get restaurants that have menu on today",
            description = "If there is no any restaurant on today, get empty list.")
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(ClockHolder.getDate());
    }

    @GetMapping("/menus")
    @Cacheable(value = "menus")
    @Operation(summary = "Get restaurants that have menu along with the menu of each restaurant on today",
            description = "If there is no any restaurant on today, get empty list.")
    public List<RestaurantMenuTo> getWithMenuOnToday() {
        return super.getWithMenusOnDate(ClockHolder.getDate());
    }
}
