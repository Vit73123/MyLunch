package ru.javaprojects.mylunch.restaurant;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    //    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_MENU_MATCHER =
//            MatcherFactory.usingAssertions(Restaurant.class,
//                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("menus.restaurant", "menus.items").isEqualTo(e),
//                    (a, e) -> assertThat(a).usingRecursiveFieldByFieldElementComparatorIgnoringFields("menus.restaurant", "menus.items").isEqualTo(e));
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final String RESTAURANT1_EMAIL = "restaurant_b@yandex.ru";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Ресторан Б", "restaurant_b@yandex.ru");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Ресторан А", "restaurant_a@yandex.ru");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Ресторан В", "restaurant_c@yandex.ru");

//    static {
//        restaurant1.setMenus(List.of(menu2, menu4, menu7));
//        restaurant2.setMenus(List.of(menu3, menu6, menu8));
//        restaurant3.setMenus(List.of(menu1, menu5));
//    }

    public static final List<Restaurant> day1Restaurants = List.of(restaurant2, restaurant1, restaurant3);
    public static final List<Restaurant> day2Restaurants = List.of(restaurant2, restaurant1, restaurant3);
    public static final List<Restaurant> todayRestaurants = List.of(restaurant2, restaurant1);

    public static final List<Restaurant> restaurants = List.of(restaurant2, restaurant1, restaurant3);

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан", "new_restaurant@yandex.ru");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Изменённый ресторан", "updated_restaurant@yandex.ru");
    }
}
