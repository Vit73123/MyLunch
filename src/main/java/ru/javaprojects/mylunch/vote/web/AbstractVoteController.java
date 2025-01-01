package ru.javaprojects.mylunch.vote.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaprojects.mylunch.vote.repository.VoteRepository;
import ru.javaprojects.mylunch.vote.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.vote.VotesUtil.createTo;
import static ru.javaprojects.mylunch.vote.VotesUtil.createTos;

public abstract class AbstractVoteController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected VoteRepository voteRepository;

    public VoteTo getByUserOnDate(LocalDate date, int userId) {
        log.info("get for user id={} on date {}", userId, date);
        return createTo(voteRepository.getByDateAndUser(date, userId));
    }

    public List<VoteTo> getByUser(int userId) {
        log.info("get for user id={}", userId);
        return createTos(voteRepository.getByUser(userId));
    }

    public List<VoteTo> getOnDate(LocalDate date) {
        log.info("get on date {}", date);
        return createTos(voteRepository.getByDate(date));
    }
}
