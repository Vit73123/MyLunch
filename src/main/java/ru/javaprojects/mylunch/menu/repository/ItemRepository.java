package ru.javaprojects.mylunch.menu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.DataConflictException;
import ru.javaprojects.mylunch.menu.model.Item;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ItemRepository extends BaseRepository<Item> {

//    Optional<Item> findByMenuIdAndMealIdAndMenu_Restaurant_Id(int menuId, int mealId, int restaurantId);

    Optional<Item> findByMenuIdAndMealId(int menuId, int mealId);

//    @Query("SELECT i FROM Item i WHERE i.id=:id AND i.menuId=:menuId AND i.mealId=:mealId AND i.menu.restaurantId=:restaurantId")
//    Optional<Item> findByMenuIdAndMealIdAndMenu_RestaurantId(int menuId, int mealId, int restaurantId);

//    @Query("SELECT i FROM Item i JOIN FETCH i.meal m WHERE i.menuId=:menuId")
//    List<Item> getByMenu(int menuId);

//    List<Item> getByMenuIdAndMenu_RestaurantId(int menuId, int restaurantId);

//    @Query("SELECT i FROM Item i WHERE i.menuId=:menuId AND i.menu.restaurantId=:restaurantId")
//    Optional<List<Item>> findByMenuIdAndMenu_RestaurantId(int menuId, int restaurantId);

//    @Query("SELECT i FROM Item i JOIN i.meal m WHERE i.menuId=:menuId ORDER BY i.meal.description")
//    List<Item> findByMenu(int menuId, int restaurantId);

//    default List<Item> getByMenu(int menuId, int restaurantId) {
//        findByMenuIdAndMenu_RestaurantId(menuId, restaurantId).orElseThrow(
//                () -> new NotFoundException(
//                        "Items of menu id=" + menuId + " restaurant id=" + restaurantId + " not found"));
//        return findByMenu(menuId, restaurantId);
//    }

    @Query("SELECT i FROM Item i JOIN i.meal m WHERE i.menuId=:menuId ORDER BY i.meal.description")
    List<Item> getByMenu(int menuId);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Item i WHERE i.id=:id AND i.menuId=:menuId AND i.menu.restaurantId=:restaurantId")
//    int deleteByIdAndMenuIdAndMenu_RestaurantId(int id, int menuId, int restaurantId);
//
//    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
//    @SuppressWarnings("all") // transaction invoked
//    default void deleteExisted(int id, int menuId, int restaurantId) {
//        if (deleteByIdAndMenuIdAndMenu_RestaurantId(id, menuId, restaurantId) == 0) {
//            throw new NotFoundException(
//                    "Item with id=" + id + " of menu id=" + menuId + "restaurant id=" + restaurantId + " not found");
//        }
//    }
}
