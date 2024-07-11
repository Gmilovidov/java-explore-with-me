package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.mapper.StatMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;


    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        return StatMapper.mapToHit(statRepository.save(StatMapper.mapToStat(endpointHit)));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidRequestException("Некорректно заданы даты");
        }

        List<ViewStats> stats;
        if (uris == null && !unique) {
            stats = statRepository.getAllStats(start, end);
        } else if (!unique) {
            stats = statRepository.getStatsWithUris(start, end, uris);
        } else  if (uris == null) {
            stats = statRepository.getStatsWithUnique(start, end);
        } else {
            stats = statRepository.getStatsWithUniqueAndUris(start, end, uris);
        }

        return stats;
    }
}
