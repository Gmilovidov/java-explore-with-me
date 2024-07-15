package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.Comment;
import ru.practicum.models.enums.CommentState;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId, Pageable pageable);

    Page<Comment> findAllByEventIdAndState(Long eventId, CommentState state, Pageable pageable);
}
