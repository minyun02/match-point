package com.minsproject.matchpoint.sport_profile.domain;

import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.utils.ProfileSimilarityCalculator;

import java.util.List;

public class RecommendedProfiles {

    private List<ProfileWithInfo<SportProfile>> recommendedProfiles;

    public RecommendedProfiles(List<ProfileWithInfo<SportProfile>> recommendedProfiles) {
        this.recommendedProfiles = recommendedProfiles;
    }

    public void createRecommendations(SportProfile targetProfile) {
        ProfileSimilarityCalculator calculator = new ProfileSimilarityCalculator();

        for (ProfileWithInfo<SportProfile> profile : recommendedProfiles) {
            profile.setSimilarity(calculator.calculateSimilarity(targetProfile, profile));
        }
    }

    public List<ProfileWithInfo<SportProfile>> sortInSimilarity() {
        return this.recommendedProfiles.stream().sorted(ProfileWithInfo.compareBySimilarity()).toList();
    }
}
