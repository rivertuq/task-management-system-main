package ru.ostritsov.projectTSM.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.ostritsov.projectTSM.util.JwtTokenUtils;

import java.io.IOException;
import java.security.SignatureException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Извлекаем токен из заголовка Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token has expired: {}", e.getMessage());
            } catch (io.jsonwebtoken.JwtException e) {
                log.error("JWT Token signature invalid: {}", e.getMessage());
            } catch (Exception e) {
                log.error("JWT Token error: {}", e.getMessage());
            }
        }

        // Проверяем токен и добавляем пользователя в SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorities = jwtTokenUtils.getRoles(jwt).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Передаем управление следующему фильтру
        filterChain.doFilter(request, response);
    }
}
