package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class MatchSearchRequest {

    @Schema(description = "받은 매칭 목록을 조회하려는 프로필의 아이디")
    @NotNull(message = "운동 프로필 아이디는 필수입니다.")
    private Long memberId;

    @Schema(description = "검색 필터링 - 상대방 닉네임", nullable = true)
    private String inviterNickname;

    @Schema(description = "검색 필터링 - 매칭 상태", nullable = true)
    private MatchStatus status;

    @Schema(description = "검색 필터링 - 매칭 날짜 검색 범위 시작 날짜", nullable = true)
    private LocalDateTime startDate;

    @Schema(description = "검색 필터링 - 매칭 날짜 검색 범위 종료 날짜", nullable = true)
    private LocalDateTime endDate;

    public MatchSearchRequest(Long memberId, String inviterNickname, MatchStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.memberId = memberId;
        this.inviterNickname = inviterNickname;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}