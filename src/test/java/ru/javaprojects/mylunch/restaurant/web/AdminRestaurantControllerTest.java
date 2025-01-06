package ru.javaprojects.mylunch.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.menu.MenuTestData;
import ru.javaprojects.mylunch.restaurant.RestaurantsUtil;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.common.util.JsonUtil.writeValue;
import static ru.javaprojects.mylunch.menu.MenuTestData.DAY_1;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.createTo;
import static ru.javaprojects.mylunch.restaurant.RestaurantsUtil.createTos;
import static ru.javaprojects.mylunch.restaurant.web.AdminRestaurantController.REST_URL;
import static ru.javaprojects.mylunch.restaurant.web.UniqueRestaurantMailValidator.EXCEPTION_DUPLICATE_EMAIL;
import static ru.javaprojects.mylunch.user.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-email?email=" + restaurant1.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTos(restaurants)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOnDate() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "on-date")
                .queryParam("date", "{date}")
                .buildAndExpand(DAY_1)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantsUtil.createTos(day1Restaurants)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithMenuOnDate() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "menus/on-date")
                .queryParam("date", "{date}")
                .buildAndExpand(DAY_1)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MENU_TO_MATCHER.contentJson(
                        RestaurantsUtil.createWithDailyMenuTos(MenuTestData.day1Menus)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWithMenuOnToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "menus/on-today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MENU_TO_MATCHER.contentJson(
                        RestaurantsUtil.createWithDailyMenuTos(MenuTestData.todayMenus)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        RestaurantTo newTo = new RestaurantTo(null, "Новый ресторан", "new_restaurant@yandex.ru");
        Restaurant newRestaurant = RestaurantsUtil.createNewFromTo(newTo);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        RestaurantTo created = RESTAURANT_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_TO_MATCHER.assertMatch(created, createTo(newRestaurant));
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        RestaurantTo invalidTo = new RestaurantTo(null, "", "invalid_restaurant@yandex.ru");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        RestaurantTo updatedTo = new RestaurantTo(RESTAURANT1_ID, "Изменённый ресторан", "updated_restaurant@yandex.ru");

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(repository.getExisted(RESTAURANT1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        Restaurant notFound = new Restaurant(NOT_FOUND, "Несуществующий ресторан", "not_found_restaurant@yandex.ru");

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(createTo(notFound))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(restaurant1);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        RestaurantTo unsafeTo = new RestaurantTo(RESTAURANT1_ID, "<script>alert(123)</script>", "restaurant_b@yandex.ru");

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(unsafeTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicated() throws Exception {
        RestaurantTo duplicatedTo = new RestaurantTo(RESTAURANT2_ID, "Ресторан А", RESTAURANT1_EMAIL);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(duplicatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
