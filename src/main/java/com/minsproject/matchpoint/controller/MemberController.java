package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.MemberCreateRequest;
import com.minsproject.matchpoint.dto.response.MemberResponse;
import com.minsproject.matchpoint.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "종목별 멤버", description = "사용자의 종목별 멤버 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "사용자의 운동 종목 계정 생성")
    @ApiResponse(responseCode = "200", description = "만들어진 운동 종목 계정을 반환한다")
    public MemberResponse create(@Valid @RequestBody MemberCreateRequest request) {
        return memberService.create(request);
    }
}
