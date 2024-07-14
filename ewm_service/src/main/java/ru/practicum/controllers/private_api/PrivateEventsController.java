package ru.practicum.controllers.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDtoOut;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequestDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.services.private_api.PrivateEventsService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventsController {
    private final PrivateEventsService privateEventsService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getUserEvents(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return ResponseEntity.ok().body(privateEventsService.getUserEvents(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<EventDtoOut> createEvent(@PathVariable Long userId,
                                                   @Valid @RequestBody NewEventDto newEventDto) {
        return new ResponseEntity<>(privateEventsService.createEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDtoOut> getEventById(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privateEventsService.getEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDtoOut> updateEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @Valid @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto) {
        return ResponseEntity.ok().body(privateEventsService.updateEvent(userId, eventId, updateEventUserRequestDto));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privateEventsService.getEventRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return ResponseEntity.ok().body(privateEventsService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest));
    }
}
