package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.dto.request.TopRankingRequest;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.RecommendedProfiles;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.SportProfileRepository;
import com.minsproject.matchpoint.sport_profile.presentation.dto.ProfileSearchDTO;
import com.minsproject.matchpoint.sport_profile.presentation.dto.SportProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SportProfileService {

    private final FileService fileService;
    private final UserService userService;
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

    public SportProfile create(SportProfileDTO request, MultipartFile profileImage) {
        User user = userService.getUserById(request.getUserId());

        SportProfile profile = SportProfile.createWithRanking(request, user, getMaxRanking(request.getSportType()));
        if (profileImage != null && !profileImage.isEmpty()) {
            profile.setProfileImage(fileService.uploadProfileImage(profileImage));
        }

        return sportProfileRepository.save(profile);
    }

    @Transactional
    public SportProfile modify(Long profileId, SportProfileUpdateRequest request) {
        SportProfile sportProfile = getProfileById(profileId);
        sportProfile.updateProfile(request);
        return sportProfileRepository.save(sportProfile);
    }

    public List<ProfileWithInfo<SportProfile>> getProfileListForMatch(ProfileSearchDTO searchDTO) {
        SportProfile sportProfile = getProfileById(searchDTO.getProfileId());

        return sportProfileRepository.findProfileListForMatch(
                searchDTO,
                sportProfile.getSportType(),
                sportProfile.getLatitude(),
                sportProfile.getLongitude()
        );
    }

    public List<ProfileWithInfo<SportProfile>> getRecommendations(Long profileId, Integer distance, Integer pageSize) {
        SportProfile sportProfile = getProfileById(profileId);

        List<ProfileWithInfo<SportProfile>> profileListForMatch = sportProfileRepository.findProfileListForMatch(
                ProfileSearchDTO.createForRecommendations(profileId, distance, pageSize),
                sportProfile.getSportType(),
                sportProfile.getLatitude(),
                sportProfile.getLongitude()
        );

        if (profileListForMatch.isEmpty()) {
            return profileListForMatch;
        }

        RecommendedProfiles recommendedProfiles = new RecommendedProfiles(profileListForMatch);
        recommendedProfiles.createRecommendations(sportProfile);

        return recommendedProfiles.sortInSimilarity();
    }

    public List<SportProfile> getProfilesByUser(User user) {
        return sportProfileRepository.findByUser(user).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));
    }
}