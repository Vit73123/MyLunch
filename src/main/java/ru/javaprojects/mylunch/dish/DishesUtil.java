package ru.javaprojects.mylunch.dish;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.to.DishTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class DishesUtil {

    public static Dish createNewFromTo(DishTo dishTo, int restaurantId) {
        return new Dish(null, dishTo.getDescription(), dishTo.getPrice(), restaurantId);
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setDescription(dishTo.getDescription());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getDescription(), dish.getPrice());
    }

    public static List<DishTo> createTos(Collection<Dish> dishes) {
        return dishes.stream()
                .map(DishesUtil::createTo)
                .toList();
    }
}
