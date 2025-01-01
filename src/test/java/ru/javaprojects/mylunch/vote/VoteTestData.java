package ru.javaprojects.mylunch.vote;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.to.VoteTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;
import static ru.javaprojects.mylunch.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user", "votedTime");
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final LocalDate DAY_1 = LocalDate.of(2024, 12, 1);
    public static final LocalDate DAY_2 = LocalDate.of(2024, 12, 2);
    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalTime CURRENT_TIME = LocalTime.of(11, 0);
    public static final LocalDateTime NOW = LocalDateTime.of(TODAY, CURRENT_TIME);
    public static final LocalTime LATE_TIME = LocalTime.of(11, 1);
    public static final LocalDateTime LATE = LocalDateTime.of(TODAY, LATE_TIME);

    public static final int VOTE5_ID = 5;
    public static final int VOTE6_ID = 6;
    public static final int NOT_FOUND = 100;

    public static final Vote vote1 = new Vote(1, DAY_1, LocalTime.of(9, 0), RESTAURANT1_ID, ADMIN_ID);
    public static final Vote vote2 = new Vote(2, DAY_1, LocalTime.of(10, 0), RESTAURANT1_ID, USER_ID);
    public static final Vote vote3 = new Vote(3, DAY_2, LocalTime.of(10, 0), RESTAURANT3_ID, ADMIN_ID);
    public static final Vote vote4 = new Vote(4, DAY_2, LocalTime.of(10, 0), RESTAURANT3_ID, USER_ID);
    public static final Vote vote5 = new Vote(5, TODAY, LocalTime.of(9, 0), RESTAURANT1_ID, USER_ID);
    public static final Vote vote6 = new Vote(6, TODAY, LocalTime.of(10, 0), RESTAURANT2_ID, ADMIN_ID);

    public static final List<Vote> userVotes = List.of(vote5, vote4, vote2);
    public static final List<Vote> adminVotes = List.of(vote6, vote3, vote1);
    public static final List<Vote> todayVotes = List.of(vote5, vote6);
    public static final List<Vote> noVotes = new ArrayList<>();


    public static Vote getNew() {
        return new Vote(null, TODAY, CURRENT_TIME, RESTAURANT1_ID, GUEST_ID);
    }

    public static Vote getNewByUser(int userId) {
        return new Vote(null, TODAY, CURRENT_TIME, RESTAURANT1_ID, userId);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE5_ID, TODAY, CURRENT_TIME, RESTAURANT1_ID, ADMIN_ID);
    }
}
