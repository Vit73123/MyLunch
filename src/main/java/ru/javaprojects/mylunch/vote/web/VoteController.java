package ru.javaprojects.mylunch.vote.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javaprojects.mylunch.app.AuthUser;
import ru.javaprojects.mylunch.app.config.AppPropertiesConfig;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.vote.VotesUtil;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.repository.VoteRepository;
import ru.javaprojects.mylunch.vote.to.VoteTo;
import ru.javaprojects.mylunch.vote.to.CreateVoteTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkTimeLimit;
import static ru.javaprojects.mylunch.vote.VotesUtil.createDateTo;
import static ru.javaprojects.mylunch.vote.VotesUtil.createDateTos;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    protected final Logger log = getLogger(getClass());

    public static final String REST_URL = "/api/profile/votes";

    @Autowired
    private AppPropertiesConfig app;

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/on-today")
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get for user id={} on today", authUser.id());
        return createDateTo(voteRepository.getByDateAndUser(ClockHolder.getDate(), authUser.id()));
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get for user id={}", authUser.id());
        return createDateTos(voteRepository.getByUser(authUser.id()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public VoteTo create(@Validated @RequestBody CreateVoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create on today for restaurant id={} user id={}", voteTo.getRestaurantId(), authUser.id());
        LocalDateTime now = ClockHolder.getDateTime();
        checkNew(voteTo);
        Vote vote = VotesUtil.createNewFromTo(voteTo, authUser.id());
        vote.setVotedDate(now.toLocalDate());
        vote.setVotedTime(now.toLocalTime());
        return createDateTo(voteRepository.prepareAndSave(vote));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Validated @RequestBody CreateVoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update on today for restaurant id={} user id={}", voteTo.getRestaurantId(), authUser.id());
        LocalDateTime now = ClockHolder.getDateTime();
        LocalTime time = now.toLocalTime();
        checkTimeLimit(time, app.getVotedTimeLimit());
        LocalDate today = now.toLocalDate();
        Vote vote = voteRepository.getByDateAndUser(today, authUser.id());
        VotesUtil.updateFromTo(vote, voteTo);
        voteRepository.prepareAndSave(vote);
    }
}
