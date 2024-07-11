package ru.practicum.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.exceptions.WrongConditionException;
import ru.practicum.helper.UtilsUpdateWithoutNull;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.AdminEventsParams;
import ru.practicum.models.Event;
import ru.practicum.models.Location;
import ru.practicum.models.enums.AdminStateAction;
import ru.practicum.models.enums.State;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.LocationRepository;
import ru.practicum.services.admin_api.AdminEventsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getEvents(AdminEventsParams adminEventsParams,
                                        Pageable pageable) {

        List<Event> events = eventRepository.getEventsByParameters(adminEventsParams.getInitiators(),
                adminEventsParams.getStates(),
                adminEventsParams.getCategories(),
                adminEventsParams.getRangeStart(),
                adminEventsParams.getRangeEnd(), pageable).getContent();

        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return events.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие с id=" + eventId + " не найдено."));
        LocalDateTime newEventDate = updateEventAdminRequestDto.getEventDate();

        if (newEventDate != null) {
            if (newEventDate.isBefore(LocalDateTime.now())) {
                throw new InvalidRequestException("Дата события не может быть ранее текущего момента");
            }

            LocalDateTime publishDate = eventToUpdate.getPublishedOn();

            if (newEventDate.isBefore(publishDate.minusHours(1))) {
                throw new InvalidRequestException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.");
            }
        }

        if (updateEventAdminRequestDto.getStateAction() != null) {
            if (eventToUpdate.getState() != State.PENDING) {
                throw new WrongConditionException("Событие можно публиковать, только если оно в состоянии ожидания публикации. " +
                        "Текущий статус :" + eventToUpdate.getState());
            }

            if (updateEventAdminRequestDto.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                eventToUpdate.setState(State.PUBLISHED);
                eventToUpdate.setPublishedOn(LocalDateTime.now());
            } else {
                eventToUpdate.setState(State.CANCELED);
            }
        }

        if (updateEventAdminRequestDto.getLocation() != null) {
            Location location = locationRepository.save(updateEventAdminRequestDto.getLocation());
            eventToUpdate.setLocation(location);
        }

        UtilsUpdateWithoutNull.copyProperties(updateEventAdminRequestDto, eventToUpdate);

        return EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));
    }
}
