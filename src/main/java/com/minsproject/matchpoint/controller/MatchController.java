package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.MatchCreateRequest;
import com.minsproject.matchpoint.dto.request.MatchListRequest;
import com.minsproject.matchpoint.dto.request.MatchResultRequest;
import com.minsproject.matchpoint.dto.request.QuickMatchCreate;
import com.minsproject.matchpoint.dto.response.MatchResponse;
import com.minsproject.matchpoint.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/matches")
@RestController
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    @Operation(summary = "사용자의 모든 매칭 목록 조회")
    @ApiResponse(responseCode = "200", description = "사용자의 모든 매칭을 배열로 반환한다.")
    public List<MatchResponse> list(MatchListRequest request) {
        return matchService.list(request).stream()
                .map(MatchResponse::fromEntity)
                .toList();
    }

    @PostMapping("/quick-match")
    @Operation(summary = "빠른 매칭에서 QR 코드를 통해 매칭을 수락")
    @ApiResponse(responseCode = "200", description = "매칭 정보를 반환한다.")
    public MatchResponse createQuickMatch(@RequestBody QuickMatchCreate request) {
        return MatchResponse.fromEntity(matchService.createQuickMatch(request));
    }

    @GetMapping("/{matchId}")
    @Operation(summary = "매칭 정보 조회")
    @ApiResponse(responseCode = "200", description = "조회한 매칭 정보를 반환한다.")
    public MatchResponse getMatch(@PathVariable Long matchId) {
        return MatchResponse.fromEntity(matchService.getMatch(matchId));
    }

    @PostMapping("/{matchId}/result")
    @Operation(summary = "매치 결과 입력")
    @ApiResponse(responseCode = "200")
    public void result(@PathVariable Long matchId, @RequestBody MatchResultRequest request) {
        matchService.result(matchId, request);
    }

    @PostMapping
    @Operation(summary = "매치 신청")
    @ApiResponse(responseCode = "200", description = "생성된 매칭 정보를 반환한다.")
    public MatchResponse createMatch(@RequestBody MatchCreateRequest request) {
        return MatchResponse.fromEntity(matchService.createMatch(request));
    }
}