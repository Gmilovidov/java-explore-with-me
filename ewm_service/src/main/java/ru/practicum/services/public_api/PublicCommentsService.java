package ru.practicum.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface PublicCommentsService {
    CommentDto getCommentById(Long commentId);

    List<CommentDto> getEventComments(Long eventId, Pageable pageable);
}
