package ru.javaprojects.mylunch.dish.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;
import ru.javaprojects.mylunch.common.validation.NoHtml;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends BaseTo implements HasId {
    @Size(max = 255)
    @NotBlank
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String description;

    @Range(min = 5)
    int price;

    public DishTo(Integer id, String description, int price) {
        super(id);
        this.description = description;
        this.price = price;
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + description + ' ' + price + ']';
    }
}
