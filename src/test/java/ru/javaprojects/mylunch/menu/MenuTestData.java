package ru.javaprojects.mylunch.menu;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.ItemTo;
import ru.javaprojects.mylunch.menu.to.MenuItemsTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.javaprojects.mylunch.dish.DishTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "items");
    public static final MatcherFactory.Matcher<MenuItemsTo> MENU_ITEMS_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItemsTo.class);
    public static final MatcherFactory.Matcher<Item> ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Item.class, "menu", "dish");
    public static final MatcherFactory.Matcher<ItemTo> MENU_ITEM_TO_MATCHER = MatcherFactory.usingEqualsComparator(ItemTo.class);

    public static final LocalDate DAY_1 = LocalDate.of(2024, 12, 1);
    public static final LocalDate DAY_2 = LocalDate.of(2024, 12, 2);
    public static final LocalDate TODAY = ClockHolder.getDate();
    public static final LocalDate NEW_DAY = LocalDate.of(2024, 12, 3);

    public static final int MENU1_ID = 1;
    public static final int MENU7_ID = 7;
    public static final int MENU8_ID = 8;

    public static final int ITEM12_ID = 12;

    public static final int NOT_FOUND = 100;

    public static final Menu menu1 = new Menu(1, DAY_1, RESTAURANT3_ID);
    public static final Menu menu2 = new Menu(2, DAY_1, RESTAURANT1_ID);
    public static final Menu menu3 = new Menu(3, DAY_1, RESTAURANT2_ID);
    public static final Menu menu4 = new Menu(4, DAY_2, RESTAURANT1_ID);
    public static final Menu menu5 = new Menu(5, DAY_2, RESTAURANT3_ID);
    public static final Menu menu6 = new Menu(6, DAY_2, RESTAURANT2_ID);
    public static final Menu menu7 = new Menu(7, TODAY, RESTAURANT1_ID);
    public static final Menu menu8 = new Menu(8, TODAY, RESTAURANT2_ID);

    public static final Item item1 = new Item(1, 1, 1);     // DAY_1 MENU_1 RESTAURANT_3
    public static final Item item2 = new Item(2, 2, 8);     // DAY_1 MENU_2 RESTAURANT_1
    public static final Item item3 = new Item(3, 2, 2);     // DAY_1 MENU_2 RESTAURANT_1
    public static final Item item4 = new Item(4, 2, 5);     // DAY_1 MENU_2 RESTAURANT_1
    public static final Item item5 = new Item(5, 3, 3);     // DAY_1 MENU_3 RESTAURANT_2
    public static final Item item6 = new Item(6, 3, 4);     // DAY_1 MENU_3 RESTAURANT_2
    public static final Item item7 = new Item(7, 4, 5);     // DAY_2 MENU_4 RESTAURANT_1
    public static final Item item8 = new Item(8, 4, 2);     // DAY_2 MENU_4 RESTAURANT_1
    public static final Item item9 = new Item(9, 5, 6);     // DAY_2 MENU_5 RESTAURANT_3
    public static final Item item10 = new Item(10, 6, 7);   // DAY_2 MENU_6 RESTAURANT_2
    public static final Item item11 = new Item(11, 6, 4);   // DAY_2 MENU_6 RESTAURANT_2
    public static final Item item12 = new Item(12, 7, 5);   // TODAY MENU_7 RESTAURANT_1
    public static final Item item13 = new Item(13, 7, 2);   // TODAY MENU_7 RESTAURANT_1
    public static final Item item14 = new Item(14, 7, 8);   // TODAY MENU_7 RESTAURANT_1
    public static final Item item15 = new Item(15, 8, 7);   // TODAY MENU_8 RESTAURANT_2
    public static final Item item16 = new Item(16, 8, 4);   // TODAY MENU_8 RESTAURANT_2

    static {
        menu1.setRestaurant(restaurant3);                       // DAY_1 RESTAURANT_1
        menu2.setRestaurant(restaurant1);                       // DAY_1 RESTAURANT_1
        menu3.setRestaurant(restaurant2);                       // DAY_1 RESTAURANT_1
        menu4.setRestaurant(restaurant1);                       // DAY_2 RESTAURANT_1
        menu5.setRestaurant(restaurant3);                       // DAY_2 RESTAURANT_1
        menu6.setRestaurant(restaurant2);                       // DAY_2 RESTAURANT_1
        menu7.setRestaurant(restaurant1);                       // TODAY RESTAURANT_1
        menu8.setRestaurant(restaurant2);                       // TODAY RESTAURANT_1

        menu1.setItems(sortedItems(dish1));                     // DAY_1 RESTAURANT_3
        menu2.setItems(sortedItems(dish8, dish2, dish5));       // DAY_1 RESTAURANT_1
        menu3.setItems(sortedItems(dish3, dish4));              // DAY_1 RESTAURANT_2
        menu4.setItems(sortedItems(dish5, dish2));              // DAY_2 RESTAURANT_1
        menu5.setItems(sortedItems(dish6));                     // DAY_2 RESTAURANT_3
        menu6.setItems(sortedItems(dish7, dish4));              // DAY_2 RESTAURANT_2
        menu7.setItems(sortedItems(dish5, dish2, dish8));       // TODAY RESTAURANT_1
        menu8.setItems(sortedItems(dish7, dish4));              // TODAY RESTAURANT_2

        item1.setMenu(menu1);       // DAY_1 MENU_1 RESTAURANT_3
        item2.setMenu(menu2);       // DAY_1 MENU_2 RESTAURANT_1
        item3.setMenu(menu2);       // DAY_1 MENU_2 RESTAURANT_1
        item4.setMenu(menu2);       // DAY_1 MENU_2 RESTAURANT_1
        item5.setMenu(menu3);       // DAY_1 MENU_3 RESTAURANT_2
        item6.setMenu(menu3);       // DAY_1 MENU_3 RESTAURANT_2
        item7.setMenu(menu4);       // DAY_2 MENU_4 RESTAURANT_1
        item8.setMenu(menu4);       // DAY_2 MENU_4 RESTAURANT_1
        item9.setMenu(menu5);       // DAY_2 MENU_5 RESTAURANT_3
        item10.setMenu(menu6);      // DAY_2 MENU_6 RESTAURANT_2
        item11.setMenu(menu6);      // DAY_2 MENU_6 RESTAURANT_2
        item12.setMenu(menu7);      // TODAY MENU_7 RESTAURANT_1
        item13.setMenu(menu7);      // TODAY MENU_7 RESTAURANT_1
        item14.setMenu(menu7);      // TODAY MENU_7 RESTAURANT_1
        item15.setMenu(menu8);      // TODAY MENU_7 RESTAURANT_2
        item16.setMenu(menu8);      // TODAY MENU_7 RESTAURANT_2

        item1.setDish(dish1);       // DAY_1 MENU_1 RESTAURANT_3
        item2.setDish(dish8);       // DAY_1 MENU_2 RESTAURANT_1
        item3.setDish(dish2);       // DAY_1 MENU_2 RESTAURANT_1
        item4.setDish(dish5);       // DAY_1 MENU_2 RESTAURANT_1
        item5.setDish(dish3);       // DAY_1 MENU_3 RESTAURANT_2
        item6.setDish(dish4);       // DAY_1 MENU_3 RESTAURANT_2
        item7.setDish(dish5);       // DAY_2 MENU_4 RESTAURANT_1
        item8.setDish(dish2);       // DAY_2 MENU_4 RESTAURANT_1
        item9.setDish(dish6);       // DAY_2 MENU_5 RESTAURANT_3
        item10.setDish(dish7);      // DAY_2 MENU_6 RESTAURANT_2
        item11.setDish(dish4);      // DAY_2 MENU_6 RESTAURANT_2
        item12.setDish(dish5);      // TODAY MENU_7 RESTAURANT_1
        item13.setDish(dish2);      // TODAY MENU_7 RESTAURANT_1
        item14.setDish(dish8);      // TODAY MENU_7 RESTAURANT_1
        item15.setDish(dish7);      // TODAY MENU_7 RESTAURANT_2
        item16.setDish(dish4);      // TODAY MENU_7 RESTAURANT_2
    }

    public static final List<Item> menu1Items = List.of(item1);                     // DAY_1 RESTAURANT_3
    public static final List<Item> menu2Items = List.of(item3, item4, item2);       // DAY_1 RESTAURANT_1
    public static final List<Item> menu3Items = List.of(item6, item5);              // DAY_1 RESTAURANT_2
    public static final List<Item> menu4Items = List.of(item8, item7);              // DAY_2 RESTAURANT_1
    public static final List<Item> menu5Items = List.of(item9);                     // DAY_2 RESTAURANT_3
    public static final List<Item> menu6Items = List.of(item11, item10);            // DAY_2 RESTAURANT_2
    public static final List<Item> menu7Items = List.of(item13, item12, item14);    // TODAY RESTAURANT_1
    public static final List<Item> menu8Items = List.of(item16, item15);            // TODAY RESTAURANT_2

    public static final List<Dish> MENU_1_DISHES = List.of(dish1);                  // DAY_1 RESTAURANT_3
    public static final List<Dish> MENU_2_DISHES = List.of(dish2, dish5, dish8);    // DAY_1 RESTAURANT_1
    public static final List<Dish> MENU_3_DISHES = List.of(dish4, dish3);           // DAY_1 RESTAURANT_2
    public static final List<Dish> MENU_4_DISHES = List.of(dish2, dish5);           // DAY_2 RESTAURANT_1
    public static final List<Dish> MENU_5_DISHES = List.of(dish6);                  // DAY_2 RESTAURANT_3
    public static final List<Dish> MENU_6_DISHES = List.of(dish4, dish7);           // DAY_2 RESTAURANT_2
    public static final List<Dish> MENU_7_DISHES = List.of(dish2, dish5, dish8);    // TODAY RESTAURANT_1
    public static final List<Dish> MENU_8_DISHES = List.of(dish4, dish7);           // TODAY RESTAURANT_2

    public static final List<Menu> restaurant1menus = List.of(menu7, menu4, menu2); // ALL FOR RESTAURANT_1
    public static final List<Menu> day1Menus = List.of(menu3, menu2, menu1);        // DAY_1 SORTED BY RESTAURANTS NAMES
    public static final List<Menu> todayMenus = List.of(menu8, menu7);              // TODAY SORTED BY RESTAURANTS NAMES
    public static final List<Item> todayMenu = List.of(item11, item12, item10);     // TODAY RESTAURANT_2

    public static Menu getNewOnToday() {
        return new Menu(null, TODAY, RESTAURANT3_ID);
    }

    public static MenuTo getNewToOnToday() {
        return new MenuTo(null, null);
    }

    public static MenuTo getNewToOnDate() {
        return new MenuTo(null, NEW_DAY);
    }

    public static Item getNewItem() {
        return new Item(null, MENU8_ID, DISH3_ID);
    }

    private static Set<Dish> sortedItems(Dish... dishes) {
        return Set.of(dishes).stream().sorted(
                Comparator.comparing(Dish::getDescription)).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
