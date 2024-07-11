package ru.practicum.controllers.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.services.private_api.PrivateRequestsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestsController {
    private final PrivateRequestsService privateRequestsService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId) {
        return ResponseEntity.ok().body(privateRequestsService.getRequests(userId));
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId,
                                          @RequestParam(required = false) Long eventId) {
        if (eventId == null) {
            throw new InvalidRequestException("необходим параметр eventId");
        }

        return new ResponseEntity<>(privateRequestsService.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return ResponseEntity.ok().body(privateRequestsService.cancelRequest(userId, requestId));
    }
}
