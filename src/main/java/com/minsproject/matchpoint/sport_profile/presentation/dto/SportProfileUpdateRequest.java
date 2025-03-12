package com.minsproject.matchpoint.sport_profile.presentation.dto;

import com.minsproject.matchpoint.constant.type.SportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SportProfileUpdateRequest {
    private String nickname;

    private SportType sportType;

    private String sido;

    private String sigungu;

    private String dong;

    private String detail;

    private String fullAddress;

    private Double latitude;

    private Double longitude;
}
