package ru.ostritsov.projectTSM.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.ostritsov.projectTSM.dto.user.UserDto;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private UserDto author;
}
