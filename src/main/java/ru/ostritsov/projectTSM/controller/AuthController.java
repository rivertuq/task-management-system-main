package ru.ostritsov.projectTSM.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ostritsov.projectTSM.dto.jwt.JwtRequest;
import ru.ostritsov.projectTSM.dto.user.RegistrationUserDto;
import ru.ostritsov.projectTSM.service.AuthServiceImpl;
import ru.ostritsov.projectTSM.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userServiceImpl;
    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/authorization")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authServiceImpl.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.ok(userServiceImpl.createUser(registrationUserDto));
    }
}
