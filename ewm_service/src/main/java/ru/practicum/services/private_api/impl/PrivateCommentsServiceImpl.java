package ru.practicum.services.private_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.exceptions.WrongConditionException;
import ru.practicum.helper.UtilsUpdateWithoutNull;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.enums.CommentState;
import ru.practicum.models.enums.State;
import ru.practicum.repositories.CommentRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.private_api.PrivateCommentsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateCommentsServiceImpl implements PrivateCommentsService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto create(Long authorId, Long eventId, NewCommentDto newCommentDto) {
        User author = findUserById(authorId);
        Event event = findEventById(eventId);
        if (!State.PUBLISHED.equals(event.getState())) {
            throw new  WrongConditionException("комментарий можно оставить только к опубликованному событию");
        }

        Comment comment = CommentMapper.mapToComment(newCommentDto, author, event, CommentState.PENDING);

        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {
        findUserById(userId);
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Комментарий с id=" + commentId + " не найден."));

        if (!userId.equals(commentToUpdate.getAuthor().getId())) {
            throw new WrongConditionException("нельзя изменить комментарий другого пользователя");
        }
        if (CommentState.PUBLISHED == commentToUpdate.getState()) {
            throw new WrongConditionException("нельзя изменить опубликованный комментарий");
        }

        UtilsUpdateWithoutNull.copyProperties(newCommentDto, commentToUpdate);
        commentToUpdate.setState(CommentState.PENDING);
        commentToUpdate.setUpdatedOn(LocalDateTime.now());

        return CommentMapper.mapToCommentDto(commentRepository.save(commentToUpdate));
    }

    @Override
    public void deleteByAuthor(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Комментарий с id=" + commentId + " не найден."));
        findUserById(userId);

        if (!userId.equals(comment.getAuthor().getId())) {
            throw new WrongConditionException("только автор может удалить комментарий");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAuthorComments(Long userId, Pageable pageable) {
        findUserById(userId);
        List<Comment> comments = commentRepository.findByAuthorId(userId, pageable);

        if (comments.isEmpty()) {
            return new ArrayList<>();
        }

        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие с id=" + eventId + " не найдено."));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с id=" + userId + " не найден."));
    }
}
