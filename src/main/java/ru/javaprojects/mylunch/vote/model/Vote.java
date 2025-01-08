package ru.javaprojects.mylunch.vote.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.View;
import ru.javaprojects.mylunch.common.model.BaseEntity;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.user.model.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "voted_date"}, name = "vote_unique_user_date_idx")}
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "voted_date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    private LocalDate votedDate = ClockHolder.getDate();

    @Column(name = "voted_time", nullable = false, columnDefinition = "time default current_time")
    @NotNull
    private LocalTime votedTime = ClockHolder.getTime();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @OrderBy("name ASC, email ASC")
    @JsonBackReference(value = "user-votes")
    @NotNull(groups = View.Persist.class)
    private User user;

    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, insertable = false, updatable = false)
    @OrderBy("name ASC, email ASC")
    @JsonBackReference(value = "restaurant-votes")
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @Range(min = 1)
    private int restaurantId;

    public Vote(Integer id, int restaurantId, int userId) {
        this(id, null, null, restaurantId, userId);
    }

    public Vote(Integer id, LocalDate votedDate, LocalTime votedTime, int restaurantId, int userId) {
        super(id);
        this.votedDate = votedDate;
        this.votedTime = votedTime;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}
