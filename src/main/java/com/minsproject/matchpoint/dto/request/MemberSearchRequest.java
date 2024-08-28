package com.minsproject.matchpoint.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberSearchRequest {
    @Schema(description = "사용자 운동 프로필 아이디")
    @NotNull(message = "사용자의 운동 프로필 정보는 필수입니다.")
    private Long memberId;

    @Schema(description = "운동 종목 아이디")
    @NotNull(message = "운동 종목 정보는 필수입니다.")
    private Long sportId;

    @Schema(description = "운동 종목명")
    @NotBlank(message = "운동 종목명은 필수입니다.")
    private String sportName;

    @Schema(description = "매칭 상대를 찾을 범위 주소(시)")
    @NotBlank(message = "매칭을 위한 주소는 필수입니다.")
    private String city;

    @Schema(description = "매칭 상대를 찾을 범위 주소(구,군)")
    @NotBlank(message = "매칭을 위한 주소는 필수입니다.")
    private String district;

    @Schema(description = "매칭 상대를 찾을 범위 주소(동,읍)")
    @NotBlank(message = "매칭을 위한 주소는 필수입니다.")
    private String neighborhood;

    @Schema(description = "매칭 상대를 찾을 범위 기준 위도")
    @NotNull(message = "매칭 상대방 검색에는 사용자 위치가 필요합니다.")
    private Double latitude;

    @Schema(description = "매칭 상대를 찾을 범위 기준 경도")
    @NotNull(message = "매칭 상대방 검색에는 사용자 위치가 필요합니다.")
    private Double longitude;

    @Schema(description = "매칭 상대를 찾을 반경", nullable = true, defaultValue = "1")
    private Integer radius = 1;

    @Schema(description = "매칭 상대의 최소 레벨", nullable = true)
    private Integer minLevel;

    @Schema(description = "매칭 상대의 최대 레벨", nullable = true)
    private Integer maxLevel;

    @Schema(description = "원하는 매칭 장소")
    private PlaceRequest place;

    @Schema(description = "원하는 매칭 날짜")
    private LocalDateTime matchDay;

    @Schema(description = "페이징 - 목록에서 마지막 프로필 아이디", defaultValue = "0")
    private Long lastMemberId = 0L;

    @Schema(description = "페이징 - 목록에 노출 할 정보 개수", defaultValue = "20")
    private Integer pageSize = 20;
}