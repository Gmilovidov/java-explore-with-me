package ru.practicum.handler;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;

    private String reason;

    private String message;

    private String timestamp;
}