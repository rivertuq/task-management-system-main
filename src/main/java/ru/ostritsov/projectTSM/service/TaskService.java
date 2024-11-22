package ru.ostritsov.projectTSM.service;

import ru.ostritsov.projectTSM.dto.task.*;
import ru.ostritsov.projectTSM.model.Task;

import java.util.List;

public interface TaskService {
    Task findById(Long taskId);

    TaskDto createTask(String username, TaskCreateDto taskDto);

    TaskDto updateTask(TaskCreateDto taskDto, Long taskId, String username);

    TaskDto updateExecutor(TaskUpdateExecutor taskUpdateExecutor, Long taskId, String username);

    TaskDto updateStatus(TaskUpdateStatus taskUpdateStatus, Long taskId, String username);

    TaskDto updateStatusByExecutor(TaskUpdateStatus taskUpdateStatus, Long taskId, String username);

    String deleteTaskById(Long taskId);

    TaskDto taskView(Long taskId);

    List<TaskDtoWithComments> viewUserTasks(String username, Integer from, Integer size);

    List<TaskDtoWithComments> viewTasksByUserId(Long userId, Integer from, Integer size);
}
