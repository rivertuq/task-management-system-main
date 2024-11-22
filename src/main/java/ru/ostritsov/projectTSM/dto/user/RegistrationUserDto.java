package ru.ostritsov.projectTSM.dto.user;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
