package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.dto.request.MatchSearchRequest;
import com.minsproject.matchpoint.entity.Match;

import java.util.List;

public interface MatchCustomRepository {
    List<Match> findReceivedMatches(MatchSearchRequest request);
}
