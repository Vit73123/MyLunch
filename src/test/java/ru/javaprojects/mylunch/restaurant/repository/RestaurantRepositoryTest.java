package ru.javaprojects.mylunch.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.menu.MenuTestData;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

class RestaurantRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    RestaurantRepository repository;

    @Test
    void get() {
        Restaurant actual = repository.getExisted(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.getExisted(NOT_FOUND));
    }

    @Test
    void save() {
        Restaurant created = repository.save(getNew());
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    void duplicateMailSave() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.save(new Restaurant(null, "Duplicate", RESTAURANT1_EMAIL)));
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        repository.save(updated);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(RESTAURANT1_ID), getUpdated());
    }

    @Test
    void delete() {
        repository.deleteExisted(RESTAURANT1_ID);
        assertFalse(repository.findById(RESTAURANT1_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExisted(NOT_FOUND));
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(
                repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email")), restaurants);
    }

    @Test
    void getByEmail() {
        RESTAURANT_MATCHER.assertMatch(repository.getExistedByEmail(RESTAURANT1_EMAIL), restaurant1);
    }

    @Test
    void getOnDate() {
        RESTAURANT_MATCHER.assertMatch(repository.getOnDate(MenuTestData.DAY_1), day1Restaurants);
    }

    @Test
    void getWithMenusOnDate() {
        RESTAURANT_MATCHER.assertMatch(repository.getWithMenusOnDate(MenuTestData.DAY_1), day1Restaurants);
    }
}