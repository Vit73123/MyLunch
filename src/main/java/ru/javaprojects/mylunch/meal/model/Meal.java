package ru.javaprojects.mylunch.meal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.View;
import ru.javaprojects.mylunch.common.model.BaseEntity;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "meal",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"restaurant_id", "description"}, name = "meal_unique_menu_id_description_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal extends BaseEntity implements HasId {

    @Column(name = "description", nullable = false)
    @Size(max = 255)
    @NotBlank
    private String description;

    @Column(name = "price", nullable = false)
    @Range(min = 5)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-meals")
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @Range(min = 1)
    private int restaurantId;

    @ManyToMany(mappedBy = "items")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Menu> menus;

    public Meal(Meal m) {
        this(m.id, m.description, m.price, m.restaurantId);
        this.setRestaurant(m.restaurant);
    }

    public Meal(Integer id, String description, int price, int restaurantId) {
        super(id);
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
