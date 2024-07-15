package ru.practicum.controllers.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequest;
import ru.practicum.services.admin_api.AdminCommentsService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateState(@PathVariable Long commentId,
                                                 @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.ok().body(adminCommentsService.updateState(commentId, commentUpdateRequest));
    }

    @DeleteMapping("/{commentId}")
    public  ResponseEntity<Void> delete(@PathVariable Long commentId) {
        adminCommentsService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}