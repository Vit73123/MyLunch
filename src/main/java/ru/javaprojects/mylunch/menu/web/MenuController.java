package ru.javaprojects.mylunch.menu.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.menu.to.MenuItemsTo;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu API")
public class MenuController extends AbstractMenuController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    @GetMapping
    @Operation(summary = "Get the menu of the restaurant, and dishes in it",
            description = "Menu must belong to the restaurant if exists, or return null.")
    public MenuItemsTo getOnToday(@PathVariable int restaurantId) {
        return super.getOnDate(ClockHolder.getDate(), restaurantId);
    }
}
