package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.response.SportResponse;
import com.minsproject.matchpoint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/sports/{userId}")
    @Operation(summary = "사용자의 운동 프로필 조회")
    @ApiResponse(responseCode = "200", description = "해당 사용자의 운동 종목을 조회한다.")
    public List<SportResponse> getSports(@PathVariable Long userId) {
        return userService.getSportsById(userId);
    }
}
