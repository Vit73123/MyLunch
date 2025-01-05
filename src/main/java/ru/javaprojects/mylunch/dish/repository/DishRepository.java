package ru.javaprojects.mylunch.dish.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.dish.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurantId=:restaurantId")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    default Dish getExistedByRestaurantId(int id, int restaurantId) {
        return this.findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Dish with id=" + id + " restaurant_id=" + restaurantId + " not found"));
    }

    @Query("SELECT d FROM Dish d WHERE d.restaurantId=:restaurantId ORDER BY d.description ASC")
    List<Dish> getByRestaurant(int restaurantId);

    @Transactional
    default Dish prepareAndSave(Dish dish, int restaurantId) {
        if (!dish.isNew()) {
            findByIdAndRestaurantId(dish.id(), restaurantId).orElseThrow(
                    () -> new NotFoundException("Dish with id=" + dish.id() + " restaurant_id=" + restaurantId + " not found"));
        }
        dish.setRestaurantId(restaurantId);
        return save(dish);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int deleteByIdAndRestaurantId(int id, int restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExistedByRestaurantId(int id, int restaurantId) {
        if (deleteByIdAndRestaurantId(id, restaurantId) == 0) {
            throw new NotFoundException("Dish with id=" + id + " restaurant_id=" + restaurantId + " not found");
        }
    }

    default void checkExistsByRestaurant(int id, int restaurantId) {
        this.findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Dish with id=" + id + " of restaurant id=" + restaurantId + " not found"));
    }
}
