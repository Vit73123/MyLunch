package ru.javaprojects.mylunch.menu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.View;
import ru.javaprojects.mylunch.common.model.BaseEntity;
import ru.javaprojects.mylunch.dish.model.Dish;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "menu_unique_menu_dish_idx")}
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, insertable = false, updatable = false)
    @OrderBy("issuedDate DESC")
    @JsonBackReference(value = "menu-items")
    @NotNull(groups = View.Persist.class)
    private Menu menu;

    @Column(name = "menu_id", nullable = false)
    @Range(min = 1)
    private int menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false, insertable = false, updatable = false)
    @OrderBy("description ASC")
    @JsonBackReference(value = "dish-items")
    @NotNull(groups = View.Persist.class)
    private Dish dish;

    @Column(name = "dish_id", nullable = false)
    @Range(min = 1)
    private int dishId;

    public Item(Integer id, int menuId, int dishId) {
        super(id);
        this.menuId = menuId;
        this.dishId = dishId;
    }
}
