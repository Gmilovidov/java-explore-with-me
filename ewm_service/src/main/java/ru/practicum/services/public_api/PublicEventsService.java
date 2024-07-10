package ru.practicum.services.public_api;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.models.UserEventsParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventsService {
    EventFullDto getEventById(Long id, HttpServletRequest request);

    List<EventShortDto> getEvents(UserEventsParams build, Integer from, Integer size);
}