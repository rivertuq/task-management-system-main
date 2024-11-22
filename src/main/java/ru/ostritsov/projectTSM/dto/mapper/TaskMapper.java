package ru.ostritsov.projectTSM.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.ostritsov.projectTSM.dto.task.TaskDto;
import ru.ostritsov.projectTSM.dto.task.TaskDtoWithComments;
import ru.ostritsov.projectTSM.model.Comment;
import ru.ostritsov.projectTSM.model.Task;

import java.util.List;

@UtilityClass
public class TaskMapper {
    public TaskDto toTaskDto(Task task) {
        TaskDto taskDto = TaskDto.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserMapper.toUserDto(task.getAuthor()))
                .executor(UserMapper.toUserDto(task.getExecutor()))
                .build();
        return taskDto;
    }

    public TaskDtoWithComments toTaskDtoComments(Task task, List<Comment> comments) {
        TaskDtoWithComments taskDto = TaskDtoWithComments.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserMapper.toUserDto(task.getAuthor()))
                .executor(UserMapper.toUserDto(task.getExecutor()))
                .comments(CommentMapper.mapToListDto(comments))
                .build();

        return taskDto;
    }
}
