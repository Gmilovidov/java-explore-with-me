package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.EndpointHit;
import ru.practicum.model.Stat;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    Stat hitToStat(EndpointHit endpointHit);

    EndpointHit statToHit(Stat stat);
}
