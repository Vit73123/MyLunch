package ru.javaprojects.mylunch.vote.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.vote.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.votedDate=:date")
    List<Vote> getByDate(LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.userId=:userId ORDER BY v.votedDate DESC")
    List<Vote> getByUser(int userId);

    @Query("SELECT v FROM Vote v WHERE v.restaurantId=:restaurantId ORDER BY v.votedDate DESC, v.votedTime DESC")
    List<Vote> getByRestaurant(int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.votedDate=:date AND v.restaurantId=:restaurantId ORDER BY v.votedTime DESC")
    List<Vote> getByDateAndRestaurant(LocalDate date, int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.votedDate=:date AND v.userId=:userId")
    Optional<Vote> findByDateAndUserId(LocalDate date, int userId);

    default Vote getByDateAndUser(LocalDate date, int userId) {
        return findByDateAndUserId(date, userId).orElseThrow(
                () -> new NotFoundException("Vote for user id=" + userId + " on date " + date + " not found")
        );
    }

    @Transactional
    default Vote prepareAndSave(Vote vote) {
        if (vote.isNew()) {
            if (findByDateAndUserId(vote.getVotedDate(), vote.getUserId()).orElse(null) != null) {
                throw new NotFoundException(
                        "Vote for user id=" + vote.getUserId() + " on date " + vote.getVotedDate() + " already exists");
            }
        }
        return this.save(vote);
    }

    @Transactional
    @Query("DELETE FROM Vote v WHERE v.votedDate=:date AND v.userId=:userId")
    @Modifying
    int deleteByDateAndUerId(LocalDate date, int userId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(LocalDate date, int userId) {
        if (deleteByDateAndUerId(date, userId) == 0) {
            throw new NotFoundException("Vote for user id=" + userId + " on date " + date + " not found");
        }
    }
}
