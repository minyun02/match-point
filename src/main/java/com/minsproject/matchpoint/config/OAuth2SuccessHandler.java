package com.minsproject.matchpoint.config;

import com.minsproject.matchpoint.config.jwt.JwtTokenProvider;
import com.minsproject.matchpoint.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String provider = jwtTokenProvider.getProviderFromAuthentication((OAuth2AuthenticationToken) authentication);
        DefaultOAuth2User oAuth2User = jwtTokenProvider.getPrincipalFromAuthentication(authentication);
        String email = jwtTokenProvider.getEmailByProvider(provider, oAuth2User.getAttributes());
        String token = jwtTokenProvider.generateAccessToken(email, provider);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, provider);
        tokenService.saveOrUpdate(email, provider, refreshToken, token);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
        response.setStatus(SC_OK);
    }
}
