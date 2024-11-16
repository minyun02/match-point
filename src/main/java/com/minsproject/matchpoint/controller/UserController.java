package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.response.UserResponse;
import com.minsproject.matchpoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/token")
    @Operation(summary = "토큰으로 사용자 조회")
    @ApiResponse(responseCode = "200", description = "토큰으로 사용자 조회")
    public UserResponse getUserByToken(@RequestParam String token) {
        return UserResponse.fromEntity(userService.getUserByToken(token));
    }

    @GetMapping("/provider")
    @Operation(summary = "provider, providerId로 사용자 조회")
    @ApiResponse(responseCode = "200", description = "provider, providerId로 사용자 조회")
    public UserResponse getUserByProviderId(@RequestParam String provider, @RequestParam String providerId) {
        return UserResponse.fromEntity(userService.getUserByProviderId(provider, providerId));
    }
}