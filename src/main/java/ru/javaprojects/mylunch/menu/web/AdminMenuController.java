package ru.javaprojects.mylunch.menu.web;

import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.mylunch.menu.model.Menu;
import ru.javaprojects.mylunch.menu.to.MenuTo;

import javax.annotation.Nullable;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaprojects.mylunch.common.validation.ValidationUtil.checkNew;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTo;
import static ru.javaprojects.mylunch.menu.MenusUtil.createTos;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends AbstractMenuController {
    protected final Logger log = getLogger(getClass());

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    @Override
    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id, @PathVariable int restaurantId) {
        return super.get(id, restaurantId);
    }

    @GetMapping("/by-date")
    public MenuTo getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            @PathVariable int restaurantId) {
        log.info("getByDate {} of restaurant id={}", date, restaurantId);
        return createTo(menuRepository.getExistedByDate(date, restaurantId));
    }

    @GetMapping
    @Transactional
    public List<MenuTo> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant id={}", restaurantId);
        restaurantRepository.checkExists(restaurantId);
        return createTos(menuRepository.getByRestaurant(restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Menu> createWithLocation(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable int restaurantId) {
        log.info("create on date {} of restaurant id={}", date, restaurantId);
        restaurantRepository.checkExists(restaurantId);
        Menu newMenu = menuRepository.findByDateAndRestaurantId(date, restaurantId).orElse(
                new Menu(null, date != null ? date : LocalDate.now(), 0)
        );
        checkNew(newMenu);
        Menu created = menuRepository.prepareAndSave(newMenu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurantId)
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete with id={} of restaurant id={}", id, restaurantId);
        menuRepository.deleteExistedByRestaurantId(id, restaurantId);
    }

}
