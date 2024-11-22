package ru.ostritsov.projectTSM.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ostritsov.projectTSM.dto.mapper.TaskMapper;
import ru.ostritsov.projectTSM.dto.task.*;
import ru.ostritsov.projectTSM.exception.ConflictException;
import ru.ostritsov.projectTSM.exception.ForbiddenException;
import ru.ostritsov.projectTSM.exception.NotFoundException;
import ru.ostritsov.projectTSM.exception.ValidationException;
import ru.ostritsov.projectTSM.model.*;
import ru.ostritsov.projectTSM.repository.TaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserServiceImpl userService;
    private final CommentServiceImpl commentService;

    @Override
    public Task findById(Long taskId) {
        return existTask(taskId);
    }

    @Override
    public TaskDto createTask(String username, TaskCreateDto taskDto) {
        validateStatus(taskDto);
        validatePriority(taskDto);
        User user = existUserByUsername(username);
        Task task = Task.builder()
                .header(taskDto.getHeader())
                .description(taskDto.getDescription())
                .status(Status.valueOf(taskDto.getStatus()))
                .priority(Priority.valueOf(taskDto.getPriority()))
                .author(user)
                .executor(null)
                .build();
        taskRepository.save(task);

        return TaskMapper.toTaskDto(task);
    }

    @Override
    public TaskDto updateTask(TaskCreateDto taskCreateDto, Long taskId, String username) {
        Task task = existTask(taskId);
        authorMatch(username, task.getAuthor().getId());
        if (taskCreateDto.getStatus() != null) {
            validateStatus(taskCreateDto);
            task.setStatus(Status.valueOf(taskCreateDto.getStatus()));
        }
        if (taskCreateDto.getPriority() != null) {
            validatePriority(taskCreateDto);
            task.setPriority(Priority.valueOf(taskCreateDto.getPriority()));
        }
        if (taskCreateDto.getHeader() != null) {
            task.setHeader(taskCreateDto.getHeader());
        }
        if (taskCreateDto.getDescription() != null) {
            task.setDescription(taskCreateDto.getDescription());
        }
        taskRepository.save(task);

        return TaskMapper.toTaskDto(task);
    }

    @Override
    public TaskDto updateExecutor(TaskUpdateExecutor taskUpdateExecutor, Long taskId, String username) {
        Task task = existTask(taskId);
        authorMatch(username, task.getAuthor().getId());
        User user = existUserById(taskUpdateExecutor.getExecutorId());
        User executor = task.getExecutor();
        if (!Objects.isNull(executor) && user.equals(executor)) {
            throw new ConflictException("Данный пользователь уже стоит как испольнитель задачи!");
        }
        task.setExecutor(user);
        taskRepository.save(task);

        return TaskMapper.toTaskDto(task);
    }

    public TaskDto updateStatus(TaskUpdateStatus taskUpdateStatus, Long taskId, String username) {
        Task task = existTask(taskId);
        authorMatch(username, task.getAuthor().getId());
        task.setStatus(Status.valueOf(taskUpdateStatus.getStatus()));
        taskRepository.save(task);

        return TaskMapper.toTaskDto(task);
    }

    public TaskDto updateStatusByExecutor(TaskUpdateStatus taskUpdateStatus, Long taskId, String username) {
        Task task = existTask(taskId);
        User user = userService.findByUsername(username).get();
        if (!Objects.equals(user.getId(), task.getExecutor().getId())) {
            throw new ForbiddenException("Нет прав на изменение статуса задачи!");
        }
        task.setStatus(Status.valueOf(taskUpdateStatus.getStatus()));
        taskRepository.save(task);

        return TaskMapper.toTaskDto(task);
    }

    @Override
    public String deleteTaskById(Long taskId) {
        existTask(taskId);
        for (Comment comment : commentService.findByTaskId(taskId)) {
            commentService.deleteComment(comment);
        }
        taskRepository.deleteById(taskId);
        return "Success";
    }

    @Override
    public TaskDto taskView(Long taskId) {
        Task task = existTask(taskId);

        return TaskMapper.toTaskDto(task);
    }

    public List<TaskDtoWithComments> viewUserTasks(String username, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        Long userId = existUserByUsername(username).getId();
        List<Task> tasks = taskRepository.findByAuthorId(userId, page);

        return tasks.stream()
                .map(task -> TaskMapper.toTaskDtoComments(task, commentService.findByTaskId(task.getId())))
                .collect(Collectors.toList());
    }

    public List<TaskDtoWithComments> viewTasksByUserId(Long userId, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        existUserById(userId);
        List<Task> tasks = taskRepository.findByAuthorId(userId, page);

        return tasks.stream()
                .map(task -> TaskMapper.toTaskDtoComments(task, commentService.findByTaskId(task.getId())))
                .collect(Collectors.toList());
    }

    private User existUserByUsername(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует!");
        }

        return userOptional.get();
    }

    private User existUserById(Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует!");
        }

        return userOptional.get();
    }

    private Task existTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new NotFoundException("Такой задачи не существует!");
        }

        return taskOptional.get();
    }

    private void authorMatch(String username, Long authorId) {
        User user = userService.findByUsername(username).get();
        if (!Objects.equals(user.getId(), authorId)) {
            throw new ForbiddenException("Нет прав на изменение задачи!");
        }
    }

    private void validateStatus(TaskCreateDto taskDto) {
        String status = taskDto.getStatus();
        if (!Status.valueOf(status).equals(Status.PENDING) &&
                !Status.valueOf(status).equals(Status.IN_PROGRESS) &&
                    !Status.valueOf(status).equals(Status.DONE)) {
            throw new ValidationException("Неверно указан статус задачи!" +
                    "Допустимые значения: 'PENDING', 'IN_PROGRESS', 'DONE'.");
        }
    }

    private void validatePriority(TaskCreateDto taskDto) {
        String priority = taskDto.getPriority();
        if (Priority.valueOf(priority).equals(Priority.HIGH) &&
                Priority.valueOf(priority).equals(Priority.MEDIUM) &&
                Priority.valueOf(priority).equals(Priority.LOW)) {
            throw new ValidationException("Неверно указан приоритет задачи!" +
                    "Допустимые значения: 'HIGH', 'MEDIUM', 'LOW'.");
        }
    }
}
