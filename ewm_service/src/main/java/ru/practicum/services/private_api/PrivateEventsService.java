package ru.practicum.services.private_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventsService {
    List<EventShortDto> getUserEvets(Long initiatorId, Pageable pageable);

    EventFullDto createEvent(Long initiatorId, NewEventDto newEventDto);

    EventFullDto getEventById(Long initiatorId, Long eventId);

    EventFullDto updateEvent(Long initiatorId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(Long initiatorId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long initiatorId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
