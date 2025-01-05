package ru.javaprojects.mylunch.menu;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.MenuItemsTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenusUtil {

    public static Menu createNewFromTo(MenuTo menuTo) {
        return new Menu(null, menuTo.getIssuedDate());
    }

    public static MenuItemsTo createWithItemsTo(Menu menu) {
        return new MenuItemsTo(menu.getId(), menu.getIssuedDate(), menu.getRestaurantId(), menu.getItems());
    }

    public static List<MenuItemsTo> createWithItemsTos(Collection<Menu> menus) {
        return menus.stream()
                .map(MenusUtil::createWithItemsTo)
                .toList();
    }
}
