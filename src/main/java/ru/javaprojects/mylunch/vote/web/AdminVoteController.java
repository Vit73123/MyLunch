package ru.javaprojects.mylunch.vote.web;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaprojects.mylunch.vote.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController extends AbstractVoteController {

    public static final String REST_URL = "/api/admin/votes";

    @GetMapping("/on-today")
    public List<VoteTo> getOnToday() {
        return super.getOnDate(LocalDate.now());
    }

    @GetMapping("/on-date")
    public List<VoteTo> getOnDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getOnDate(date);
    }
}
