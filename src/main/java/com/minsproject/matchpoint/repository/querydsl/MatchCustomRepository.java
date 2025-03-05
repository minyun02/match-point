package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.MatchListRequest;
import com.minsproject.matchpoint.entity.Match;

import java.util.List;

public interface MatchCustomRepository {
    List<Match> list(MatchListRequest request);
}
