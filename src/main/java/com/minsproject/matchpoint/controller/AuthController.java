package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.TokenRefreshRequest;
import com.minsproject.matchpoint.dto.response.TokenResponse;
import com.minsproject.matchpoint.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/token")
    @Operation(summary = "토큰 재발급")
    @ApiResponse(responseCode = "200", description = "새로운 token, refreshToken을 반환한다.")
    public TokenResponse refreshToken(@RequestBody TokenRefreshRequest request) {
        return tokenService.generateToken(request.getEmail(), request.getProvider());
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신")
    @ApiResponse(responseCode = "200", description = "갱신된 token, refreshToken을 반환한다.")
    public TokenResponse refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }
}
