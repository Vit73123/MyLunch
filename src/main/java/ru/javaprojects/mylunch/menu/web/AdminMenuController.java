package ru.javaprojects.mylunch.menu.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.meal.repository.MealRepository;
import ru.javaprojects.mylunch.menu.ItemsUtil;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.repository.ItemRepository;
import ru.javaprojects.mylunch.menu.to.ItemTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import javax.annotation.Nullable;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTo;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTos;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends AbstractMenuController {
    protected final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected MealRepository mealRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get with id={} of restaurant id={}", id, restaurantId);
        return createTo(menuRepository.getExisted(id, restaurantId));
    }

    @GetMapping("/on-today")
    public MenuTo getOnToday(@PathVariable int restaurantId) {
        return super.getOnDate(LocalDate.now(), restaurantId);
    }

    @Override
    @GetMapping("/on-date")
    public MenuTo getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            @PathVariable int restaurantId) {
        return super.getOnDate(date, restaurantId);
    }

    @GetMapping
    @Transactional
    public List<MenuTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createTos(menuRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Menu> createWithLocation(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable int restaurantId) {
        log.info("create on date {} of restaurant id={}", date, restaurantId);
        restaurantRepository.checkExists(restaurantId);
        Menu newMenu = menuRepository.findByDateAndRestaurantId(date, restaurantId)
                .orElse(new Menu(null, date != null ? date : LocalDate.now(), 0));
        checkNew(newMenu);
        Menu created = menuRepository.prepareAndSave(newMenu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurantId)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete with id={} of restaurant id={}", id, restaurantId);
        menuRepository.deleteExisted(id, restaurantId);
    }

    @GetMapping("/{menuId}/items")
    @Transactional
    public List<ItemTo> getItems(@PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("get items for menu id={} restaurant id={}", menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        return ItemsUtil.createTos(itemRepository.getByMenu(menuId));
    }

    @PostMapping(value = "/{menuId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Item addItem(@RequestParam int mealId, @PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("add item from meal id={} for menu id={} restaurant id={}", mealId, menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        mealRepository.checkExistsByRestaurant(mealId, restaurantId);
        Item newItem = itemRepository.findByMenuIdAndMealId(menuId, mealId)
                .orElse(new Item(null, menuId, mealId));
        checkNew(newItem);
        return itemRepository.save(newItem);
    }

    @DeleteMapping(value = "/{menuId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteItem(@RequestParam int itemId, @PathVariable int menuId, @PathVariable int restaurantId) {
        log.info("delete item id={} for menu id={} restaurant id={}", itemId, menuId, restaurantId);
        menuRepository.checkExistsByRestaurant(menuId, restaurantId);
        itemRepository.deleteExisted(itemId);
    }
}
