package ru.practicum.controllers.public_api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.models.UserEventsParams;
import ru.practicum.services.public_api.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/events")
public class PublicEventsController {
    private final PublicEventsService publicEventsService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@RequestParam(required = false) String text,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) Boolean paid,
                                                        @RequestParam(required = false)
                                             @DateTimeFormat(pattern = Constants.pattern) LocalDateTime rangeStart,
                                                        @RequestParam(required = false)
                                             @DateTimeFormat(pattern = Constants.pattern)
                                             LocalDateTime rangeEnd,
                                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                        HttpServletRequest request) {
        return ResponseEntity.ok().body(publicEventsService.getEvents(UserEventsParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .request(request)
                .build(), from, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(publicEventsService.getEventById(id, request));
    }
}
