package ru.javaprojects.mylunch.vote.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javaprojects.mylunch.AbstractControllerTest;
import ru.javaprojects.mylunch.app.config.AppPropertiesConfig;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.repository.VoteRepository;

import java.net.URI;
import java.time.Clock;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.NOT_TODAY_RESTAURANT;
import static ru.javaprojects.mylunch.user.UserTestData.*;
import static ru.javaprojects.mylunch.vote.VoteTestData.getNew;
import static ru.javaprojects.mylunch.vote.VoteTestData.getUpdated;
import static ru.javaprojects.mylunch.vote.VoteTestData.*;
import static ru.javaprojects.mylunch.vote.VotesUtil.createTo;
import static ru.javaprojects.mylunch.vote.VotesUtil.createTos;
import static ru.javaprojects.mylunch.vote.web.VoteController.REST_URL;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';

    @Autowired
    VoteRepository repository;

    @Autowired
    AppPropertiesConfig app;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "on-today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(vote5)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(userVotes)));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getAllNewUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTos(noVotes)));
        assertTrue(repository.getByUser(GUEST_ID).isEmpty());
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void create() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        Vote newVote = getNew();

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("restaurantId", "{restaurantId}")
                .buildAndExpand(newVote.getRestaurantId())
                .toUri();

        ResultActions action = perform(MockMvcRequestBuilders.post(uri))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setVotedTime(created.getVotedTime());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void createAfterTimeLimit() throws Exception {
        Clock testClock = Clock.fixed(LATE.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        Vote newVote = getNew();
        newVote.setVotedTime(LATE_TIME);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("restaurantId", "{restaurantId}")
                .buildAndExpand(newVote.getRestaurantId())
                .toUri();

        ResultActions action = perform(MockMvcRequestBuilders.post(uri))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setVotedTime(created.getVotedTime());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithNotFoundRestaurant() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        Vote newVote = getNew();
        newVote.setRestaurantId(NOT_TODAY_RESTAURANT);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("restaurantId", "{restaurantId}")
                .buildAndExpand(newVote.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.post(uri))
                .andDo(print())
                .andExpect(status().isNotFound());

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        Vote updated = getUpdated();

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("restaurantId", "{restaurantId}")
                .buildAndExpand(updated.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.put(uri))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(repository.getExisted(updated.id()), updated);

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateAfterTimeLimit() throws Exception {
        Clock testClock = Clock.fixed(LATE.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        Vote updated = getUpdated();
        updated.setVotedTime(LATE_TIME);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .queryParam("restaurantId", "{restaurantId}")
                .buildAndExpand(updated.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.put(uri))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        ClockHolder.setClock(beforeTestClock);
    }
}
