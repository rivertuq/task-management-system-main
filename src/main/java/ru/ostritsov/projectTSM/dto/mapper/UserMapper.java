package ru.ostritsov.projectTSM.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.ostritsov.projectTSM.dto.user.UserDto;
import ru.ostritsov.projectTSM.model.User;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return  userDto;
    }
}
