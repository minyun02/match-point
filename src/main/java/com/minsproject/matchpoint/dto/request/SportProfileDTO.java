package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.type.SportType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SportProfileDTO {
    private Long userId;
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