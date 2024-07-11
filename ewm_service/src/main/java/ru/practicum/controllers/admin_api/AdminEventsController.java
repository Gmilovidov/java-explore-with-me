package ru.practicum.controllers.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.models.AdminEventsParams;
import ru.practicum.models.enums.State;
import ru.practicum.services.admin_api.AdminEventsService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam(required = false) List<Long> initiators,
                                                       @RequestParam(required = false) List<State> states,
                                                       @RequestParam(required = false) List<Long> categories,
                                                       @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.pattern)
                                        LocalDateTime rangeStart,
                                                       @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.pattern)
                                        LocalDateTime rangeEnd,
                                                       @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return ResponseEntity.ok().body(adminEventsService.getEvents(AdminEventsParams.builder()
                .initiators(initiators)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build(), pageable));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return ResponseEntity.ok().body(adminEventsService.updateEvent(eventId, updateEventAdminRequest));
    }
}
