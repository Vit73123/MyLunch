package ru.javaprojects.mylunch.menu.to;

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
public class ItemTo extends BaseTo implements HasId {
    @Size(max = 255)
    @NotBlank
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String description;

    @Range(min = 5)
    int price;

    @Range(min = 1)
    Integer dishId;

    public ItemTo(Integer id, String description, int price, int dishId) {
        super(id);
        this.description = description;
        this.price = price;
        this.dishId = dishId;
    }

    @Override
    public String toString() {
        return "ItemTo:" + id + '[' + description + ' ' + price + " dishId=" + dishId + ']';
    }
}
