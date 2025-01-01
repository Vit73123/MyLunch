package ru.javaprojects.mylunch.vote.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.vote.repository.VoteRepository;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.user.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.mylunch.user.UserTestData.USER_MAIL;
import static ru.javaprojects.mylunch.vote.VoteTestData.*;
import static ru.javaprojects.mylunch.vote.VotesUtil.createTos;
import static ru.javaprojects.mylunch.vote.web.AdminVoteController.REST_URL;

public class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    VoteRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOnToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "on-today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(todayVotes)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOnDate() throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL_SLASH + "on-date")
                .queryParam("date", "{date}")
                .buildAndExpand(TODAY)
                .toUri();

        perform(MockMvcRequestBuilders.get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(todayVotes)));
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
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
