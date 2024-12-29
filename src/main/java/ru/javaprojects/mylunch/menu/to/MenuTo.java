package ru.javaprojects.mylunch.menu.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;
import ru.javaprojects.mylunch.meal.MealsUtil;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.meal.to.MealTo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo implements HasId {
    @NotNull
    LocalDate issuedDate;

    Integer restaurantId;

    List<MealTo> items;

    public MenuTo(Integer id, LocalDate issuedDate, Integer restaurantId, Collection<Meal> items) {
        super(id);
        this.issuedDate = issuedDate;
        this.restaurantId = restaurantId;
        this.items = items != null ? MealsUtil.createTos(items) : null;
    }

    @Override
    public String toString() {
        return "MenuTo:" + id + '[' + issuedDate + "restaurantId=" + restaurantId + ']';
    }
}
