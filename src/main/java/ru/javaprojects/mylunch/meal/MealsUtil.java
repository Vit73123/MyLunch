package ru.javaprojects.mylunch.meal;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.meal.to.MealTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MealsUtil {

    public static Meal createNewFromTo(MealTo mealTo) {
        return new Meal(null, mealTo.getDescription(), mealTo.getPrice(), 0);
    }

    public static Meal updateFromTo(Meal meal, MealTo mealTo) {
        meal.setDescription(mealTo.getDescription());
        meal.setPrice(mealTo.getPrice());
        return meal;
    }

    public static MealTo createTo(Meal meal) {
        return new MealTo(meal.getId(), meal.getDescription(), meal.getPrice());
    }

    public static List<MealTo> createTos(Collection<Meal> meals) {
        return meals.stream()
                .map(MealsUtil::createTo)
                .toList();
    }
}
