package ru.javaprojects.mylunch.restaurant.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.NotFoundException;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.email = LOWER(:email)")
    Optional<Restaurant> findByEmailIgnoreCase(String email);

    default Restaurant getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(
                () -> new NotFoundException("Restaurant with email=" + email + " not found"));
    }

    @Query("SELECT r FROM Restaurant r JOIN r.menus m WHERE m.issuedDate=:date ORDER BY r.name ASC")
    List<Restaurant> getOnDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m LEFT JOIN FETCH m.items WHERE m.issuedDate=:date ORDER BY r.name ASC")
    List<Restaurant> getWithMenusOnDate(@Param("date") LocalDate date);

    default void checkExists(int id) {
        if (!existsById(id)) {
            throw new NotFoundException("Restaurant with id=" + id + " not found");
        }
    }
}
