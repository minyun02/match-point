package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.TopRankingRequest;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;

import java.util.List;

public interface SportProfileCustomRepository {

    List<SportProfile> list(TopRankingRequest request);

    List<ProfileWithInfo<SportProfile>> findProfileListForMatch(Long profileId,
                                                                SportType sportType,
                                                                Double latitude,
                                                                Double longitude,
                                                                String searchWord,
                                                                String sort,
                                                                Integer distance,
                                                                Long lastId,
                                                                Integer pageSize
    );

    Integer findMaxRankingBySportType(SportType sportType);
}