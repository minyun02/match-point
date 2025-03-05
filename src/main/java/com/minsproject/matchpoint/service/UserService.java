package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.config.jwt.JwtTokenProvider;
import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.dto.request.SignupRequest;
import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.dto.response.TokenResponse;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.entity.UserToken;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class UserService {

    private final FileService fileService;
    private final TokenService tokenService;
    private final SportProfileService sportProfileService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public User loadUserByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserByProviderId(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public TokenResponse signup(SignupRequest request, MultipartFile profileImage) {
        if (userRepository.findByProviderAndProviderId(request.getProvider(), request.getProviderId()).isPresent()) {
            throw new MatchPointException(ErrorCode.CANNOT_SIGN_UP, "이미 등록된 사용자입니다.");
        }

        User newUser = User.builder()
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .email(request.getEmail())
                .name(request.getName())
                .role(UserRole.USER)
                .status(UserStatus.NORMAL)
                .build();

        User savedUser = userRepository.save(newUser);

        if (!request.getSportProfiles().isEmpty()) {
            SportProfileDTO newSportProfile = request.getSportProfiles().get(0);

            long lastRanking = sportProfileService.getLastRanking(newSportProfile.getSportType());
            if (lastRanking == 0) lastRanking = 1;

            SportProfile savedProfile = sportProfileService.createProfile(newSportProfile);
            if (savedProfile == null) {
                throw new MatchPointException(ErrorCode.CANNOT_SIGN_UP, "선택한 스포츠 프로필 생성에 실패했습니다.");
            }
            savedProfile.setUser(savedUser);
            savedProfile.setRanking((int) lastRanking);

            savedUser.setCurrentSport(newSportProfile.getSportType());

            if (profileImage != null && !profileImage.isEmpty()) {
                savedProfile.setProfileImage(fileService.uploadProfileImage(profileImage));
            }
        }

        String token = jwtTokenProvider.generateAccessToken(savedUser.getEmail(), savedUser.getProvider());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail(), savedUser.getProvider());
        tokenService.saveOrUpdate(savedUser.getEmail(), savedUser.getProvider(), token, refreshToken);

        return new TokenResponse(token, refreshToken);
    }

    public User getUserInfoByToken(String token) {
        UserToken userToken = tokenService.getUserByToken(token);
        return userRepository.findByEmailAndProvider(userToken.getEmail(), userToken.getProvider())
                .orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }
}
