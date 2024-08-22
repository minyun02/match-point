package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public Sport getSportsById(Long sportsId) {
        return sportRepository.findById(sportsId).orElseThrow(() -> new LeagueCustomException(ErrorCode.SPORTS_NOT_FOUND));
    }

}