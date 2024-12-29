package ru.javaprojects.mylunch.menu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.menu.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurantId=:restaurantId")
    Optional<Menu> findByIdAndRestaurantId(int id, int restaurantId);

    default Menu getExisted(int id, int restaurantId) {
        return this.findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Menu with id=" + id + " of restaurant id=" + restaurantId + " not found"));
    }

    @Query("SELECT m FROM Menu m WHERE m.issuedDate=:issuedDate AND m.restaurantId=:restaurantId")
    Optional<Menu> findByDateAndRestaurantId(LocalDate issuedDate, int restaurantId);

    default Menu getExistedByDate(LocalDate date, int restaurantId) {
        return this.findByDateAndRestaurantId(date, restaurantId).orElseThrow(
                () -> new NotFoundException("Menu with date=" + date + " of restaurant id=" + restaurantId + " not found"));
    }

    @Query("SELECT m FROM Menu m WHERE m.restaurantId=:restaurantId ORDER BY m.issuedDate DESC")
    List<Menu> getByRestaurant(int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.issuedDate=:date ORDER BY m.restaurant.name ASC")
    List<Menu> getByDate(LocalDate date);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.items i " +
            "WHERE m.id=:id AND m.restaurantId=:restaurantId AND i.restaurantId=:restaurantId ORDER BY i.description ASC")
    Optional<Menu> findWithMealsByRestaurantId(int id, int restaurantId);

    default Menu getExistedWithMeals(int id, int restaurantId) {
        return this.findWithMealsByRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Menu with id=" + id + " of restaurant id=" + restaurantId + " not found"));
    }

    @Transactional
    default Menu prepareAndSave(Menu menu, int restaurantId) {
        menu.setRestaurantId(restaurantId);
        return save(menu);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    int deleteByIdAndRestaurantId(int id, int restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(int id, int restaurantId) {
        if (deleteByIdAndRestaurantId(id, restaurantId) == 0) {
            throw new NotFoundException("Menu with id=" + id + " of restaurant id=" + restaurantId + " not found");
        }
    }

    default void checkExistsByRestaurant(int id, int restaurantId) {
        this.findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new NotFoundException("Menu with id=" + id + " of restaurant id=" + restaurantId + " not found"));
    }
}
