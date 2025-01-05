package ru.javaprojects.mylunch.menu.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.menu.model.Item;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.mylunch.dish.DishTestData.DISH5_ID;
import static ru.javaprojects.mylunch.menu.MenuTestData.*;

public class ItemRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    ItemRepository repository;

    @Test
    void save() {
        Item created = repository.save(getNewItem());
        int newId = created.id();
        Item newItem = getNewItem();
        newItem.setId(newId);
        newItem.setMenuId(newItem.getMenuId());
        newItem.setDishId(newItem.getDishId());
        ITEM_MATCHER.assertMatch(created, newItem);
        ITEM_MATCHER.assertMatch(repository.getExisted(newId), newItem);
    }

    @Test
    void saveDuplicated() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.save(new Item(null, MENU7_ID, DISH5_ID)));
    }

    @Test
    void delete() {
        repository.deleteExisted(ITEM12_ID);
        assertFalse(repository.findById(ITEM12_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExisted(NOT_FOUND));
    }

    @Test
    void getByMenu() {
        ITEM_MATCHER.assertMatch(repository.getByMenu(MENU7_ID), menu7Items);
    }

    @Test
    void getByNotFoundMenu() {
        assertTrue(repository.getByMenu(NOT_FOUND).isEmpty());
    }
}
