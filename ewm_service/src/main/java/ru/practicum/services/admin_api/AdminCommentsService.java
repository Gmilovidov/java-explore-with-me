package ru.practicum.services.admin_api;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequest;

public interface AdminCommentsService {
    CommentDto updateState(Long commentId, CommentUpdateRequest commentUpdateRequest);

    void delete(Long commentId);
}
