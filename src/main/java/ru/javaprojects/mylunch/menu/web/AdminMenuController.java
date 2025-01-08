package ru.javaprojects.mylunch.menu.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public MenuItemsTo get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get with id={} of restaurant id={}", id, restaurantId);
        return createWithItemsTo(menuRepository.getExisted(id, restaurantId));
    }

    @GetMapping("/on-today")
    public MenuItemsTo getOnToday(@PathVariable int restaurantId) {
        return super.getOnDate(ClockHolder.getDate(), restaurantId);
    }

    @Override
    @GetMapping("/on-date")
    public MenuItemsTo getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @PathVariable int restaurantId) {
        return super.getOnDate(date, restaurantId);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<MenuItemsTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createWithItemsTos(menuRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
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
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete with id={} of restaurant id={}", id, restaurantId);
        menuRepository.deleteExisted(id, restaurantId);
    }

    @GetMapping("/{menuId}/items")
    @Transactional(readOnly = true)
    public List<ItemTo> getItems(@PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("get items for menu id={} restaurant id={}", menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        return ItemsUtil.createMenuItemTos(itemRepository.getByMenu(menuId));
    }

    @PostMapping(value = "/{menuId}/items")
    @Transactional
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
    public void deleteItem(@PathVariable int itemId, @PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("delete item id={} for menu id={} restaurant id={}", itemId, menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        itemRepository.deleteExisted(itemId);
    }
}
