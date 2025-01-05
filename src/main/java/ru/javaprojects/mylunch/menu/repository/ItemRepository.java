package ru.javaprojects.mylunch.menu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.mylunch.common.BaseRepository;
import ru.javaprojects.mylunch.common.error.IllegalRequestDataException;
import ru.javaprojects.mylunch.menu.model.Item;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ItemRepository extends BaseRepository<Item> {

    Optional<Item> findByMenuIdAndDishId(int menuId, int dishId);

    @Query("SELECT i FROM Item i JOIN i.dish m WHERE i.menuId=:menuId ORDER BY i.dish.description")
    List<Item> getByMenu(int menuId);

    @Transactional
    default Item prepareAndSave(Item item, int menuId) {
        int dishId = item.getDishId();
        if (findByMenuIdAndDishId(menuId, dishId).orElse(null) != null) {
            throw new IllegalRequestDataException("item with dish id=" + item.getDish() + " for menu id=" + menuId + " already exists");
        }
        item.setMenuId(menuId);
        return save(item);
    }

    Optional<Item> findByDishId(int dishId);

    default void checkDishNotExists(int dishId) {
        if (findByDishId(dishId).orElse(null) != null) {
            throw new IllegalRequestDataException("dish with id=" + dishId + " already used and could not be changed");
        }
    }
}
