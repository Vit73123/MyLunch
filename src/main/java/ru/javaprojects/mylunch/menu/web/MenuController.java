package ru.javaprojects.mylunch.menu.web;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController extends AbstractMenuController {

    protected final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/restaurants/{restaurantId}/menus";

    @Override
    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id, @PathVariable int restaurantId) {
        return super.get(id, restaurantId);
    }
}
