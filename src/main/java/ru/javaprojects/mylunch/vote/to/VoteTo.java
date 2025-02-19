package ru.javaprojects.mylunch.vote.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {

    @NotNull
    LocalDate votedDate;

    @Range(min = 1)
    Integer restaurantId;

    public VoteTo(Integer id, LocalDate votedDate, int restaurantId) {
        super(id);
        this.votedDate = votedDate;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "VoteTo:" + id + '[' + votedDate + "restaurantId=" + restaurantId + ']';
    }
}
