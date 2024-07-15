package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.enums.CommentState;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment mapToComment(NewCommentDto newCommentDto, User author, Event event, CommentState state) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .createdOn(LocalDateTime.now())
                .author(author)
                .event(event)
                .state(state)
                .build();
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .publishedOn(comment.getPublishedOn())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .state(comment.getState())
                .build();
    }
}
