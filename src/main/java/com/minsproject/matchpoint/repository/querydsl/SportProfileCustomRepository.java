package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.TopRankingRequest;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.sport_profile.presentation.dto.ProfileSearchDTO;

import java.util.List;

public interface SportProfileCustomRepository {

    List<SportProfile> list(TopRankingRequest request);

    List<ProfileWithInfo<SportProfile>> findProfileListForMatch(ProfileSearchDTO searchDTO,
                                                                SportType sportType,
                                                                Double latitude,
                                                                Double longitude
    );

    Integer findMaxRankingBySportType(SportType sportType);
}