package ru.javaprojects.mylunch.menu.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class ItemTo extends BaseTo implements HasId {
    @Range(min = 1)
    Integer dishId;

    public ItemTo(Integer id, int dishId) {
        super(id);
        this.dishId = dishId;
    }

    @Override
    public String toString() {
        return "ItemTo:" + id + '[' + " dishId=" + dishId + ']';
    }
}
