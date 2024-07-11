package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit create(@RequestBody EndpointHit endpointHit) {
        return statService.create(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(required = false)
                                    @DateTimeFormat(pattern = Constants.pattern) LocalDateTime start,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = Constants.pattern) LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {

        if (start == null || end == null) {
            throw new InvalidRequestException("The start and end dates of the range cannot be empty");
        }

        return statService.getStats(start, end, uris, unique);
    }
}
