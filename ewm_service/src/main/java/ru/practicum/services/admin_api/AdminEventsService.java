package ru.practicum.services.admin_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.models.AdminEventsParams;

import java.util.List;

public interface AdminEventsService {
    List<EventFullDto> getEvents(AdminEventsParams adminEventsParams, Pageable pageable);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
