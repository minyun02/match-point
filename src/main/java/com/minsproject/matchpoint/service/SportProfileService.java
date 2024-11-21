package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.entity.SportProfile;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.repository.SportProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SportProfileService {

    private final SportProfileRepository sportProfileRepository;

    public Boolean checkNicknameDuplication(String nickname) {
        return sportProfileRepository.findByNickname(nickname).isPresent();
    }

    public SportProfile createProfile(User user, SportProfileDTO newSportProfile) {
        SportProfile sportProfile = SportProfile.builder()
                .user(user)
                .sportType(newSportProfile.getSportType())
                .nickname(newSportProfile.getNickname())
                .sido(newSportProfile.getSido())
                .sigungu(newSportProfile.getSigungu())
                .dong(newSportProfile.getDong())
                .detail(newSportProfile.getDetail())
                .fullAddress(newSportProfile.getFullAddress())
                .latitude(newSportProfile.getLatitude())
                .longitude(newSportProfile.getLongitude())
                .build();
        return sportProfileRepository.save(sportProfile);
    }
}