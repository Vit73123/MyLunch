package ru.javaprojects.mylunch.menu;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant");
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuTo.class);

    public static final LocalDate DAY_1 = LocalDate.of(2024, 12, 1);
    public static final LocalDate DAY_2 = LocalDate.of(2024, 12, 2);
    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate NEW_DAY = LocalDate.of(2024, 12, 3);

    public static final int MENU1_ID = 1;
    public static final int MENU7_ID = 7;
//    public static final int MENU8_ID = 8;
    public static final int NOT_FOUND = 100;

    public static final Menu menu1 = new Menu(1, DAY_1, RESTAURANT3_ID);
    public static final Menu menu2 = new Menu(2, DAY_1, RESTAURANT1_ID);
//    public static final Menu menu3 = new Menu(3, DAY_1, RESTAURANT2_ID);
    public static final Menu menu4 = new Menu(4, DAY_2, RESTAURANT1_ID);
//    public static final Menu menu5 = new Menu(5, DAY_2, RESTAURANT3_ID);
//    public static final Menu menu6 = new Menu(6, DAY_2, RESTAURANT2_ID);
    public static final Menu menu7 = new Menu(7, TODAY, RESTAURANT1_ID);
//    public static final Menu menu8 = new Menu(8, TODAY, RESTAURANT2_ID);

    public static final List<Menu> restaurant1menus = List.of(menu7, menu4, menu2);

    public static Menu getNewOnDate(LocalDate date) {
        return new Menu(null, date, 0);
    }
}
