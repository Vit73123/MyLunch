package ru.javaprojects.mylunch.dish;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.to.DishTo;

import java.util.List;

import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant", "menus");
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH5_ID = 5;
    public static final int NOT_USED = 9;
    public static final int NOT_FOUND = 100;

    public static final String DISH6_DESCRIPTION = "Обед В-2";
    public static final String DISH8_DESCRIPTION = "Обед Б-3";

    public static final Dish dish1 = new Dish(1, "Обед В-1", 310, RESTAURANT3_ID);
    public static final Dish dish2 = new Dish(2, "Обед Б-1", 110, RESTAURANT1_ID);
    public static final Dish dish3 = new Dish(3, "Обед А-2", 220, RESTAURANT2_ID);
    public static final Dish dish4 = new Dish(4, "Обед А-1", 210, RESTAURANT2_ID);
    public static final Dish dish5 = new Dish(5, "Обед Б-2", 120, RESTAURANT1_ID);
    public static final Dish dish6 = new Dish(6, "Обед В-2", 320, RESTAURANT3_ID);
    public static final Dish dish7 = new Dish(7, "Обед А-3", 230, RESTAURANT2_ID);
    public static final Dish dish8 = new Dish(8, "Обед Б-3", 130, RESTAURANT1_ID);
    public static final Dish notUsed = new Dish(9, "Обед Б-4", 140, RESTAURANT1_ID);

    public static final List<Dish> restaurant1dishes = List.of(dish2, dish5, dish8, notUsed);

    public static Dish getNew() {
        return new Dish(null, "Созданный обед", 100, 0);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(notUsed);
        updated.setDescription("Изменённый обед");
        updated.setPrice(141);
        return updated;
    }
}
