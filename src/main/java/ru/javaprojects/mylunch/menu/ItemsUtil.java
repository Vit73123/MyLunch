package ru.javaprojects.mylunch.menu;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.to.CreateItemTo;
import ru.javaprojects.mylunch.menu.to.ItemTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class ItemsUtil {

    public static Item createNewFromTo(CreateItemTo itemTo) {
        return new Item(null, itemTo.getDishId());
    }

    public static ItemTo createMenuItemTo(Item item) {
        return new ItemTo(item.getId(), item.getDish().getDescription(), item.getDish().getPrice(), item.getDishId());
    }

    public static List<ItemTo> createMenuItemTos(Collection<Item> items) {
        return items.stream()
                .map(ItemsUtil::createMenuItemTo)
                .toList();
    }
}
