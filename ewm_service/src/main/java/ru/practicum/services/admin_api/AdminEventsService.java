package ru.practicum.services.admin_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventDtoOut;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.models.AdminEventsParams;

import java.util.List;

public interface AdminEventsService {
    List<EventDtoOut> getEvents(AdminEventsParams adminEventsParams, Pageable pageable);

    EventDtoOut updateEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
