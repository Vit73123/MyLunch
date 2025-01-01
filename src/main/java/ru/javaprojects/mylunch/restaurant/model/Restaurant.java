package ru.javaprojects.mylunch.restaurant.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaprojects.mylunch.common.HasIdAndEmail;
import ru.javaprojects.mylunch.common.model.NamedEntity;
import ru.javaprojects.mylunch.meal.model.Meal;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.vote.model.Vote;

import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasIdAndEmail {

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "restaurant-meals")
    private List<Meal> meals;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("issuedDate")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "restaurant-menus")
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("votedDate DESC")
    @JsonManagedReference(value = "restaurant-votes")
    private List<Vote> votes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.email);
    }

    public Restaurant(Integer id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
