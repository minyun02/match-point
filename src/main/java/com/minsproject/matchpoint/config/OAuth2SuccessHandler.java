package com.minsproject.matchpoint.config;

import com.minsproject.matchpoint.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        jwtTokenProvider.generateRefreshToken(authentication, accessToken);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\":\"" + accessToken + "\"}");
        response.setStatus(SC_OK);
    }
}
