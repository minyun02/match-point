package com.minsproject.matchpoint.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuickMatchCreate {
    private Long inviterId;
    private Long inviteeId;
    private String sportType;
}