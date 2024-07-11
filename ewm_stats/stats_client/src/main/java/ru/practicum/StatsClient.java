package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    private final RestTemplate rest;
    private final String serverUrl;
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";

    public StatsClient(@Value("${stats_server.url}") String serverUrl) {
        this.rest = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    public void create(EndpointHit endpointHit) {
        rest.exchange(serverUrl + API_PREFIX_HIT,
                HttpMethod.POST,
                new HttpEntity<>(endpointHit),
                Object.class);
    }

    public List<ViewStats> readAll(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(Constants.FORMATTER),
                "end", end.format(Constants.FORMATTER),
                "uris", uris.toArray(),
                "unique", unique
        );

        ResponseEntity<List<ViewStats>> response = rest
                .exchange(serverUrl + API_PREFIX_STATS + "?start={start}&end={end}&uris={uris}&unique={unique}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        parameters);

        return response.getBody();
    }
}
