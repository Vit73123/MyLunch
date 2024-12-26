package ru.javaprojects.mylunch.restaurant.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaprojects.mylunch.common.HasIdAndEmail;
import ru.javaprojects.mylunch.restaurant.repository.RestaurantRepository;

@Component
@AllArgsConstructor
public class UniqueRestaurantMailValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "Restaurant with this email already exists";

    private final RestaurantRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail restaurant = ((HasIdAndEmail) target);
        if (StringUtils.hasText(restaurant.getEmail())) {
            repository.findByEmailIgnoreCase(restaurant.getEmail())
                    .ifPresent(dbRestaurant -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbRestaurant.id();

                            // it is ok, if update ourselves
                            if (restaurant.getId() != null && dbId == restaurant.id()) return;

                            // Workaround for update with restaurant.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId))
                                return;
                        }
                        errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
