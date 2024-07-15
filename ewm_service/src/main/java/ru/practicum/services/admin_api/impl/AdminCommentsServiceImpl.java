package ru.practicum.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequest;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.enums.CommentAdminStateAction;
import ru.practicum.models.enums.CommentState;
import ru.practicum.repositories.CommentRepository;
import ru.practicum.services.admin_api.AdminCommentsService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {
    private final CommentRepository commentRepository;

    @Override
    public CommentDto updateState(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Комментарий с id=" + commentId + " не найден."));

        if (commentUpdateRequest.getStateAction() == CommentAdminStateAction.PUBLISH_COMMENT) {
            comment.setState(CommentState.PUBLISHED);
            comment.setPublishedOn(LocalDateTime.now());
        } else {
            comment.setState(CommentState.CANCELED);
        }
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Комментарий с id=" + commentId + " не найден."));
        commentRepository.deleteById(commentId);
    }
}
