package ru.javaprojects.mylunch.menu.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.repository.DishRepository;
import ru.javaprojects.mylunch.menu.ItemsUtil;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.repository.ItemRepository;
import ru.javaprojects.mylunch.menu.to.CreateItemTo;
import ru.javaprojects.mylunch.menu.to.ItemTo;
import ru.javaprojects.mylunch.menu.to.MenuItemsTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.menu.MenusUtil.*;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "menus")
@Tag(name = "Menu administrator API")
public class AdminMenuController extends AbstractMenuController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get the menu of the restaurant, and dishes in it, by menu id",
            description = "Menu must belong to the restaurant. Restaurant and menu must exist.")
    public MenuItemsTo get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get with id={} of restaurant id={}", id, restaurantId);
        return createWithItemsTo(menuRepository.getExisted(id, restaurantId));
    }

    @GetMapping("/on-today")
    @Operation(summary = "Get the menu of the restaurant, and dishes in it, on today",
            description = "Menu must belong to the restaurant if exists, or return null. Restaurant must exist.")
    public MenuItemsTo getOnToday(@PathVariable int restaurantId) {
        return super.getOnDate(ClockHolder.getDate(), restaurantId);
    }

    @Override
    @GetMapping("/on-date")
    @Operation(summary = "Get the menu of the restaurant, and dishes in it, on specified date",
            description = "Menu must belong to the restaurant if exists, or return null. Restaurant must exist.")
    public MenuItemsTo getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @PathVariable int restaurantId) {
        return super.getOnDate(date, restaurantId);
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get all menus of the restaurant, and dishes in each of them",
            description = "Menu must belong to the restaurant if exists, or return null. Restaurant must exist.")
    public List<MenuItemsTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createWithItemsTos(menuRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "Create new menu of the restaurant",
            description = "Just specify the date. Menu of the restaurant on the date must not exist. Restaurant must exist.")
    public ResponseEntity<MenuItemsTo> create(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create {} for restaurant id={}", menuTo, restaurantId);
        checkNew(menuTo);
        restaurantRepository.checkExists(restaurantId);
        Menu created = menuRepository.prepareAndSave(createNewFromTo(menuTo), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurantId)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(createWithItemsTo(created));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Delete the menu of the restaurant by menu id",
            description = "Menu must belong to the restaurant. Restaurant and menu must exist.")
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete with id={} of restaurant id={}", id, restaurantId);
        menuRepository.deleteExisted(id, restaurantId);
    }

    @GetMapping("/{menuId}/items")
    @Transactional(readOnly = true)
    @Operation(summary = "Get the dishes in menu of the restaurant by menu id",
            description = "Menu must belong to the restaurant. Restaurant and menu must exist.")
    public List<ItemTo> getItems(@PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("get items for menu id={} restaurant id={}", menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        return ItemsUtil.createMenuItemTos(itemRepository.getByMenu(menuId));
    }

    @PostMapping(value = "/{menuId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "Include the dish of the restaurant into the menu by menu id",
            description = "Just specify dish id. Menu must belong to the restaurant. Restaurant, dish and menu must exist.")
    public ResponseEntity<ItemTo> addItem(@Valid @RequestBody CreateItemTo itemTo,
                                          @PathVariable int menuId,
                                          @PathVariable int restaurantId) {
        log.info("add {} for menu id={} restaurant id={}", itemTo, menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        Dish dish = dishRepository.getExistedByRestaurant(itemTo.getDishId(), restaurantId);
        Item created = itemRepository.prepareAndSave(ItemsUtil.createNewFromTo(itemTo), menuId);
        created.setDish(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{menuId}/items/{itemsId}")
                .buildAndExpand(restaurantId, menuId, created.id())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(ItemsUtil.createMenuItemTo(created));
    }

    @DeleteMapping(value = "/{menuId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "Remove the dish from menu by its item id",
            description = "Menu must belong to the restaurant. Restaurant, item and menu must exist.")
    public void deleteItem(@PathVariable int itemId, @PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("delete item id={} for menu id={} restaurant id={}", itemId, menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        itemRepository.deleteExisted(itemId);
    }
}
