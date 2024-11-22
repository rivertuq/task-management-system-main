package ru.ostritsov.projectTSM.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ostritsov.projectTSM.dto.task.*;
import ru.ostritsov.projectTSM.service.TaskServiceImpl;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskServiceImpl taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(Principal principal,
                                              @RequestBody TaskCreateDto taskCreateDto) {
        return ResponseEntity.created(URI.create("http://localhost:8080/task/create)"))
                .body(taskService.createTask(principal.getName(), taskCreateDto));
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId,
                                              @RequestBody TaskCreateDto taskCreateDto,
                                              Principal principal) {
        return ResponseEntity.ok().body(taskService.updateTask(taskCreateDto, taskId, principal.getName()));
    }

    @PutMapping("/update-executor/{taskId}")
    public ResponseEntity<TaskDto> updateExecutor(@PathVariable Long taskId,
                                                  @RequestBody TaskUpdateExecutor taskUpdateExecutor,
                                                  Principal principal) {
        return ResponseEntity.ok().body(taskService.updateExecutor(taskUpdateExecutor, taskId, principal.getName()));
    }

    @PutMapping("/update-status/{taskId}")
    public ResponseEntity<TaskDto> updateStatus(@PathVariable Long taskId,
                                                @RequestBody TaskUpdateStatus taskUpdateStatus,
                                                Principal principal) {
        return ResponseEntity.ok().body(taskService.updateStatus(taskUpdateStatus, taskId, principal.getName()));
    }

    @PutMapping("/update-status-by-executor/{taskId}")
    public ResponseEntity<TaskDto> updateStatusByExecutor(@PathVariable Long taskId,
                                                          @RequestBody TaskUpdateStatus taskUpdateStatus,
                                                          Principal principal) {
        return ResponseEntity.ok().body(taskService.updateStatusByExecutor(taskUpdateStatus, taskId, principal.getName()));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.deleteTaskById(taskId));
    }

    @GetMapping("/view/{taskId}")
    public ResponseEntity<TaskDto> taskView(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.taskView(taskId));
    }

    @Validated
    @GetMapping("/view-user-tasks")
    public ResponseEntity<List<TaskDtoWithComments>> viewUserTasks(Principal principal,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                       @Min(0) Integer from,
                                                       @RequestParam(required = false, defaultValue = "10")
                                                       @Min(0) Integer size) {
        return ResponseEntity.ok().body(taskService.viewUserTasks(principal.getName(), from, size));
    }

    @Validated
    @GetMapping("/view-user-tasks/{userId}")
    public ResponseEntity<List<TaskDtoWithComments>> viewTasksByUserId(@PathVariable Long userId,
                                                           @RequestParam(required = false, defaultValue = "0")
                                                           @Min(0) Integer from,
                                                           @RequestParam(required = false, defaultValue = "10")
                                                           @Min(0) Integer size) {
        return ResponseEntity.ok().body(taskService.viewTasksByUserId(userId, from, size));
    }
}
