package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStats;
import ru.practicum.models.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
    "FROM Stat s " +
    "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
    "GROUP BY s.app, s.uri " +
    "ORDER BY COUNT(s) DESC")
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s) DESC")
    List<ViewStats> getStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s) DESC")
    List<ViewStats> getStatsWithUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s) DESC")
    List<ViewStats> getStatsWithUniqueAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
