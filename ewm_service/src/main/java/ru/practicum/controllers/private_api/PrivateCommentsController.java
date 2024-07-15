package ru.practicum.controllers.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.services.private_api.PrivateCommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class PrivateCommentsController {
    private final PrivateCommentsService privateCommentsService;


    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @Valid @RequestBody NewCommentDto newCommentDto) {
        return new ResponseEntity<>(privateCommentsService.create(userId, eventId, newCommentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long userId,
                             @PathVariable Long commentId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.ok().body(privateCommentsService.update(userId, commentId, newCommentDto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        privateCommentsService.deleteByAuthor(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAuthorComments(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return ResponseEntity.ok().body(privateCommentsService.getAuthorComments(userId, pageable));
    }
}