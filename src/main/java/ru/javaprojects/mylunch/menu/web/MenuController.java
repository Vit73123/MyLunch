package ru.javaprojects.mylunch.menu.web;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.menu.to.MenuItemsTo;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "menus")
public class MenuController extends AbstractMenuController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    @GetMapping
    @Cacheable(key = "#restaurantId")
    public MenuItemsTo getOnToday(@PathVariable int restaurantId) {
        return super.getOnDate(ClockHolder.getDate(), restaurantId);
    }
}
