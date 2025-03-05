package com.minsproject.matchpoint.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MatchResultRequest {

    @Pattern(regexp = "^(win|lose)", message = "유효한 매칭 결과가 아닙니다.")
    private String result;

    private Long submitterId;
}