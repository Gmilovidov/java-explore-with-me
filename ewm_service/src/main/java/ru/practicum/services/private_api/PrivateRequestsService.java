package ru.practicum.services.private_api;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestsService {
    List<ParticipationRequestDto> getRequests(Long requesterId);

    ParticipationRequestDto create(Long requesterId, Long eventId);

    ParticipationRequestDto cancelRequest(Long requesterId, Long requestId);
}
