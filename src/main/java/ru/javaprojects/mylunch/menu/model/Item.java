package ru.javaprojects.mylunch.menu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.View;
import ru.javaprojects.mylunch.common.model.BaseEntity;
import ru.javaprojects.mylunch.meal.model.Meal;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"menu_id", "meal_id"}, name = "menu_unique_menu_item_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, insertable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("issuedDate DESC")
    @JsonBackReference(value = "menu-items")
    @NotNull(groups = View.Persist.class)
    private Menu menu;

    @Column(name = "menu_id", nullable = false)
    @Range(min = 1)
    private int menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false, insertable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("description ASC")
    @JsonBackReference(value = "meal-items")
    @NotNull(groups = View.Persist.class)
    private Meal meal;

    @Column(name = "meal_id", nullable = false)
    @Range(min = 1)
    private int mealId;

    public Item(Integer id, int menuId, int mealId) {
        super(id);
        this.menuId = menuId;
        this.mealId = mealId;
    }
}
