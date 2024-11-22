package ru.ostritsov.projectTSM.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ostritsov.projectTSM.dto.user.UserDto;
import ru.ostritsov.projectTSM.service.UserServiceImpl;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final UserServiceImpl userService;

    @GetMapping("/")
    public String homePage() {
        return "Welcome to the home page!";
    }

    @GetMapping("/admin")
    public String adminData() {
        return "Admin data";
    }

    @GetMapping("/info")
    public UserDto userData(Principal principal) {
        return userService.getInfo(principal.getName());
    }
}
