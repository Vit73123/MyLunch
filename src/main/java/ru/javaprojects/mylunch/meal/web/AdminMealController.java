package ru.javaprojects.mylunch.meal.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.meal.repository.MealRepository;
import ru.javaprojects.mylunch.meal.to.MealTo;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.meal.MealsUtil.*;

@RestController
@RequestMapping(value = AdminMealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMealController {
    protected final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/meals";

    @Autowired
    protected MealRepository mealRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public MealTo get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get meal id={} of restaurant id={}", id, restaurantId);
        return createTo(mealRepository.getExistedByRestaurantId(id, restaurantId));
    }

    @GetMapping
    @Transactional
    public List<MealTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("get meals of restaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createTos(mealRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Meal> createWithLocation(@Valid @RequestBody MealTo mealTo, @PathVariable int restaurantId) {
        log.info("create {} of restaurant id={}", mealTo, restaurantId);
        checkNew(mealTo);
        restaurantRepository.checkExists(restaurantId);
        Meal created = mealRepository.prepareAndSave(createNewFromTo(mealTo), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurantId)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody MealTo mealTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} with id={} of restaurant id={}", mealTo, id, restaurantId);
        assureIdConsistent(mealTo, id);
        Meal meal = mealRepository.getExistedByRestaurantId(id, restaurantId);
        mealRepository.prepareAndSave(updateFromTo(meal, mealTo), restaurantId);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete meal with id={} of restaurant id={}", id, restaurantId);
        mealRepository.deleteExistedByRestaurantId(id, restaurantId);
    }

}
