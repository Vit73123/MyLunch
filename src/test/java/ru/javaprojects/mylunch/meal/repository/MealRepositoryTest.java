package ru.javaprojects.mylunch.meal.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.mylunch.meal.MealTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT3_ID;

class MealRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    MealRepository repository;

    @Test
    void get() {
        Meal actual = repository.getExistedByRestaurantId(MEAL1_ID, RESTAURANT3_ID);
        MEAL_MATCHER.assertMatch(actual, meal1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(NOT_FOUND, RESTAURANT3_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(MEAL1_ID, RESTAURANT1_ID));
    }

    @Test
    void getOwnNotFoundRestaurant() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(MEAL1_ID, RestaurantTestData.NOT_FOUND));
    }

    @Test
    void save() {
        Meal created = repository.prepareAndSave(getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Meal newItem = getNew();
        newItem.setId(newId);
        newItem.setRestaurantId(created.getRestaurantId());
        MEAL_MATCHER.assertMatch(created, newItem);
        MEAL_MATCHER.assertMatch(repository.getExistedByRestaurantId(newId, RESTAURANT1_ID), newItem);
    }

    @Test
    void saveNotOwn() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.prepareAndSave(new Meal(null, "Обед несуществующего ресторана", 100, 0), RestaurantTestData.NOT_FOUND));
    }

    @Test
    void duplicateDescriptionRestaurantSave() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.prepareAndSave(new Meal(null, meal1.getDescription(), 1000, 0), RESTAURANT3_ID));
    }

    @Test
    void update() {
        Meal updated = getUpdated();
        repository.prepareAndSave(updated, RESTAURANT3_ID);
        MEAL_MATCHER.assertMatch(repository.getExistedByRestaurantId(MEAL1_ID, RESTAURANT3_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.prepareAndSave(new Meal(NOT_FOUND, "Несуществующий обед", 1000, 0), RESTAURANT3_ID));
    }

    @Test
    void updateNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.prepareAndSave(new Meal(MEAL1_ID, "Обед несуществующего ресторана", 1000, 0), RestaurantTestData.NOT_FOUND));
    }

    @Test
    void delete() {
        repository.deleteExistedByRestaurantId(MEAL1_ID, RESTAURANT3_ID);
        assertFalse(repository.findById(MEAL1_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExistedByRestaurantId(NOT_FOUND, RESTAURANT3_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExistedByRestaurantId(MEAL1_ID, RESTAURANT1_ID));
    }

    @Test
    void getByRestaurant() {
        MEAL_MATCHER.assertMatch(repository.getByRestaurant(RESTAURANT1_ID), restaurant1meals);
    }

    @Test
    void getByNotFoundRestaurant() {
        assertTrue(repository.getByRestaurant(RestaurantTestData.NOT_FOUND).isEmpty());
    }
}