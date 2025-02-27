package com.minsproject.matchpoint.utils;

import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;

public class ProfileSimilarityCalculator {

    private static final double LOCATION_WEIGHT = 0.4;
    private static final double SKILL_WEIGHT = 0.35;
    private static final double MANNER_WEIGHT = 0.25;

    public double calculateSimilarity(SportProfile profile1, ProfileWithInfo profile2) {
        double locationSimilarity = calculateLocationSimilarity(profile1, profile2.getDistance());
        double skillSimilarity = calculateSkillSimilarity(profile1, (SportProfile) profile2.getEntity());
        double mannerSimilarity = calculateMannerSimilarity(profile1, (SportProfile) profile2.getEntity());


        return (locationSimilarity * LOCATION_WEIGHT) +
                (skillSimilarity * SKILL_WEIGHT) +
                (mannerSimilarity * MANNER_WEIGHT);
    }

    private double calculateMannerSimilarity(SportProfile p1, SportProfile p2) {
        double mannerDifference = Math.abs(p1.getMannerRate() - p2.getMannerRate());
        double normalizedDifference = mannerDifference / 5.0;
        double similarity = 1.0 - normalizedDifference;
        double minMannerThreshold = 3.0;

        if (p1.getMannerRate() < minMannerThreshold ||
                p2.getMannerRate() < minMannerThreshold) {
            similarity *= 0.5;
        }

        double highMannerThreshold = 4.5;
        if (p1.getMannerRate() >= highMannerThreshold &&
                p2.getMannerRate() >= highMannerThreshold) {
            similarity = Math.min(1.0, similarity * 1.2);
        }

        return similarity;
    }

    private double calculateLocationSimilarity(SportProfile profile1, Double distance) {
        return Math.max(0, 1 - (distance / 10000));
    }

    private double calculateSkillSimilarity(SportProfile profile1, SportProfile profile2) {
        double winRateDiff = Math.abs(profile1.getWinRate().doubleValue() - profile2.getWinRate().doubleValue());

        double rankingDiff = Math.abs(profile1.getRanking() - profile2.getRanking());

        return 1 - ((winRateDiff / 100 + rankingDiff / 1000) / 2);
    }
}