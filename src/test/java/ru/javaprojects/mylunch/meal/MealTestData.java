package ru.javaprojects.mylunch.meal;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.meal.to.MealTo;

import java.util.List;

import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant", "menus");
    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(MealTo.class);

    public static final int MEAL1_ID = 1;
    public static final int MEAL2_ID = 2;
    public static final int MEAL3_ID = 3;
    public static final int MEAL5_ID = 5;
    public static final int NOT_FOUND = 100;

    public static final String MEAL6_DESCRIPTION = "Обед В-2";

    public static final Meal meal1 = new Meal(1, "Обед В-1", 310, RESTAURANT3_ID);
    public static final Meal meal2 = new Meal(2, "Обед Б-1", 110, RESTAURANT1_ID);
    public static final Meal meal3 = new Meal(3, "Обед А-2", 220, RESTAURANT2_ID);
    public static final Meal meal4 = new Meal(4, "Обед А-1", 210, RESTAURANT2_ID);
    public static final Meal meal5 = new Meal(5, "Обед Б-2", 120, RESTAURANT1_ID);
    public static final Meal meal6 = new Meal(6, "Обед В-2", 320, RESTAURANT3_ID);
    public static final Meal meal7 = new Meal(7, "Обед А-3", 230, RESTAURANT2_ID);
    public static final Meal meal8 = new Meal(8, "Обед Б-3", 130, RESTAURANT1_ID);

    static {
//        meal1.setRestaurant(restaurant3);
//        meal2.setRestaurant(restaurant1);
//        meal3.setRestaurant(restaurant2);
//        meal4.setRestaurant(restaurant2);
//        meal5.setRestaurant(restaurant1);
//        meal6.setRestaurant(restaurant3);
//        meal7.setRestaurant(restaurant2);
//        meal8.setRestaurant(restaurant1);
    }

    public static final List<Meal> restaurant1meals = List.of(meal2, meal5, meal8);
//    public static final List<Meal> restaurant2meals = List.of(meal4, meal3, meal7);
//    public static final List<Meal> restaurant3meals = List.of(meal1, meal6);

    public static Meal getNew() {
        return new Meal(null, "Созданный обед", 100, 0);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDescription("Изменённый обед");
        updated.setPrice(101);
        return updated;
    }
}
