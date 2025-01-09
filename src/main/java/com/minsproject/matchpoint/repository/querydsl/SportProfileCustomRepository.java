package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.entity.SportProfile;

import java.util.List;

public interface SportProfileCustomRepository {

    List<SportProfile> list(String sportType, String range, String address, Integer pageSize, Long lastId, String sort);

    List<ProfileWithInfo<SportProfile>> findProfileListForMatch(Long profileId, String sportType,
                                                                Double latitude,
                                                                Double longitude,
                                                                String searchWord,
                                                                String sort,
                                                                Integer distance,
                                                                Long lastId,
                                                                Integer pageSize
    );
}