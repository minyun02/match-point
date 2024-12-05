package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.response.MatchResponse;
import com.minsproject.matchpoint.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/matches")
@RestController
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    @Operation(summary = "사용자의 모든 매칭 목록 조회")
    @ApiResponse(responseCode = "200", description = "사용자의 모든 매칭을 배열로 반환한다.")
    public List<MatchResponse> list(@RequestParam SportType sportType,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam Long lastId,
                                    @RequestParam Integer pageSize) {
        return matchService.list(sportType, sort, lastId, pageSize).stream()
                .map(MatchResponse::fromEntity)
                .toList();
    }
}
