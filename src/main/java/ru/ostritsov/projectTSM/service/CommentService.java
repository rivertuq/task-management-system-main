package ru.ostritsov.projectTSM.service;

import ru.ostritsov.projectTSM.dto.comment.CommentCreateDto;
import ru.ostritsov.projectTSM.dto.comment.CommentDto;
import ru.ostritsov.projectTSM.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByTaskId(Long taskId);

    CommentDto createComment(Long taskId, CommentCreateDto commentCreateDto, String username);

    void deleteComment(Comment comment);

    String deleteCommentById(Long commentId);
}
