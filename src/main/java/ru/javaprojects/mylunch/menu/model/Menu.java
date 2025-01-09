package ru.javaprojects.mylunch.menu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.View;
import ru.javaprojects.mylunch.common.model.BaseEntity;
import ru.javaprojects.mylunch.common.util.ClockHolder;
import ru.javaprojects.mylunch.dish.model.Dish;
import ru.javaprojects.mylunch.restaurant.model.Restaurant;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "menu",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"issued_date", "restaurant_id"}, name = "menu_unique_date_restaurant_idx")}
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity implements HasId {

    @Column(name = "issued_date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    private LocalDate issuedDate = ClockHolder.getDate();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-menus")
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @Range(min = 1)
    private int restaurantId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "menu_item",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @OrderBy("description")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "menu-items")
    private Set<Dish> items;

    public Menu(Menu m) {
        this(m.id, m.issuedDate, m.restaurantId);
    }

    public Menu(Integer id, LocalDate issuedDate) {
        super(id);
        this.issuedDate = issuedDate;

    }

    public Menu(Integer id, LocalDate issuedDate, int restaurantId) {
        super(id);
        this.issuedDate = issuedDate;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", date='" + issuedDate + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
