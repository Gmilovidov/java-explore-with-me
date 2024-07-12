package ru.practicum.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.helper.UtilsUpdateWithoutNull;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.admin_api.AdminCompilationsService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.mapToCompilation(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(Long compId) {
        findCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilationToUpdate = findCompilationById(compId);

        UtilsUpdateWithoutNull.copyProperties(updateCompilationRequest, compilationToUpdate);
        compilationToUpdate.setEvents(findEvents(updateCompilationRequest.getEvents()));

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilationToUpdate));
    }

    private List<Event> findEvents(List<Long> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);

        if (eventIds.isEmpty() || events.isEmpty()) {
            return new ArrayList<>();
        }

        return events;
    }

    private Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Компиляция с id=" + compId + " не найдена."));
    }
}
