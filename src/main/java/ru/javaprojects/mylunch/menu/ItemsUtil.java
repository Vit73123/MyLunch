package ru.javaprojects.mylunch.menu;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.ItemTo;
import ru.javaprojects.mylunch.menu.to.MenuItemTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class ItemsUtil {

    public static Item createNewFromTo(ItemTo itemTo) {
        return new Item(null, itemTo.getDishId());
    }

    public static MenuItemTo createMenuItemTo(Item item) {
        return new MenuItemTo(item.getId(), item.getDish().getDescription(), item.getDish().getPrice(), item.getDishId());
    }

    public static List<MenuItemTo> createMenuItemTos(Collection<Item> items) {
        return items.stream()
                .map(ItemsUtil::createMenuItemTo)
                .toList();
    }
}
