package ru.javaprojects.mylunch.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.meal.MealTestData;
import ru.javaprojects.mylunch.menu.ItemsUtil;
import ru.javaprojects.mylunch.menu.MenuTestData;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.repository.ItemRepository;
import ru.javaprojects.mylunch.menu.repository.MenuRepository;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.meal.MealTestData.MEAL3_ID;
import static ru.javaprojects.mylunch.meal.MealTestData.MEAL5_ID;
import static ru.javaprojects.mylunch.menu.MenuTestData.NOT_FOUND;
import static ru.javaprojects.mylunch.menu.MenuTestData.*;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTo;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTos;
import static ru.javaprojects.mylunch.menu.web.AdminMenuController.REST_URL;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;
import static ru.javaprojects.mylunch.user.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ItemRepository itemRepository;

    // Get menu

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU8_ID)
                .buildAndExpand(RESTAURANT2_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTo(menu8)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotOwn() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU1_ID)
                .buildAndExpand(RESTAURANT2_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOwnNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU7_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // Get menus by date

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOnDate() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "/on-date")
                .queryParam("date", "{date}")
                .buildAndExpand(RESTAURANT3_ID, DAY_1)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTo(menu1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOnToday() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "/on-today")
                .buildAndExpand(RESTAURANT2_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTo(menu8)));
    }

    // Get menus by restaurant

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(createTos(restaurant1menus)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // Authenticated / forbidden

    @Test
    void getUnAuth() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isForbidden());
    }

    // Create menu

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocationOnDate() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("date", "{date}")
                .buildAndExpand(RESTAURANT3_ID, NEW_DAY)
                .toUri();

        Menu newMenu = getNewMenuOnDate(NEW_DAY);
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        newMenu.setRestaurantId(created.getRestaurantId());
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId, RESTAURANT3_ID), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocationOnToday() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        Menu newMenu = getNewMenuOnDate(TODAY);
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        newMenu.setRestaurantId(created.getRestaurantId());
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId, RESTAURANT3_ID), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("date", "{date}")
                .buildAndExpand(RestaurantTestData.NOT_FOUND, NEW_DAY)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicated() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("date", "{date}")
                .buildAndExpand(RESTAURANT1_ID, DAY_1)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    // Delete menu

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU7_ID)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(menuRepository.findById(MENU7_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotOwn() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU7_ID)
                .buildAndExpand(RESTAURANT2_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(menuRepository.findById(MENU7_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteOwnNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MENU7_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(menuRepository.findById(MENU7_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getItems() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .buildAndExpand(RESTAURANT1_ID, MENU7_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ITEM_TO_MATCHER.contentJson(ItemsUtil.createTos(menu7Items)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getItemsNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .buildAndExpand(RestaurantTestData.NOT_FOUND, MENU7_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getItemsNotOwnRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .buildAndExpand(RESTAURANT2_ID, MENU7_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getItemsNotFoundMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .buildAndExpand(RESTAURANT1_ID, NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getItemsNotOwnMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .buildAndExpand(RESTAURANT1_ID, MENU8_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // Add item

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItem() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT2_ID, MENU8_ID, MEAL3_ID)
                .toUri();

        Item newItem = getNewItem();
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Item added = ITEM_MATCHER.readFromJson(action);
        int newId = added.id();
        newItem.setId(newId);
        ITEM_MATCHER.assertMatch(added, newItem);
        ITEM_MATCHER.assertMatch(itemRepository.getExisted(newId), newItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RestaurantTestData.NOT_FOUND, MENU8_ID, MEAL3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotOwnRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT1_ID, MENU8_ID, MEAL3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotFoundMeal() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT2_ID, MENU8_ID, MealTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotOwnMeal() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT2_ID, MENU8_ID, MEAL5_ID)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotFoundMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT2_ID, MenuTestData.NOT_FOUND, MEAL3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addItemNotOwnMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("mealId", "{mealId}")
                .buildAndExpand(RESTAURANT2_ID, MENU7_ID, MEAL3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // Delete item

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteItem() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("itemId", "{itemId}")
                .buildAndExpand(RESTAURANT1_ID, MENU7_ID, ITEM12_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(itemRepository.findById(ITEM12_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteItemNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("itemId", "{itemId}")
                .buildAndExpand(NOT_FOUND, MENU7_ID, ITEM12_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(itemRepository.findById(ITEM12_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteItemNotOwnRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("itemId", "{itemId}")
                .buildAndExpand(RESTAURANT2_ID, MENU7_ID, ITEM12_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(itemRepository.findById(ITEM12_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteItemNotMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("itemId", "{itemId}")
                .buildAndExpand(RESTAURANT1_ID, NOT_FOUND, ITEM12_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(itemRepository.findById(ITEM12_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteItemNotOwnMenu() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "{menuId}" + "/items")
                .queryParam("itemId", "{itemId}")
                .buildAndExpand(RESTAURANT1_ID, MENU8_ID, ITEM12_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(itemRepository.findById(ITEM12_ID).isPresent());
    }
}

