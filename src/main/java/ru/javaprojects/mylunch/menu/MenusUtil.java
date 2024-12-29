package ru.javaprojects.mylunch.menu;

import lombok.experimental.UtilityClass;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenusUtil {

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getIssuedDate(), menu.getRestaurantId(), menu.getItems());
    }

    public static List<MenuTo> createTos(Collection<Menu> menus) {
        return menus.stream()
                .map(MenusUtil::createTo)
                .toList();
    }
}
