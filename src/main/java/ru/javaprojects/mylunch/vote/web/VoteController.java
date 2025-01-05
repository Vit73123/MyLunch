package ru.javaprojects.mylunch.vote.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaprojects.mylunch.app.AuthUser;
import ru.javaprojects.mylunch.app.config.AppPropertiesConfig;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.menu.repository.MenuRepository;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.to.VoteTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkTimeLimit;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController extends AbstractVoteController {

    public static final String REST_URL = "/api/profile/votes";

    @Autowired
    private AppPropertiesConfig app;

    @Autowired
    MenuRepository menuRepository;

    @GetMapping("/on-today")
    VoteTo get(@AuthenticationPrincipal AuthUser authUser) {
        return super.getByUserOnDate(ClockHolder.getDate(), authUser.id());
    }

    @GetMapping
    List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return super.getByUser(authUser.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Vote create(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create on today for restaurant id={} user id={}", authUser, restaurantId);
        LocalDateTime now = ClockHolder.getDateTime();
        Vote vote = new Vote(null, now.toLocalDate(), now.toLocalTime(), restaurantId, authUser.id());
        return voteRepository.prepareAndSave(vote);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update on today for restaurant id={} user id={}", authUser, restaurantId);
        LocalDateTime now = ClockHolder.getDateTime();
        LocalTime time = now.toLocalTime();
        checkTimeLimit(time, app.getVotedTimeLimit());
        LocalDate today = now.toLocalDate();
        Vote vote = voteRepository.getByDateAndUser(today, authUser.id());
        vote.setVotedTime(time);
        vote.setRestaurantId(restaurantId);
        voteRepository.prepareAndSave(vote);
    }
}
