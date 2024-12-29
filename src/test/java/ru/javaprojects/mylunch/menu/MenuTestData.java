package ru.javaprojects.mylunch.menu;

import ru.javaprojects.mylunch.MatcherFactory;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.menu.model.Item;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.ItemTo;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.javaprojects.mylunch.meal.MealTestData.*;
import static ru.javaprojects.mylunch.restaurant.RestaurantTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "items");
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class);
    public static final MatcherFactory.Matcher<Item> ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Item.class, "menu", "meal");
    public static final MatcherFactory.Matcher<ItemTo> ITEM_TO_MATCHER = MatcherFactory.usingEqualsComparator(ItemTo.class);

    public static final LocalDate DAY_1 = LocalDate.of(2024, 12, 1);
    public static final LocalDate DAY_2 = LocalDate.of(2024, 12, 2);
    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate NEW_DAY = LocalDate.of(2024, 12, 3);

    public static final int MENU1_ID = 1;
    public static final int MENU2_ID = 2;
    public static final int MENU3_ID = 3;
    public static final int MENU7_ID = 7;
    public static final int MENU8_ID = 8;

    public static final int ITEM1_ID = 1;
    public static final int ITEM3_ID = 3;
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
    public static final Item item12 = new Item(12, 7, 5);   // DAY_3 MENU_7 RESTAURANT_1
    public static final Item item13 = new Item(13, 7, 2);   // DAY_3 MENU_7 RESTAURANT_1
    public static final Item item14 = new Item(14, 7, 8);   // DAY_3 MENU_7 RESTAURANT_1
    public static final Item item15 = new Item(15, 8, 7);   // DAY_3 MENU_8 RESTAURANT_2
    public static final Item item16 = new Item(16, 8, 4);   // DAY_3 MENU_8 RESTAURANT_2

    static {
        menu1.setItems(sortedItems(meal1));                 // DAY_1 RESTAURANT_3
        menu2.setItems(sortedItems(meal8, meal2, meal5));   // DAY_1 RESTAURANT_1
        menu3.setItems(sortedItems(meal3, meal4));          // DAY_1 RESTAURANT_2
        menu4.setItems(sortedItems(meal5, meal2));          // DAY_2 RESTAURANT_1
        menu5.setItems(sortedItems(meal6));                 // DAY_2 RESTAURANT_3
        menu6.setItems(sortedItems(meal7, meal4));          // DAY_2 RESTAURANT_2
        menu7.setItems(sortedItems(meal5, meal2, meal8));   // DAY_3 RESTAURANT_1
        menu8.setItems(sortedItems(meal7, meal4));          // DAY_3 RESTAURANT_2

        item1.setMenu(menu1);     // DAY_1 MENU_1 RESTAURANT_3
        item2.setMenu(menu2);     // DAY_1 MENU_2 RESTAURANT_1
        item3.setMenu(menu2);     // DAY_1 MENU_2 RESTAURANT_1
        item4.setMenu(menu2);     // DAY_1 MENU_2 RESTAURANT_1
        item5.setMenu(menu3);     // DAY_1 MENU_3 RESTAURANT_2
        item6.setMenu(menu3);     // DAY_1 MENU_3 RESTAURANT_2
        item7.setMenu(menu4);     // DAY_2 MENU_4 RESTAURANT_1
        item8.setMenu(menu4);     // DAY_2 MENU_4 RESTAURANT_1
        item9.setMenu(menu5);     // DAY_2 MENU_5 RESTAURANT_3
        item10.setMenu(menu6);   // DAY_2 MENU_6 RESTAURANT_2
        item11.setMenu(menu6);   // DAY_2 MENU_6 RESTAURANT_2
        item12.setMenu(menu7);   // DAY_3 MENU_7 RESTAURANT_1
        item13.setMenu(menu7);   // DAY_3 MENU_7 RESTAURANT_1
        item14.setMenu(menu7);   // DAY_3 MENU_7 RESTAURANT_1
        item15.setMenu(menu8);   // DAY_3 MENU_7 RESTAURANT_2
        item16.setMenu(menu8);   // DAY_3 MENU_7 RESTAURANT_2

        item1.setMeal(meal1);     // DAY_1 MENU_1 RESTAURANT_3
        item2.setMeal(meal8);     // DAY_1 MENU_2 RESTAURANT_1
        item3.setMeal(meal2);     // DAY_1 MENU_2 RESTAURANT_1
        item4.setMeal(meal5);     // DAY_1 MENU_2 RESTAURANT_1
        item5.setMeal(meal3);     // DAY_1 MENU_3 RESTAURANT_2
        item6.setMeal(meal4);     // DAY_1 MENU_3 RESTAURANT_2
        item7.setMeal(meal5);     // DAY_2 MENU_4 RESTAURANT_1
        item8.setMeal(meal2);     // DAY_2 MENU_4 RESTAURANT_1
        item9.setMeal(meal6);     // DAY_2 MENU_5 RESTAURANT_3
        item10.setMeal(meal7);   // DAY_2 MENU_6 RESTAURANT_2
        item11.setMeal(meal4);   // DAY_2 MENU_6 RESTAURANT_2
        item12.setMeal(meal5);   // DAY_3 MENU_7 RESTAURANT_1
        item13.setMeal(meal2);   // DAY_3 MENU_7 RESTAURANT_1
        item14.setMeal(meal8);   // DAY_3 MENU_7 RESTAURANT_1
        item15.setMeal(meal7);   // DAY_3 MENU_7 RESTAURANT_2
        item16.setMeal(meal4);   // DAY_3 MENU_7 RESTAURANT_2
    }

//    public static final List<Item> menu1Items = List.of(item1);                     // DAY_1 RESTAURANT_3
//    public static final List<Item> menu2Items = List.of(item3, item4, item2);       // DAY_1 RESTAURANT_1
//    public static final List<Item> menu3Items = List.of(item6, item5);              // DAY_1 RESTAURANT_2
//    public static final List<Item> menu4Items = List.of(item8, item7);              // DAY_2 RESTAURANT_1
//    public static final List<Item> menu5Items = List.of(item9);                     // DAY_2 RESTAURANT_3
//    public static final List<Item> menu6Items = List.of(item11, item10);            // DAY_2 RESTAURANT_2
    public static final List<Item> menu7Items = List.of(item13, item12, item14);    // DAY_3 RESTAURANT_1
//    public static final List<Item> menu8Items = List.of(item16, item15);            // DAY_3 RESTAURANT_2

//    public static final List<Meal> menu1Meals = List.of(meal1);                     // DAY_1 RESTAURANT_3
//    public static final List<Meal> menu2Meals = List.of(meal2, meal5, meal8);       // DAY_1 RESTAURANT_1
//    public static final List<Meal> menu3Meals = List.of(meal4, meal3);              // DAY_1 RESTAURANT_2
//    public static final List<Meal> menu4Meals = List.of(meal2, meal5);              // DAY_2 RESTAURANT_1
//    public static final List<Meal> menu5Meals = List.of(meal6);                     // DAY_2 RESTAURANT_3
//    public static final List<Meal> menu6Meals = List.of(meal4, meal7);              // DAY_2 RESTAURANT_2
//    public static final List<Meal> menu7Meals = List.of(meal2, meal5, meal8);       // DAY_3 RESTAURANT_1
    public static final List<Meal> menu8Meals = List.of(meal4, meal7);              // DAY_3 RESTAURANT_2

    public static final List<Menu> restaurant1menus = List.of(menu7, menu4, menu2);
//    public static final List<Item> menu8Items = List.of(item11, item12, item10);    // DAY_3 RESTAURANT_2
//    public static final List<Item> todayMenus = List.of(item11, item12, item10);    // DAY_3 RESTAURANT_2

    public static Menu getNewMenuOnDate(LocalDate date) {
        return new Menu(null, date, 0);
    }

    public static Item getNewItem() {
        return new Item(null, MENU8_ID, MEAL3_ID);
    }

    private static Set<Meal> sortedItems(Meal... meals) {
        return Set.of(meals).stream().sorted(
                Comparator.comparing(Meal::getDescription)).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
