package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.repository.SportProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SportProfileService {

    private SportProfileRepository sportProfileRepository;

    public Boolean checkNicknameDuplication(String nickname) {
        return sportProfileRepository.findByNickname(nickname).isPresent();
    }
}