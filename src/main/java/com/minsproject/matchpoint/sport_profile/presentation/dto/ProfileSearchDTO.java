package com.minsproject.matchpoint.sport_profile.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileSearchDTO {
    private Long profileId;
    private String searchWord;
    private String sort;
    private Integer distance;
    private Long lastId;
    private Integer pageSize;
}
