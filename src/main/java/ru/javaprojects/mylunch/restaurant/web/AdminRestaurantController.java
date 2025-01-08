package ru.javaprojects.mylunch.restaurant.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.to.RestaurantMenuTo;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaprojects.mylunch.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.*;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant administrator API")
public class AdminRestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    @GetMapping("/{id}")
    @Operation(summary = "Get the restaurant by id",
            description = "Restaurant must exist.")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get with id={}", id);
        return createTo(restaurantRepository.getExisted(id));
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get the restaurant by email",
            description = "Email of the restaurant must exist.")
    public RestaurantTo getByEmail(@RequestParam("email") String email) {
        log.info("getByEmail {}", email);
        return createTo(restaurantRepository.getExistedByEmail(email));
    }

    @GetMapping
    @Operation(summary = "Get all restaurants that are in the database")
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return createTos(restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email")));
    }

    @GetMapping("/on-today")
    @Cacheable("restaurants")
    @Operation(summary = "Get restaurants that have menu on today",
            description = "If there is no any restaurant on the date, get empty list.")
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(ClockHolder.getDate());
    }

    @GetMapping("/on-date")
    @Operation(summary = "Get restaurants that have menu on specified date",
            description = "If there is no any restaurant on the date, get empty list.")
    public List<RestaurantTo> getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getOnDate(date);
    }

    @GetMapping("/menus/on-today")
    @Cacheable("menus")
    @Operation(summary = "Get restaurants that have menu along with the menu of each restaurant on today",
            description = "If there is no any restaurant on today, get empty list.")
    public List<RestaurantMenuTo> getWithMenusOnToday() {
        return super.getWithMenusOnDate(ClockHolder.getDate());
    }

    @GetMapping("/menus/on-date")
    @Operation(summary = "Get restaurants that have menu along with the menu of each restaurant on specified date",
            description = "If there is no any restaurant on the date, get empty list.")
    public List<RestaurantMenuTo> getWithMenusOnDate(LocalDate date) {
        return super.getWithMenusOnDate(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(cacheNames = {"restaurants", "menus"})
    @Operation(summary = "Create new restaurant with its name and email",
            description = "Email of the restaurant must not exist.")
    public ResponseEntity<RestaurantTo> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create {}", restaurantTo);
        checkNew(restaurantTo);
        RestaurantTo created = createTo(restaurantRepository.save(createNewFromTo(restaurantTo)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(cacheNames = {"restaurants", "menus"})
    @Operation(summary = "Update the restaurant, change its name or email",
            description = "Restaurant must exist, email of the restaurant must not exist.")
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update {} with id={}", restaurantTo, id);
        assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurantRepository.save(updateFromTo(restaurant, restaurantTo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = {"restaurants", "menus"})
    @Operation(summary = "Delete the restaurant by id",
            description = "Restaurant must exist.")
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        restaurantRepository.deleteExisted(id);
    }
}
