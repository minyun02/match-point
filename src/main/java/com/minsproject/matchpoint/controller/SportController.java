package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.response.SportResponse;
import com.minsproject.matchpoint.service.SportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "운동 종목", description = "운동 종목 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sports")
public class SportController {

    private SportService sportService;

    @Operation(summary = "운동 종목 목록 조회")
    @ApiResponse(responseCode = "200", description = "모든 운동 종목을 목록으로 반환한다")
    @GetMapping
    public List<SportResponse> list() {
        return sportService.getList();
    }
}
