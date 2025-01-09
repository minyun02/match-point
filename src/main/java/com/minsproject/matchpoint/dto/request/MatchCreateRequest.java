package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.type.SportType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MatchCreateRequest {
    private final SportType sportType;
    private final Long inviterId;
    private final Long inviteeId;
    private final LocalDateTime matchDate;
    private final String matchLocation;
}