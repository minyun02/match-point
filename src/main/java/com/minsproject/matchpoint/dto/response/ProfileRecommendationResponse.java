package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProfileRecommendationResponse {
    private Long profileId;
    private String nickname;
    private String profileImage;

    private SportType sportType;
    private BigDecimal winRate;
    private Double mannerRate;
    private Integer ranking;
    private Integer totalMatches;

    private Double distanceInKm;

    private double similarity;

    public static ProfileRecommendationResponse fromEntity(ProfileWithInfo<SportProfile> profile) {
        SportProfile entity = profile.getEntity();
        return ProfileRecommendationResponse.builder()
                .profileId(entity.getId())
                .nickname(entity.getNickname())
                .profileImage(entity.getProfileImage())
                .sportType(entity.getSportType())
                .winRate(entity.getWinRate())
                .mannerRate(entity.getMannerRate())
                .ranking(entity.getRanking())
                .totalMatches(entity.getTotalMatches())
                .distanceInKm(profile.getDistance())
                .similarity(profile.getSimilarity())
                .build();
    }
}