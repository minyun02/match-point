package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.MatchRequest;
import com.minsproject.matchpoint.dto.response.MatchResponse;
import com.minsproject.matchpoint.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
