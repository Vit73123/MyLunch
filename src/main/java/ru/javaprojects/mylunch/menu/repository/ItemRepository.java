package ru.javaprojects.mylunch.menu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.menu.model.Item;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ItemRepository extends BaseRepository<Item> {

    Optional<Item> findByMenuIdAndMealId(int menuId, int mealId);

    @Query("SELECT i FROM Item i JOIN i.meal m WHERE i.menuId=:menuId ORDER BY i.meal.description")
    List<Item> getByMenu(int menuId);
}
