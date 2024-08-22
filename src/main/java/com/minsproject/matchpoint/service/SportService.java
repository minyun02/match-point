package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public Sport getSportById(Long sportsId) {
        return sportRepository.findById(sportsId).orElseThrow(() -> new MatchPointException(ErrorCode.SPORTS_NOT_FOUND));
    }

}
