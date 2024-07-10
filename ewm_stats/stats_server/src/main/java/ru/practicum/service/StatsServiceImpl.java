package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.exceptions.InvalidRequestException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;


    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        return StatsMapper.mapToHit(statsRepository.save(StatsMapper.mapToStat(endpointHit)));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidRequestException("Некорректно заданы даты");
        }

        List<ViewStats> stats;
        if (uris == null && !unique) {
            stats = statsRepository.getAllStats(start, end);
        } else if (!unique) {
            stats = statsRepository.getStatsWithUris(start, end, uris);
        } else  if (uris == null) {
            stats = statsRepository.getStatsWithUnique(start, end);
        } else {
            stats = statsRepository.getStatsWithUniqueAndUris(start, end, uris);
        }

        return stats;
    }
}