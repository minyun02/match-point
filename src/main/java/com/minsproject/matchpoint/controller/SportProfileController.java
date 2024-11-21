package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.service.SportProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@RestController
public class SportProfileController {

    private final SportProfileService sportProfileService;

    @GetMapping("/nickname")
    @Operation(summary = "회원가입에서 닉네임 중복여부 확인")
    @ApiResponse(responseCode = "200", description = "닉네임 중복여부를 확인하고 boolean을 반환한다")
    public Boolean checkNicknameDuplication(@RequestParam String nickname) {
        return sportProfileService.checkNicknameDuplication(nickname);
    }
}
