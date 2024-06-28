package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        return statsMapper.statToHit(statsRepository.save(statsMapper.hitToStat(endpointHit)));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
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
