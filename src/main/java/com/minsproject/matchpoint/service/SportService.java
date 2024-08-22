package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.dto.response.SportResponse;
import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public Sport getSportById(Long sportsId) {
        return sportRepository.findById(sportsId).orElseThrow(() -> new MatchPointException(ErrorCode.SPORTS_NOT_FOUND));
    }

    public List<SportResponse> getList() {
        List<Sport> sports = sportRepository.findAll();
        return sports.stream().map(SportResponse::fromEntity).toList();
    }
}
