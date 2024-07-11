package ru.practicum.services.private_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.exceptions.WrongConditionException;
import ru.practicum.helper.UtilsUpdateWithoutNull;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.models.*;
import ru.practicum.models.enums.State;
import ru.practicum.models.enums.Status;
import ru.practicum.models.enums.UserStateAction;
import ru.practicum.repositories.*;
import ru.practicum.services.private_api.PrivateEventsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getUserEvets(Long initiatorId, Pageable pageable) {
        findUserById(initiatorId);
        return eventRepository.findAllByInitiator_Id(initiatorId, pageable).stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long initiatorId, NewEventDto newEventDto) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        checkDate(eventDate);
        User initiator = findUserById(initiatorId);
        Category category = findCategoryById(newEventDto.getCategory());
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = EventMapper.mapToEvent(newEventDto, category, location, initiator, eventDate,
                State.PENDING, 0L, 0L);
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventById(Long initiatorId, Long eventId) {
        findUserById(initiatorId);
        return EventMapper.mapToEventFullDto(findEventById(eventId));
    }

    @Override
    public EventFullDto updateEvent(Long initiatorId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        findUserById(initiatorId);
        Event eventToUpdate = findEventById(eventId);
        LocalDateTime newEventDate = updateEventUserRequest.getEventDate();

        if (newEventDate != null) {
            checkDate(newEventDate);
        }

        if (eventToUpdate.getState().equals(State.PUBLISHED)) {
            throw new WrongConditionException("изменить можно только отмененные "
                    + "события или события в состоянии ожидания модерации");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(UserStateAction.CANCEL_REVIEW)) {
                eventToUpdate.setState(State.CANCELED);
            } else {
                eventToUpdate.setState(State.PENDING);
            }
        }

        UtilsUpdateWithoutNull.copyProperties(updateEventUserRequest, eventToUpdate);

        return EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long initiatorId, Long eventId) {
        findUserById(initiatorId);
        List<Request> requests = requestRepository.findAllByEvent_Id(eventId).stream()
                .filter(request -> request.getEvent().getInitiator().getId().equals(initiatorId))
                .collect(Collectors.toList());
        if (requests.isEmpty()) {
            return new ArrayList<>();
        }
        return requests.stream()
                .map(RequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long initiatorId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        findUserById(initiatorId);
        Event event = findEventById(eventId);
        List<Request> requestsToUpdate = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());

        if (requestsToUpdate.stream()
                .anyMatch(request -> !request.getStatus().equals(Status.PENDING))) {
            throw new WrongConditionException("статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (eventRequestStatusUpdateRequest.getStatus().equals(Status.REJECTED)) {
            rejectedRequests = requestsToUpdate.stream()
                    .map(request -> {
                        request.setStatus(Status.REJECTED);
                        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(request));
                    }).collect(Collectors.toList());
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(Status.CONFIRMED)) {
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                confirmedRequests = requestsToUpdate.stream()
                        .map(request -> {
                            request.setStatus(Status.CONFIRMED);
                            return RequestMapper.mapToParticipationRequestDto(requestRepository.save(request));
                        }).collect(Collectors.toList());
            } else if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new WrongConditionException("нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие");
            }

            for (Request request : requestsToUpdate) {
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(Status.CONFIRMED);
                    confirmedRequests.add(RequestMapper.mapToParticipationRequestDto(request));
                } else {
                    request.setStatus(Status.REJECTED);
                    rejectedRequests.add(RequestMapper.mapToParticipationRequestDto(request));
                }
                requestRepository.save(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
        }

        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие с id=" + eventId + " не найдено."));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с id=" + userId + " не найден."));
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Категория с id=" + catId + " не найдена."));
    }

    private void checkDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidRequestException("Дата события не может быть раньше, чем через два часа от текущего момента");
        }
    }
}
