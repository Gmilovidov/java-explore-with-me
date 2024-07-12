package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.Constants;
import ru.practicum.EndpointHit;
import ru.practicum.models.Stat;

import java.time.LocalDateTime;

@UtilityClass
public class StatMapper {

    public Stat mapToStat(EndpointHit endpointHit) {
        return Stat.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), Constants.FORMATTER))
                .build();
    }

    public EndpointHit mapToHit(Stat stat) {
        return EndpointHit.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .ip(stat.getIp())
                .uri(stat.getUri())
                .timestamp(stat.getTimestamp().toString())
                .build();
    }

}
