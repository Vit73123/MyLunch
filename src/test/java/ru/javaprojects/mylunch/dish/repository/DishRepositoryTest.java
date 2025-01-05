package ru.javaprojects.mylunch.dish.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.mylunch.dish.DishTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT3_ID;

class DishRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    DishRepository repository;

    @Test
    void get() {
        Dish actual = repository.getExistedByRestaurantId(DISH1_ID, RESTAURANT3_ID);
        DISH_MATCHER.assertMatch(actual, dish1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(NOT_FOUND, RESTAURANT3_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    void getOwnNotFoundRestaurant() {
        assertThrows(NotFoundException.class, () ->
                repository.getExistedByRestaurantId(DISH1_ID, RestaurantTestData.NOT_FOUND));
    }

    @Test
    void save() {
        Dish created = repository.prepareAndSave(getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Dish newItem = getNew();
        newItem.setId(newId);
        newItem.setRestaurantId(created.getRestaurantId());
        DISH_MATCHER.assertMatch(created, newItem);
        DISH_MATCHER.assertMatch(repository.getExistedByRestaurantId(newId, RESTAURANT1_ID), newItem);
    }

    @Test
    void saveNotOwn() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.prepareAndSave(new Dish(null, "Обед несуществующего ресторана", 100, 0), RestaurantTestData.NOT_FOUND));
    }

    @Test
    void duplicateDescriptionRestaurantSave() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.prepareAndSave(new Dish(null, dish1.getDescription(), 1000, 0), RESTAURANT3_ID));
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        repository.prepareAndSave(updated, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(repository.getExistedByRestaurantId(NOT_USED, RESTAURANT1_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.prepareAndSave(new Dish(NOT_FOUND, "Несуществующий обед", 1000, 0), RESTAURANT3_ID));
    }

    @Test
    void updateNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.prepareAndSave(new Dish(DISH1_ID, "Обед несуществующего ресторана", 1000, 0), RestaurantTestData.NOT_FOUND));
    }

    @Test
    void delete() {
        repository.deleteExistedByRestaurantId(DISH1_ID, RESTAURANT3_ID);
        assertFalse(repository.findById(DISH1_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExistedByRestaurantId(NOT_FOUND, RESTAURANT3_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExistedByRestaurantId(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    void getByRestaurant() {
        DISH_MATCHER.assertMatch(repository.getByRestaurant(RESTAURANT1_ID), restaurant1dishes);
    }

    @Test
    void getByNotFoundRestaurant() {
        assertTrue(repository.getByRestaurant(RestaurantTestData.NOT_FOUND).isEmpty());
    }
}