package ru.javaprojects.mylunch.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaprojects.mylunch.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT_MATCHER;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.todayRestaurants;
import static ru.javaprojects.mylunch.restaurant.web.RestaurantController.REST_URL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(todayRestaurants));
    }
}
