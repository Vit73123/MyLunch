package ru.javaprojects.mylunch.vote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javaprojects.mylunch.AbstractRepositoryTest;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.vote.model.Vote;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.mylunch.user.UserTestData.GUEST_ID;
import static ru.javaprojects.mylunch.user.UserTestData.USER_ID;
import static ru.javaprojects.mylunch.vote.VoteTestData.*;

class VoteRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    VoteRepository repository;

    @Test
    void getByDate() {
        VOTE_MATCHER.assertMatch(
                repository.getByDate(TODAY), todayVotes);
    }

    @Test
    void getByNewDate() {
        assertTrue(repository.getByUser(GUEST_ID).isEmpty());
    }

    @Test
    void getByUser() {
        VOTE_MATCHER.assertMatch(
                repository.getByUser(USER_ID), userVotes);
    }

    @Test
    void getByNewUser() {
        assertTrue(repository.getByUser(GUEST_ID).isEmpty());
    }

    @Test
    void save() {
        Vote created = repository.save(getNewByGuest());
        int newId = created.id();
        Vote newVote = getNewByGuest();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);
    }

    @Test
    void duplicateDateUserSave() {
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.save(getNewByUser()));
    }

    @Test
    void delete() {
        repository.deleteExisted(TODAY, USER_ID);
        assertFalse(repository.findByDateAndUserId(TODAY, USER_ID).isPresent());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                repository.deleteExisted(TODAY, GUEST_ID));
    }
}