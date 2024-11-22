package ru.ostritsov.projectTSM.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ostritsov.projectTSM.dto.jwt.JwtRequest;
import ru.ostritsov.projectTSM.dto.jwt.JwtResponse;
import ru.ostritsov.projectTSM.exception.AppError;
import ru.ostritsov.projectTSM.util.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            ));
        } catch (BadCredentialsException exception) {
            throw new AppError("Некорректный логин/пароль!");
        }
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }
}
