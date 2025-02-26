package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.type.SportType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuickMatchCreate {
    private Long inviterId;
    private Long inviteeId;
    private SportType sportType;
}