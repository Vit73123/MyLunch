package ru.javaprojects.mylunch.menu.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.mylunch.meal.MealTestData.MEAL_MATCHER;
import static ru.javaprojects.mylunch.menu.MenuTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.NOT_FOUND;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

class MenuRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    MenuRepository repository;

    @Test
    void get() {
        Menu actual = repository.getExisted(MENU7_ID, RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(actual, menu7);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.getExisted(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.getExisted(MENU7_ID, RESTAURANT2_ID));
    }

    @Test
    void getOwnNotFoundRestaurant() {
        assertThrows(NotFoundException.class, () ->
                repository.getExisted(MENU7_ID, RestaurantTestData.NOT_FOUND));
    }

    @Test
    void save() {
        Menu created = repository.save(getNewMenuOnDate(TODAY));
        int newId = created.id();
        Menu newItem = getNewMenuOnDate(TODAY);
        newItem.setId(newId);
        MENU_MATCHER.assertMatch(created, newItem);
        MENU_MATCHER.assertMatch(repository.getExisted(newId, RESTAURANT3_ID), newItem);
    }

    @Test
    void duplicateDateRestaurantSave() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.save(new Menu(null, TODAY, RESTAURANT2_ID)));
    }

    @Test
    void delete() {
        repository.deleteExisted(MENU7_ID, RESTAURANT1_ID);
        assertFalse(repository.findById(MENU7_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExisted(NOT_FOUND, RESTAURANT1_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExisted(MENU1_ID, RESTAURANT2_ID));
    }

    @Test
    void getByRestaurant() {
        MENU_MATCHER.assertMatch(repository.getByRestaurant(RESTAURANT1_ID), restaurant1menus);
    }

    @Test
    void getByNotFoundRestaurant() {
        assertTrue(repository.getByRestaurant(RestaurantTestData.NOT_FOUND).isEmpty());
    }


    @Test
    void getByDateAndRestaurant() {
        MENU_MATCHER.assertMatch(repository.getExistedByDate(TODAY, RESTAURANT1_ID), menu7);
    }

    @Test
    void getByNotFoundDateOrRestaurant() {
        assertThrows(NotFoundException.class, () -> repository.getExistedByDate(NEW_DAY, RESTAURANT1_ID));
        assertThrows(NotFoundException.class, () -> repository.getExistedByDate(TODAY, RESTAURANT3_ID));
    }

    @Test
    void getWithItems() {
        Menu actual = repository.getExistedWithMeals(MENU8_ID, RESTAURANT2_ID);
        MENU_MATCHER.assertMatch(actual, menu8);
        MEAL_MATCHER.assertMatch(actual.getItems(), menu8Meals);
    }

    @Test
    void getByDate() {
        List<Menu> menus = repository.getByDate(DAY_1);
        System.out.println(menus);
    }
}