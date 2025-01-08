package ru.javaprojects.mylunch.vote.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class CreateVoteTo extends BaseTo implements HasId {

    @Range(min = 1)
    Integer restaurantId;

    public CreateVoteTo(Integer id, int restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "VoteTo:" + id + '[' + "restaurantId=" + restaurantId + ']';
    }
}
