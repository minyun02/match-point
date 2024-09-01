package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchRequest {

    @Schema(description = "매칭 신청자 아이디")
    @NotNull(message = "사용자 정보는 필수입니다.")
    private Long inviterId;

    @Schema(description = "매칭 상대방 아이디")
    @NotNull(message = "매칭 상대는 필수입니다.")
    private Long inviteeId;

    @Schema(description = "운동 종목 아이디")
    @NotNull(message = "운동 종목은 필수입니다.")
    private Long sportId;

    @Schema(description = "매칭 장소 아이디")
    @NotNull(message = "매칭 장소 정보는 필수입니다.")
    private Long placeId;

    @Schema(description = "매칭 신청 메시지")
    @NotBlank(message = "매칭 신청 메시지는 필수입니다.")
    private String message;

    @NotNull(message = "매칭 날짜는 필수입니다.")
    private LocalDateTime matchDay;

    public static Match toEntity(Member inviter, Member invitee, Sport sport, Place place, MatchRequest request) {
        return new Match(
                inviter,
                invitee,
                sport,
                place,
                request.getMatchDay(),
                request.getMessage(),
                MatchStatus.WAITING
        );
    }
}
