package com.minsproject.matchpoint.config.jwt;

import com.minsproject.matchpoint.dto.response.UserResponse;
import com.minsproject.matchpoint.service.UserService;
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

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header. header is null or invalid {}", request.getRequestURL());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.split(" ")[1].trim();

            if (!jwtTokenProvider.validateToken(token)) {
                log.error("Invalid token");
                response.setStatus(UNAUTHORIZED.value());
                return;
            }
            String email = jwtTokenProvider.getClaimsEmail(token);
            String provider = jwtTokenProvider.getClaimsProvider(token);
            UserResponse user = UserResponse.fromEntity(userService.loadUserByEmailAndProvider(email, provider));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e) {
            log.error("Error occurs while validating {}", e.toString());
            response.setStatus(UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}