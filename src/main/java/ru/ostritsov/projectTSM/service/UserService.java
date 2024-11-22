package ru.ostritsov.projectTSM.service;

import ru.ostritsov.projectTSM.dto.user.RegistrationUserDto;
import ru.ostritsov.projectTSM.dto.user.UserDto;
import ru.ostritsov.projectTSM.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    UserDto createUser(RegistrationUserDto registrationUserDto);

    UserDto getInfo(String username);
}
