package ru.javaprojects.mylunch.menu;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.to.ItemTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class ItemsUtil {

    public static ItemTo createTo(Item item) {
        return new ItemTo(item.getId(), item.getDish().getDescription(), item.getDish().getPrice(), item.getDishId());
    }

    public static List<ItemTo> createTos(Collection<Item> items) {
        return items.stream()
                .map(ItemsUtil::createTo)
                .toList();
    }
}
