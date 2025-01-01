package ru.javaprojects.mylunch.restaurant.web;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaprojects.mylunch.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get with id={}", id);
        return repository.getExisted(id);
    }

    @GetMapping("/by-email")
    public Restaurant getByEmail(@RequestParam("email") String email) {
        log.info("getByEmail {}", email);
        return repository.getExistedByEmail(email);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @GetMapping("/on-today")
    public List<RestaurantTo> getOnToday() {
        return super.getOnDate(LocalDate.now());
    }

    @GetMapping("/on-date")
    public List<RestaurantTo> getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getOnDate(date);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.checkAndSave(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        repository.checkAndSave(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        repository.deleteExisted(id);
    }
}
