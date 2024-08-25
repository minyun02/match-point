package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.MemberCreateRequest;
import com.minsproject.matchpoint.dto.response.MemberResponse;
import com.minsproject.matchpoint.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "종목별 멤버", description = "사용자의 종목별 멤버 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "사용자의 운동 종목 프로필 생성")
    @ApiResponse(responseCode = "200", description = "만들어진 운동 종목 프로필을 반환한다")
    public MemberResponse create(@Valid @RequestBody MemberCreateRequest request) {
        return memberService.create(request);
    }

    @GetMapping("/by-nickname/{nickname}")
    @Operation(summary = "운동 종목 프로필 닉네임으로 조회")
    @ApiResponse(responseCode = "200", description = "해당 닉네임으로 조회한 결과가 있으면 프로필 정보를 반환하고 없으면 비어있는 프로필을 반환한다")
    public MemberResponse getMemberByNickname(@PathVariable String nickname) {
        return memberService.getMemberByNickname(nickname);
    }

    @GetMapping("/by-id/{memberId}")
    @Operation(summary = "운동 종목 프로필 아이디와 종목 아이디로 조회")
    @ApiResponse(responseCode = "200", description = "조회한 결과를 반환한다")
    public MemberResponse getMemberByIdAndSportId(@PathVariable Long memberId, @RequestParam Long sportId) {
        return memberService.getMemberByIdAndSportId(memberId, sportId);
    }

}
