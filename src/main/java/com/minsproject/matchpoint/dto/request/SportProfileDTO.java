package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.type.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SportProfileDTO {
    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;
    
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다")
    private String nickname;
    
    @NotNull(message = "스포츠 유형은 필수입니다")
    private SportType sportType;
    
    @NotBlank(message = "시/도는 필수입니다")
    private String sido;
    
    @NotBlank(message = "시/군/구는 필수입니다")
    private String sigungu;
    
    @NotBlank(message = "동/읍/면은 필수입니다")
    private String dong;

    @NotBlank(message = "상세주소는 필수입니다")
    private String detail;
    
    @NotBlank(message = "전체 주소는 필수입니다")
    private String fullAddress;
    
    @NotNull(message = "위도는 필수입니다")
    private Double latitude;
    
    @NotNull(message = "경도는 필수입니다")
    private Double longitude;
}