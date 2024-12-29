package ru.javaprojects.mylunch.restaurant.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.NamedTo;
import ru.javaprojects.mylunch.common.validation.NoHtml;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    @Email
    @NotBlank
    @Size(max = 64)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    public RestaurantTo(Integer id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    @Override
    public String toString() {
        return "RestaurantMenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email=" + email +
                '}';
    }
}
