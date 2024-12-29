package ru.javaprojects.mylunch.restaurant;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;
import ru.javaprojects.mylunch.restaurant.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantsUtil {

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getEmail());
    }

    public static List<RestaurantTo> createTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantsUtil::createTo)
                .toList();
    }
}
