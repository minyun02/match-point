package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.MatchRequest;
import com.minsproject.matchpoint.dto.request.MatchRespondRequest;
import com.minsproject.matchpoint.dto.request.MatchSearchRequest;
import com.minsproject.matchpoint.dto.response.MatchResponse;
import com.minsproject.matchpoint.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "매칭", description = "매칭 API ENDPOINTS")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @Operation(summary = "매칭 신청")
    @ApiResponse(responseCode = "200", description = "신청된 매칭 정보를 반환한다")
    public MatchResponse createMatch(@Valid @RequestBody MatchRequest request) {
        return MatchResponse.fromEntity(matchService.createMatch(request));
    }

    @GetMapping("/received")
    @Operation(summary = "받은 매칭 목록 조회")
    @ApiResponse(responseCode = "200", description = "해당 운동 프로필이 받은 매칭을 페이징 처리해서 반환한다.")
    public List<MatchResponse> receivedMatches(@Valid @ModelAttribute MatchSearchRequest request) {
        return matchService.getReceivedMatches(request).stream()
                .map(MatchResponse::fromEntity)
                .toList();
    }

    @PutMapping("/{matchId}/respond")
    @Operation(summary = "매칭 상태 변경")
    @ApiResponse(responseCode = "200", description = "매칭의 상태를 변경하고 변경된 매칭 정보를 반환한다.")
    public MatchResponse modifyStatus(@PathVariable Long matchId, @RequestBody MatchRespondRequest request) {
        return MatchResponse.fromEntity(matchService.modifyStatus(matchId, request));
    }
}
