package ru.javaprojects.mylunch.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.menu.MenuTestData.MENU_ITEMS_TO_MATCHER;
import static ru.javaprojects.mylunch.menu.MenuTestData.menu8;
import static ru.javaprojects.mylunch.menu.MenusUtil.createWithItemsTo;
import static ru.javaprojects.mylunch.menu.web.MenuController.REST_URL;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;

public class MenuControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getOnToday() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(RESTAURANT2_ID)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEMS_TO_MATCHER.contentJson(createWithItemsTo(menu8)));
    }
}

