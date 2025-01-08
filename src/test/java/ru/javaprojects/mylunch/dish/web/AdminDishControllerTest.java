package ru.javaprojects.mylunch.dish.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.dish.DishesUtil;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.repository.DishRepository;
import ru.javaprojects.mylunch.dish.to.DishTo;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.common.util.JsonUtil.writeValue;
import static ru.javaprojects.mylunch.dish.DishTestData.*;
import static ru.javaprojects.mylunch.dish.DishesUtil.createTo;
import static ru.javaprojects.mylunch.dish.DishesUtil.createTos;
import static ru.javaprojects.mylunch.dish.web.AdminDishController.REST_URL;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT3_ID;
import static ru.javaprojects.mylunch.user.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(createTo(dish1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotOwn() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOwnNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

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
                .andExpect(DISH_TO_MATCHER.contentJson(createTos(restaurant1dishes)));
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

    @Test
    void getUnAuth() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT3_ID)
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
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        DishTo newTo = new DishTo(null, "Созданный обед", 100);
        Dish newDish = DishesUtil.createNewFromTo(newTo, RESTAURANT3_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        DishTo created = DISH_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        newDish.setRestaurantId(RESTAURANT3_ID);
        DISH_TO_MATCHER.assertMatch(created, createTo(newDish));
        DISH_MATCHER.assertMatch(repository.getExistedByRestaurant(newId, RESTAURANT3_ID), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        DishTo newTo = new DishTo(null, "Созданный обед", 100);
        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        DishTo newTo = new DishTo(null, "", 100);
        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        newTo = new DishTo(null, "Invalid dish", 0);
        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        DishTo updatedTo = new DishTo(NOT_USED, "Изменённый обед", 141);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(repository.getExistedByRestaurant(updatedTo.id(), RESTAURANT1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        DishTo notFoundTo = new DishTo(NOT_FOUND, "Несуществующий обед", 100);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notFoundTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFoundRestaurant() throws Exception {
        DishTo notFoundTo = new DishTo(DISH1_ID, "Обед несуществующего ресторана", 100);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notFoundTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotOwnRestaurant() throws Exception {
        DishTo notFoundTo = new DishTo(DISH1_ID, "Обед чужого ресторана", 100);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notFoundTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateUsed() throws Exception {
        DishTo usedTo = new DishTo(DISH1_ID, "Обед, используемый в меню", 100);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(usedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        DishTo invalidTo = new DishTo(NOT_USED, "", 100);
        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        invalidTo = new DishTo(NOT_USED, "Invalid dish", 0);
        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        DishTo unsafeTo = new DishTo(NOT_USED, "<script>alert(123)</script>", 101);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(unsafeTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicated() throws Exception {
        DishTo duplicatedTo = new DishTo(NOT_USED, DISH8_DESCRIPTION, 101);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(duplicatedTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(NOT_USED).isPresent());
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
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(repository.findById(DISH1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteOwnNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_USED)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(repository.findById(DISH1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteUsed() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + DISH1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        assertTrue(repository.findById(DISH1_ID).isPresent());
    }
}

