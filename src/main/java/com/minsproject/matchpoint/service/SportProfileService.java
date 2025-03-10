package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.dto.request.TopRankingRequest;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.SportProfileRepository;
import com.minsproject.matchpoint.repository.UserRepository;
import com.minsproject.matchpoint.utils.ProfileSimilarityCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SportProfileService {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final SportProfileRepository sportProfileRepository;

    public Boolean checkNicknameDuplication(String nickname) {
        return sportProfileRepository.findByNickname(nickname).isPresent();
    }

    public SportProfile createProfile(SportProfileDTO newSportProfile) {
        SportProfile sportProfile = SportProfile.builder()
                .sportType(newSportProfile.getSportType())
                .nickname(newSportProfile.getNickname())
                .sido(newSportProfile.getSido())
                .sigungu(newSportProfile.getSigungu())
                .dong(newSportProfile.getDong())
                .detail(newSportProfile.getDetail())
                .fullAddress(newSportProfile.getFullAddress())
                .latitude(newSportProfile.getLatitude())
                .longitude(newSportProfile.getLongitude())
                .wins(0)
                .loses(0)
                .totalMatches(0)
                .mannerRate(5.0)
                .build();

        return sportProfileRepository.save(sportProfile);
    }

    public SportProfile getProfileById(Long profileId) {
        return sportProfileRepository.findById(profileId).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));
    }

    public List<SportProfile> getTopRankings(TopRankingRequest request) {
        return sportProfileRepository.list(request);
    }

    public Integer getMaxRanking(SportType sportType) {
        Integer maxRanking = sportProfileRepository.findMaxRankingBySportType(sportType);
        return maxRanking == null ? 1 : maxRanking + 1;
    }

    public boolean create(SportProfileDTO request, MultipartFile profileImage) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));

        SportProfile profile = createProfile(request);
        profile.setUser(user);

        long lastRanking = getMaxRanking(request.getSportType());
        if (lastRanking == 0) lastRanking = 1;
        profile.setRanking((int) lastRanking);

        if (profileImage != null && !profileImage.isEmpty()) {
            profile.setProfileImage(fileService.uploadProfileImage(profileImage));
        }

        sportProfileRepository.save(profile);

        return true;
    }

    @Transactional
    public SportProfile modify(Long profileId, SportProfileDTO request, MultipartFile profileImage) {
        SportProfile sportProfile = sportProfileRepository.findById(profileId).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));

        sportProfile.setNickname(request.getNickname());

        if (request.getFullAddress() != null) {
            sportProfile.updateProfileAddress(request);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            sportProfile.setProfileImage(fileService.uploadProfileImage(profileImage));
        }

        return sportProfileRepository.save(sportProfile);
    }

    public List<ProfileWithInfo<SportProfile>> getProfileListForMatch(Long profileId,
                                                                      String searchWord,
                                                                      String sort,
                                                                      Integer distance,
                                                                      Long lastId,
                                                                      Integer pageSize
    ) {
        SportProfile sportProfile = sportProfileRepository.findById(profileId).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));

        return sportProfileRepository.findProfileListForMatch(profileId, sportProfile.getSportType(), sportProfile.getLatitude(), sportProfile.getLongitude(), searchWord, sort, distance, lastId, pageSize);
    }

    public List<ProfileWithInfo<SportProfile>> getRecommendations(Long profileId, Long lastId, Integer pageSize) {
        SportProfile profile1 = sportProfileRepository.findById(profileId).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));

        ProfileSimilarityCalculator calculator = new ProfileSimilarityCalculator();

        List<ProfileWithInfo<SportProfile>> profileListForMatch = getProfileListForMatch(profileId, "", null, 10, lastId, pageSize);

        for (ProfileWithInfo<SportProfile> profile2 : profileListForMatch) {
            profile2.setSimilarity(calculator.calculateSimilarity(profile1, profile2));
        }

        return profileListForMatch.stream()
                .sorted(ProfileWithInfo.compareBySimilarity())
                .toList();
    }

    public List<SportProfile> getProfilesByUser(User user) {
        return sportProfileRepository.findByUser(user).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));
    }
}