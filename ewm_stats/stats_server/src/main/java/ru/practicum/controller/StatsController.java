package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHit> create(@RequestBody EndpointHit endpointHit) {
        return ResponseEntity.ok().body(statsService.create(endpointHit));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam(required = false)
                                                        @DateTimeFormat(pattern = Constants.pattern) LocalDateTime start,
                                                    @RequestParam(required = false)
                                                        @DateTimeFormat(pattern = Constants.pattern) LocalDateTime end,
                                                    @RequestParam(required = false) List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {

        if (start == null || end == null) {
            throw new InvalidRequestException("Старт и конец не должны быть пустыми");
        }


        return ResponseEntity.ok().body(statsService.getStats(start, end, uris, unique));
    }
}
