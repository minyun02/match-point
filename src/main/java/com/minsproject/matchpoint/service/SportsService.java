package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.entity.Sports;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.SportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportsService {

    private final SportsRepository sportsRepository;

    public Sports getSportsById(Long sportsId) {
        return sportsRepository.findById(sportsId).orElseThrow(() -> new LeagueCustomException(ErrorCode.SPORTS_NOT_FOUND));
    }

}
