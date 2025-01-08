package ru.javaprojects.mylunch.restaurant.web;

import jakarta.validation.Valid;
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
public class AdminRestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get with id={}", id);
        return createTo(restaurantRepository.getExisted(id));
    }

    @GetMapping("/by-email")
    public RestaurantTo getByEmail(@RequestParam("email") String email) {
        log.info("getByEmail {}", email);
        return createTo(restaurantRepository.getExistedByEmail(email));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return createTos(restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email")));
    }

    @GetMapping("/on-today")
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(ClockHolder.getDate());
    }

    @GetMapping("/on-date")
    public List<RestaurantTo> getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getOnDate(date);
    }

    @GetMapping("/menus/on-date")
    public List<RestaurantMenuTo> getWithMenusOnToday(LocalDate date) {
        return super.getWithMenusOnDate(date);
    }

    @GetMapping("/menus/on-today")
    public List<RestaurantMenuTo> getWithMenusOnToday() {
        return super.getWithMenusOnDate(ClockHolder.getDate());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update {} with id={}", restaurantTo, id);
        assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurantRepository.save(updateFromTo(restaurant, restaurantTo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        restaurantRepository.deleteExisted(id);
    }
}
