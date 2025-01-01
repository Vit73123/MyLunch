package ru.javaprojects.mylunch.vote;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.vote.model.Vote;
import ru.javaprojects.mylunch.vote.to.VoteTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VotesUtil {

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVotedDate(), vote.getRestaurantId());
    }

    public static List<VoteTo> createTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VotesUtil::createTo)
                .toList();
    }
}
