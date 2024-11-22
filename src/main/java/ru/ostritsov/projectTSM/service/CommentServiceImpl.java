package ru.ostritsov.projectTSM.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ostritsov.projectTSM.dto.comment.CommentCreateDto;
import ru.ostritsov.projectTSM.dto.comment.CommentDto;
import ru.ostritsov.projectTSM.dto.mapper.CommentMapper;
import ru.ostritsov.projectTSM.exception.NotFoundException;
import ru.ostritsov.projectTSM.exception.ValidationException;
import ru.ostritsov.projectTSM.model.Comment;
import ru.ostritsov.projectTSM.model.Task;
import ru.ostritsov.projectTSM.model.User;
import ru.ostritsov.projectTSM.repository.CommentRepository;
import ru.ostritsov.projectTSM.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final TaskRepository taskRepository;

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public CommentDto createComment(Long taskId, CommentCreateDto commentCreateDto, String username) {
        if (commentCreateDto.getText().isBlank() || commentCreateDto.getText().isEmpty()) {
            throw new ValidationException("Чтобы оставить комментарий, надо что-то написать!");
        }
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new NotFoundException("Такой задачи не существует!");
        }
        Task task = taskOptional.get();
        User user = userService.findByUsername(username).get();
        Comment comment = Comment.builder()
                .author(user)
                .text(commentCreateDto.getText())
                .task(task)
                .build();
        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.deleteById(comment.getId());
    }

    @Override
    public String deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
        return "Success";
    }
}
