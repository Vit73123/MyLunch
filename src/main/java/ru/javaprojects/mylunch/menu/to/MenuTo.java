package ru.javaprojects.mylunch.menu.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaprojects.mylunch.common.HasId;
import ru.javaprojects.mylunch.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo implements HasId {
    @NotNull
    LocalDate issuedDate;

    public MenuTo(Integer id, LocalDate issuedDate) {
        super(id);
        this.issuedDate = issuedDate;
    }

    @Override
    public String toString() {
        return "MenuTo:" + id + '[' + issuedDate + ']';
    }
}
