package ru.practicum.services.private_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface PrivateCommentsService {
    CommentDto create(Long authorId, Long eventId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto);

    void deleteByAuthor(Long userId, Long commentId);

    List<CommentDto> getAuthorComments(Long userId, Pageable pageable);
}
