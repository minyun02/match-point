package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberCreateRequest {
    @Schema(description = "사용자 아이디")
    @NotNull(message = "사용자 정보는 필수입니다.")
    private Long userId;

    @Schema(description = "운동 종목")
    @NotNull(message = "운동 종목은 필수입니다.")
    private Long sportId;

    @Schema(description = "해당 종목에서 사용할 닉네임")
    @NotBlank(message = "해당 종목에서 사용할 닉네임은 필수입니다.")
    @Size(max = 10, message = "닉네임은 10자리이하만 가능합니다.")
    private String nickname;

    @Schema(description = "해당 종목에서 사용할 프로필 사진", nullable = true)
    private String memberImage;

    @Schema(description = "해당 종목에서의 레벨")
    @NotNull(message = "")
    private Integer level;

    @Schema(description = "주소(도)", example = "서울시, 경기도")
    @NotBlank(message = "주소는 필수입니다.")
    private String city;

    @Schema(description = "주소(구, 군)", example = "종로구, 가평군")
    @NotBlank(message = "주소는 필수입니다.")
    private String district;

    @Schema(description = "주소(동 이하)", example = "신사동, ..읍")
    @NotBlank(message = "주소는 필수입니다.")
    private String neighborhood;

    @Schema(description = "사용자 매칭 기준 좌표(위도)", example = "37.5140")
    @NotNull(message = "매칭을 위해서 위치 좌표는 필수입니다.")
    private Double latitude;

    @Schema(description = "사용자 매칭 기준 좌표(경도)", example = "127.0565")
    @NotNull(message = "매칭을 위해서 위치 좌표는 필수입니다.")
    private Double longitude;

    public Member toEntity(User user, Sport sport) {
        return Member.builder()
                .user(user)
                .sport(sport)
                .nickname(nickname)
                .memberImage(memberImage)
                .level(level)
                .city(city)
                .district(district)
                .neighborhood(neighborhood)
                .latitude(latitude)
                .longitude(longitude)
                .canMatch(true)
                .build();
    }
}
