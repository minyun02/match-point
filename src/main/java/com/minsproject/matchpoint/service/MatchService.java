package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final MatchRepository matchRepository;


    public List<Match> list(SportType sportType, String sort, Long lastId, Integer pageSize) {
        return matchRepository.list(sportType, sort, lastId, pageSize);
    }
}
