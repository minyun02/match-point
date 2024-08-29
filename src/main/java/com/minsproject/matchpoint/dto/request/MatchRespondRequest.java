package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import lombok.Getter;

@Getter
public class MatchRespondRequest {

    private Long memberId;

    private MatchStatus status;
}
