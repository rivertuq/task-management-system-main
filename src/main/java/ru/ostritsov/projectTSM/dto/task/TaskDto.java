package ru.ostritsov.projectTSM.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.ostritsov.projectTSM.dto.user.UserDto;
import ru.ostritsov.projectTSM.model.Priority;
import ru.ostritsov.projectTSM.model.Status;

@Data
@Builder
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String header;
    private String description;
    private Status status;
    private Priority priority;
    private UserDto author;
    private UserDto executor;
}
