package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.dto.request.TopRankingRequest;
import com.minsproject.matchpoint.dto.response.ProfileRecommendationResponse;
import com.minsproject.matchpoint.dto.response.SportProfileResponse;
import com.minsproject.matchpoint.service.SportProfileService;
import com.minsproject.matchpoint.sport_profile.presentation.dto.SportProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/{profileId}")
    @Operation(summary = "스포츠 프로필 아이디 조회")
    @ApiResponse(responseCode = "200", description = "조회된 프로필 정보를 반환한다.")
    public SportProfileResponse getProfileById(@PathVariable Long profileId) {
        return SportProfileResponse.fromEntity(sportProfileService.getProfileById(profileId));
    }

    @GetMapping("/rankings")
    @Operation(summary = "해당 스포츠 순위 조회")
    @ApiResponse(responseCode = "200", description = "페이지 크기에 맞는 순위 목록을 반환한다.")
    public List<SportProfileResponse> getTopRankings(TopRankingRequest request) {
        return sportProfileService.getTopRankings(request).stream()
                .map(SportProfileResponse::fromEntity)
                .toList();
    }

    @PostMapping
    @Operation(summary = "새로운 프로필 추가")
    @ApiResponse(responseCode = "200", description = "생성 성공 boolean 값을 반환한다.")
    public SportProfileResponse create(@RequestPart("request") SportProfileDTO request,
                          @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        return SportProfileResponse.fromEntity(sportProfileService.create(request, profileImage));
    }

    @PatchMapping("/{profileId}")
    @Operation(summary = "프로필 수정")
    @ApiResponse(responseCode = "200", description = "수정된 프로필을 반환한다.")
    public SportProfileResponse modify(@PathVariable Long profileId, @RequestBody SportProfileUpdateRequest request) {
        return SportProfileResponse.fromEntity(sportProfileService.modify(profileId, request));
    }

    @GetMapping
    @Operation(summary = "매칭 가능한 프로필 목록 조회")
    @ApiResponse(responseCode = "200", description = "검색 조건에 맞는 프로필 목록을 반환한다.")
    public List<SportProfileResponse> getMatchableProfileList(@RequestParam Long profileId,
                                                              @RequestParam String searchWord,
                                                              @RequestParam String sort,
                                                              @RequestParam Integer distance,
                                                              @RequestParam Long lastId,
                                                              @RequestParam Integer pageSize
    ) {
        return sportProfileService.getProfileListForMatch(profileId, searchWord, sort, distance, lastId, pageSize)
                .stream()
                .map(SportProfileResponse::of)
                .toList();
    }

    @GetMapping("/recommendations")
    @Operation(summary = "매칭 추천 목록")
    @ApiResponse(responseCode = "200", description = "매칭 추천 목록을 반환한다.")
    public List<ProfileRecommendationResponse> getRecommendations(
            @RequestParam Long profileId,
            @RequestParam Long lastId,
            @RequestParam Integer pageSize
    ) {
        return sportProfileService.getRecommendations(profileId, lastId, pageSize).stream()
                .map(ProfileRecommendationResponse::fromEntity)
                .toList();
    }
}