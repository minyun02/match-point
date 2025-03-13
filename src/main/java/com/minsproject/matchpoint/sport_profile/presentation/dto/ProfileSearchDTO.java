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
    private Long lastId = 0L;
    private Integer pageSize;

    public ProfileSearchDTO(Long profileId, Integer distance, Integer pageSize) {
        this.profileId = profileId;
        this.distance = distance;
        this.pageSize = pageSize;
    }

    public static ProfileSearchDTO createForRecommendations(Long profileId, Integer distance, Integer pageSize) {
        if (distance == null || distance <= 0) {
            distance = 10;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 50;
        }
        return new ProfileSearchDTO(profileId, distance, pageSize);
    }
}
