package com.minsproject.matchpoint.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchResultRequest {
    private String result;
    private Long submitterId;
}