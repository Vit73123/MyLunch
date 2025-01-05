package ru.javaprojects.mylunch.restaurant.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaprojects.mylunch.common.HasIdAndEmail;
import ru.javaprojects.mylunch.common.to.NamedTo;
import ru.javaprojects.mylunch.common.validation.NoHtml;
import ru.javaprojects.mylunch.dish.DishesUtil;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.dish.to.DishTo;

import java.util.Collection;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantDailyMenuTo extends NamedTo implements HasIdAndEmail {

    @Email
    @NotBlank
    @Size(max = 64)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    Menu menu;

    public RestaurantDailyMenuTo(Integer id, String name, String email, Integer menuId, Collection<Dish> items) {
        super(id, name);
        this.email = email;
        this.menu = new Menu(menuId, items != null ? DishesUtil.createTos(items) : null);
    }

    private static class Menu {
        Integer id;

        List<DishTo> items;

        public Menu(Integer id, List<DishTo> items) {
            this.id = id;
            this.items = items;
        }
    }

    @Override
    public String toString() {
        return "RestaurantMenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email=" + email +
                ", menu=" + menu +
                '}';
    }
}
