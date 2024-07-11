package ru.practicum.services.public_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.Constants;
import ru.practicum.EndpointHit;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Event;
import ru.practicum.models.UserEventsParams;
import ru.practicum.models.enums.State;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.public_api.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {
    private static final LocalDateTime PAST = LocalDateTime.now().minusYears(300);

    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(UserEventsParams userEventsParams, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (userEventsParams.getSort() != null) {
            if (userEventsParams.getSort().equals("EVENT_DATE")) {
                pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
            } else if (userEventsParams.getSort().equals("VIEWS")) {
                pageable = PageRequest.of(from / size, size, Sort.by("views"));
            } else {
                throw new InvalidRequestException("Параметр sort задан неверно");
            }
        }

        if (userEventsParams.getRangeStart() != null && userEventsParams.getRangeEnd() != null
                && userEventsParams.getRangeStart().isAfter(userEventsParams.getRangeEnd())) {
            throw new InvalidRequestException("указан некорректный диапазон дат");
        }

        List<Event> events = eventRepository.getEventsByUserParameters(userEventsParams.getText(),
                userEventsParams.getCategories(), userEventsParams.getPaid(), userEventsParams.getRangeStart(),
                userEventsParams.getRangeEnd(), pageable).getContent();

        if (userEventsParams.getOnlyAvailable()) {
            events = events.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        statsClient.create(EndpointHit.builder()
                .app("ewm_service")
                .uri(userEventsParams.getRequest().getRequestURI())
                .ip(userEventsParams.getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build());

        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Событие с id=" + id + " не найдено."));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new DataNotFoundException("Событие должно быть опубликовано");
        }

        statsClient.create(EndpointHit.builder()
                .app("ewm_service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build());

        event.setViews(countViews(request));
        eventRepository.save(event);

        return EventMapper.mapToEventFullDto(event);
    }

    private Long countViews(HttpServletRequest request) {
        List<ViewStats> stats = statsClient.readAll(
                PAST,
                LocalDateTime.now().plusHours(1),
                List.of(request.getRequestURI()),
                true);
        return !stats.isEmpty() ? stats.get(0).getHits() : 0;
    }
}
