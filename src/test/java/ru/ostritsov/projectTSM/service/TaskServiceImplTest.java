package ru.ostritsov.projectTSM.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ostritsov.projectTSM.dto.mapper.TaskMapper;
import ru.ostritsov.projectTSM.dto.task.*;
import ru.ostritsov.projectTSM.model.*;
import ru.ostritsov.projectTSM.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private  TaskRepository taskRepository;
    @Mock
    private  UserServiceImpl userService;
    @Mock
    private  CommentServiceImpl commentService;

    @InjectMocks
    private TaskServiceImpl taskService;
    private User user;
    private User user1;
    private Task task;
    private Task task1;
    private TaskCreateDto taskCreateDto;

    @BeforeEach
    void set() {
        user = User.builder()
                .id(3L)
                .username("Steave")
                .password("$2a$12$HMERlUcLl6FVYFA8Z7PyFuYIN07LdHsaE0bXhRvcE6dO710TBzOZC")
                .email("123@mail.ru")
                .roles(List.of(new Role(1, "POLE_USER")))
                .build();

        user1 = User.builder()
                .id(4L)
                .username("Robert")
                .password("$2a$12$HMERlUcLl6FVYFA8Z7PyFuYIN07LdHsaE0bXhRvcE6dO710TBzOZC")
                .email("qwerty@gmail.com")
                .roles(List.of(new Role(1, "POLE_USER")))
                .build();

        taskCreateDto = TaskCreateDto.builder()
                .header("Сделать")
                .description("Описание")
                .status("PENDING")
                .priority("MEDIUM")
                .build();

        task = Task.builder()
                .header(taskCreateDto.getHeader())
                .description(taskCreateDto.getDescription())
                .status(Status.valueOf(taskCreateDto.getStatus()))
                .priority(Priority.valueOf(taskCreateDto.getPriority()))
                .author(user)
                .executor(null)
                .build();

        task1 = Task.builder()
                .id(1L)
                .header(taskCreateDto.getHeader())
                .description(taskCreateDto.getDescription())
                .status(Status.valueOf(taskCreateDto.getStatus()))
                .priority(Priority.valueOf(taskCreateDto.getPriority()))
                .author(user)
                .executor(null)
                .build();
    }

    @Test
    void createTask() {
        String username = user.getUsername();

        when(userService.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.save(task)).thenReturn(task);

        TaskDto actualTaskDto = taskService.createTask(username, taskCreateDto);
        actualTaskDto.setId(1L);

        assertNotNull(taskCreateDto);
        assertEquals(actualTaskDto.getId(), 1L);
        assertEquals(actualTaskDto.getHeader(), "Сделать то-то");
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask() {
        TaskCreateDto taskCreateDto1 = taskCreateDto;
        taskCreateDto1.setHeader("Очень Важно");
        Long taskId = 1L;
        String username = user.getUsername();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));
        task.setHeader(taskCreateDto1.getHeader());
        when(taskRepository.save(task1)).thenReturn(task1);
        when(userService.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        TaskDto actualTaskDto = taskService.updateTask(taskCreateDto1, taskId, username);

        assertNotNull(taskCreateDto1);
        assertEquals(actualTaskDto.getId(), 1L);
        assertEquals(actualTaskDto.getHeader(), "Очень Важно");
        verify(taskRepository).save(task1);
    }

    @Test
    void updateExecutor() {
        TaskUpdateExecutor taskUpdateExecutor = new TaskUpdateExecutor(4L);
        Long taskId = 1L;
        String username = user.getUsername();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));
        task1.setExecutor(user1);
        when(taskRepository.save(task1)).thenReturn(task1);
        when(userService.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        when(userService.findById(taskUpdateExecutor.getExecutorId())).thenReturn(Optional.ofNullable(user1));

        task1.setExecutor(null);
        TaskDto actualTaskDto = taskService.updateExecutor(taskUpdateExecutor, taskId, username);

        assertNotNull(taskUpdateExecutor);
        assertEquals(actualTaskDto.getExecutor().getUsername(), user1.getUsername());
        verify(taskRepository).save(task1);
    }

    @Test
    void updateStatus() {
        TaskUpdateStatus taskUpdateStatus = new TaskUpdateStatus("IN_PROGRESS");
        Long taskId = 1L;
        String username = user.getUsername();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));
        task.setStatus(Status.valueOf(taskUpdateStatus.getStatus()));
        when(taskRepository.save(task1)).thenReturn(task1);
        when(userService.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        task1.setStatus(Status.PENDING);
        TaskDto actualTaskDto = taskService.updateStatus(taskUpdateStatus, taskId, username);

        assertNotNull(taskUpdateStatus);
        assertEquals(actualTaskDto.getStatus(), Status.valueOf(taskUpdateStatus.getStatus()));
        verify(taskRepository).save(task1);
    }

    @Test
    void updateStatusByExecutor() {
        TaskUpdateStatus taskUpdateStatus = new TaskUpdateStatus("DONE");
        Long taskId = 1L;
        String username = user.getUsername();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));
        task.setStatus(Status.valueOf(taskUpdateStatus.getStatus()));
        when(taskRepository.save(task1)).thenReturn(task1);
        when(userService.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        task1.setStatus(Status.IN_PROGRESS);
        TaskDto actualTaskDto = taskService.updateStatus(taskUpdateStatus, taskId, username);

        assertNotNull(taskUpdateStatus);
        assertEquals(actualTaskDto.getStatus(), Status.valueOf(taskUpdateStatus.getStatus()));
        verify(taskRepository).save(task1);
    }

    @Test
    void deleteTaskById() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));
        when(commentService.findByTaskId(taskId)).thenReturn(new ArrayList<>());

        String success = taskService.deleteTaskById(taskId);

        assertEquals(success, "Success");
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void taskView() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task1));

        TaskDto actualTaskDto = taskService.taskView(taskId);

        assertEquals(actualTaskDto, TaskMapper.toTaskDto(task1));
    }
}