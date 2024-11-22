package ru.ostritsov.projectTSM.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.ostritsov.projectTSM.dto.comment.CommentDto;
import ru.ostritsov.projectTSM.model.Comment;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .build();

        return  commentDto;
    }

    public List<CommentDto> mapToListDto(List<Comment> commentList) {
        List<CommentDto> list = new ArrayList<>();
        for (Comment comment : commentList) {
            list.add(toCommentDto(comment));
        }

        return list;
    }
}
