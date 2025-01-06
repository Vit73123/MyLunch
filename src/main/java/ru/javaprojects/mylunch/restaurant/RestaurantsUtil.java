package ru.javaprojects.mylunch.restaurant;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.to.RestaurantMenuTo;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantsUtil {

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName(), restaurantTo.getEmail().toLowerCase());
    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        restaurant.setEmail(restaurantTo.getEmail().toLowerCase());
        return restaurant;
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getEmail());
    }

    public static List<RestaurantTo> createTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantsUtil::createTo)
                .toList();
    }

    public static RestaurantMenuTo createWithDailyMenuTo(Menu menu) {
        return new RestaurantMenuTo(
                menu.getRestaurantId(),
                menu.getRestaurant().getName(),
                menu.getRestaurant().getEmail(),
                menu.getId(),
                menu.getItems());
    }

    public static List<RestaurantMenuTo> createWithDailyMenuTos(Collection<Menu> menus) {
        return menus.stream()
                .map(RestaurantsUtil::createWithDailyMenuTo)
                .toList();
    }
}
