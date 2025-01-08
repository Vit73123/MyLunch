package ru.javaprojects.mylunch.vote;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.to.VoteTo;
import ru.javaprojects.mylunch.vote.to.CreateVoteTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VotesUtil {

    public static Vote createNewFromTo(CreateVoteTo voteTo, int userId) {
        return new Vote(null, voteTo.getRestaurantId(), userId);
    }

    public static Vote updateFromTo(Vote vote, CreateVoteTo voteTo) {
        vote.setRestaurantId(voteTo.getRestaurantId());
        return vote;
    }

    public static VoteTo createDateTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVotedDate(), vote.getRestaurantId());
    }

    public static List<VoteTo> createDateTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VotesUtil::createDateTo)
                .toList();
    }
}
