package ru.practicum;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
