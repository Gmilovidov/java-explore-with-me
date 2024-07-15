package ru.practicum.services.public_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.enums.CommentState;
import ru.practicum.repositories.CommentRepository;
import ru.practicum.services.public_api.PublicCommentsService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCommentsServiceImpl implements PublicCommentsService {
    private final CommentRepository commentRepository;

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Комментарий с id=" + commentId + " не найден."));

        if (comment.getState() != CommentState.PUBLISHED) {
            throw new DataNotFoundException("Комментарий должен быть опубликован");
        }
        return CommentMapper.mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, Pageable pageable) {
        List<Comment> comments = commentRepository.findAllByEventIdAndState(eventId, CommentState.PUBLISHED, pageable).getContent();

        if (comments.isEmpty()) {
            return new ArrayList<>();
        }

        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }
}
