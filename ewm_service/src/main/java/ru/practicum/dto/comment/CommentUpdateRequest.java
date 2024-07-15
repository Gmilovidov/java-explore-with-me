package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.models.enums.CommentAdminStateAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
    private CommentAdminStateAction stateAction;
}
