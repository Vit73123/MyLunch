package ru.javaprojects.mylunch.vote.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import ru.javaprojects.mylunch.vote.to.VoteTo;
import ru.javaprojects.mylunch.vote.to.CreateVoteTo;

import java.net.URI;
import java.time.Clock;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.mylunch.common.util.JsonUtil.writeValue;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaprojects.mylunch.user.UserTestData.*;
import static ru.javaprojects.mylunch.vote.VoteTestData.getUpdated;
import static ru.javaprojects.mylunch.vote.VoteTestData.*;
import static ru.javaprojects.mylunch.vote.VotesUtil.*;
import static ru.javaprojects.mylunch.vote.web.VoteController.REST_URL;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';

    @Autowired
    VoteRepository repository;

    @Autowired
    AppPropertiesConfig app;

    @Autowired
    ApplicationContext ap;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "on-today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_DATE_TO_MATCHER.contentJson(createDateTo(vote5)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_DATE_TO_MATCHER.contentJson(createDateTos(userVotes)));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getAllNewUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_DATE_TO_MATCHER.contentJson(createDateTos(noVotes)));
        assertTrue(repository.getByUser(GUEST_ID).isEmpty());
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void create() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        CreateVoteTo newTo = new CreateVoteTo(null, RESTAURANT1_ID);
        Vote newVote = createNewFromTo(newTo, GUEST_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(newTo.getRestaurantId())
                .toUri();

        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        VoteTo created = VOTE_DATE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setVotedDate(created.getVotedDate());
        VOTE_DATE_TO_MATCHER.assertMatch(created, createDateTo(newVote));
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void createAfterTimeLimit() throws Exception {
        Clock testClock = Clock.fixed(LATE.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        CreateVoteTo newTo = new CreateVoteTo(null, RESTAURANT1_ID);
        Vote newVote = createNewFromTo(newTo, GUEST_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(newTo.getRestaurantId())
                .toUri();

        ResultActions action = perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        VoteTo created = VOTE_DATE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setVotedDate(created.getVotedDate());
        VOTE_DATE_TO_MATCHER.assertMatch(created, createDateTo(newVote));
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithNotFoundRestaurant() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        CreateVoteTo newTo = new CreateVoteTo(null, RESTAURANT1_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(newTo.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isNotFound());

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Clock testClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        CreateVoteTo updatedTo = new CreateVoteTo(VOTE6_ID, RESTAURANT1_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(updatedTo.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(repository.getExisted(updatedTo.id()), getUpdated());

        ClockHolder.setClock(beforeTestClock);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateAfterTimeLimit() throws Exception {
        Clock testClock = Clock.fixed(LATE.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        Clock beforeTestClock = ClockHolder.setClock(testClock);

        CreateVoteTo updatedTo = new CreateVoteTo(VOTE6_ID, RESTAURANT1_ID);

        URI uri = UriComponentsBuilder
                .fromUriString(REST_URL)
                .buildAndExpand(updatedTo.getRestaurantId())
                .toUri();

        perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        ClockHolder.setClock(beforeTestClock);
    }
}
