package ru.javaprojects.mylunch.meal.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.meal.MealsUtil;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.meal.repository.MealRepository;
import ru.javaprojects.mylunch.restaurant.RestaurantTestData;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.common.util.JsonUtil.writeValue;
import static ru.javaprojects.mylunch.meal.MealTestData.*;
import static ru.javaprojects.mylunch.meal.MealsUtil.createTo;
import static ru.javaprojects.mylunch.meal.MealsUtil.createTos;
import static ru.javaprojects.mylunch.meal.web.AdminMealController.REST_URL;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT3_ID;
import static ru.javaprojects.mylunch.user.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class AdminMealControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MealRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();
        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(createTo(meal1)));
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
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
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
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
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
                .andExpect(MEAL_TO_MATCHER.contentJson(createTos(restaurant1meals)));
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
    void createWithLocation() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(newMeal))))
                .andDo(print())
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        newMeal.setRestaurantId(created.getRestaurantId());
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(repository.getExistedByRestaurantId(newId, RESTAURANT3_ID), newMeal);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(newMeal))))
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

        Meal invalidNew;
        invalidNew = new Meal(null, "", 100, 0);
        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(invalidNew))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        invalidNew = new Meal(null, "Invalid meal", 0, 0);
        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(invalidNew))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Meal updated = getUpdated();

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(repository.getExistedByRestaurantId(MEAL1_ID, RESTAURANT3_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        Meal notFound = new Meal(NOT_FOUND, "Несуществующий обед", 100, RESTAURANT3_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notFound)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFoundRestaurant() throws Exception {
        Meal notFound = new Meal(MEAL1_ID, "Обед несуществующего ресторана", 100, RestaurantTestData.NOT_FOUND);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(notFound)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Meal invalid;

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        invalid = new Meal(meal1);
        invalid.setDescription("");
        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        invalid = new Meal(meal1);
        invalid.setPrice(0);
        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Meal updated = new Meal(meal1);
        updated.setDescription("<script>alert(123)</script>");

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(updated))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicated() throws Exception {
        Meal updated = new Meal(meal1);
        updated.setDescription(MEAL6_DESCRIPTION);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MealsUtil.createTo(updated))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(MEAL1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + NOT_FOUND)
                .buildAndExpand(RESTAURANT3_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotOwn() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RESTAURANT1_ID)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(repository.findById(MEAL1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteOwnNotFoundRestaurant() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + MEAL1_ID)
                .buildAndExpand(RestaurantTestData.NOT_FOUND)
                .toUri();

        perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertTrue(repository.findById(MEAL1_ID).isPresent());
    }
}

