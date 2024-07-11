package ru.practicum.dto.request;


import lombok.*;
import ru.practicum.models.enums.Status;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}
