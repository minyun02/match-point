package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.Match;

import java.util.List;

public interface MatchCustomRepository {
    List<Match> list(SportType sportType, String sort, Long lastId, Integer pageSize);
}
