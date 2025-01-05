package ru.javaprojects.mylunch.dish.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.dish.to.DishTo;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.repository.DishRepository;
import ru.javaprojects.mylunch.menu.repository.ItemRepository;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.dish.DishesUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    private final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get with id={} of restaurant id={}", id, restaurantId);
        return createTo(dishRepository.getExistedByRestaurantId(id, restaurantId));
    }

    @GetMapping
    @Transactional
    public List<DishTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createTos(dishRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create {} of restaurant id={}", dishTo, restaurantId);
        checkNew(dishTo);
        restaurantRepository.checkExists(restaurantId);
        Dish created = dishRepository.prepareAndSave(createNewFromTo(dishTo), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurantId)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} with id={} of restaurant id={}", dishTo, id, restaurantId);
        assureIdConsistent(dishTo, id);
        Dish dish = dishRepository.getExistedByRestaurantId(id, restaurantId);
        itemRepository.checkDishNotExists(id);
        dishRepository.prepareAndSave(updateFromTo(dish, dishTo), restaurantId);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete with id={} of restaurant id={}", id, restaurantId);
        itemRepository.checkDishNotExists(id);
        dishRepository.deleteExistedByRestaurantId(id, restaurantId);
    }
}
