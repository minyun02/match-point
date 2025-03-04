package com.minsproject.matchpoint.sport_profile.domain;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;

import java.util.List;

public class SportProfiles {

    private final List<SportProfile> sportProfiles;

    public SportProfiles(List<SportProfile> sportProfiles) {
        this.sportProfiles = sportProfiles;
    }

    public void validateSportType(SportType sportType) {
        this.sportProfiles.stream()
                .filter(profile -> profile.getSportType() == sportType)
                .findFirst()
                .orElseThrow(() -> new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE));
    }
}
