package ru.javaprojects.mylunch.meal.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.meal.model.Meal;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.restaurantId=:restaurantId")
    Optional<Meal> findByIdAndRestaurantId(int id, int restaurantId);

    default Meal getExistedByRestaurantId(int id, int restaurantId) {
        return this.findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Meal with id=" + id + "and restaurant_id=" + restaurantId + "not found"));
    }

    @Query("SELECT m FROM Meal m WHERE m.restaurantId=:restaurantId ORDER BY m.description ASC")
    List<Meal> getByRestaurant(int restaurantId);

    @Transactional
    default Meal prepareAndSave(Meal meal, int restaurantId) {
        if (!meal.isNew()) {
            findByIdAndRestaurantId(meal.id(), restaurantId).orElseThrow(
                    () -> new NotFoundException("Meal with id=" + meal.id() + "and restaurant_id=" + restaurantId + "not found"));
        }
        meal.setRestaurantId(restaurantId);
        return save(meal);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    int deleteByIdAndRestaurantId(int id, int restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExistedByRestaurantId(int id, int restaurantId) {
        if (deleteByIdAndRestaurantId(id, restaurantId) == 0) {
            throw new NotFoundException("Meal with id=" + id + "and restaurant_id=" + restaurantId + "not found");
        }
    }
}
