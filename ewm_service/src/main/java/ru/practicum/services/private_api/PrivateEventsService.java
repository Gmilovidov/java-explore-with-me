package ru.practicum.services.private_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventDtoOut;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequestDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventsService {
    List<EventShortDto> getUserEvents(Long initiatorId, Pageable pageable);

    EventDtoOut createEvent(Long initiatorId, NewEventDto newEventDto);

    EventDtoOut getEventById(Long initiatorId, Long eventId);

    EventDtoOut updateEvent(Long initiatorId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto);

    List<ParticipationRequestDto> getEventRequests(Long initiatorId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long initiatorId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
