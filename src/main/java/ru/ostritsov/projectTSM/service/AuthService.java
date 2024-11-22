package ru.ostritsov.projectTSM.service;

import ru.ostritsov.projectTSM.dto.jwt.JwtRequest;
import ru.ostritsov.projectTSM.dto.jwt.JwtResponse;

public interface AuthService {
    JwtResponse createAuthToken(JwtRequest authRequest);
}
