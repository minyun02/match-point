package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.SignupRequest;
import com.minsproject.matchpoint.dto.response.TokenResponse;
import com.minsproject.matchpoint.dto.response.UserResponse;
import com.minsproject.matchpoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/token")
    @Operation(summary = "토큰으로 사용자 조회")
    @ApiResponse(responseCode = "200", description = "토큰으로 사용자 조회")
    public UserResponse getUserByToken(@RequestParam String token) {
        return UserResponse.fromEntity(userService.getUserInfoByToken(token));
    }

    @GetMapping(value = "/provider")
    @Operation(summary = "provider, providerId로 사용자 조회")
    @ApiResponse(responseCode = "200", description = "provider, providerId로 사용자 조회")
    public UserResponse getUserByProviderId(@RequestParam String provider, @RequestParam String providerId) {
        return UserResponse.fromEntity(userService.getUserByProviderId(provider, providerId));
    }

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입 - 사용자 및 초기 스포츠 프로필 등록")
    @ApiResponse(responseCode = "200", description = "회원가입에 성공하면 토큰 반환")
    public TokenResponse signup(
            @RequestPart("request") SignupRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        return userService.signup(request, profileImage);
    }
}