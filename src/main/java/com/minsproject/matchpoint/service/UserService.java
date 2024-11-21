package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.config.jwt.JwtTokenProvider;
import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.dto.request.SignupRequest;
import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.dto.request.UserRequest;
import com.minsproject.matchpoint.entity.SportProfile;
import com.minsproject.matchpoint.entity.User;
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

    private final UserRepository userRepository;
    private final SportProfileService sportProfileService;
    private final FileService fileService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserRequest loadUserByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider).map(UserRequest::fromEntity).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserByToken(String token) {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserByProviderId(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public String signup(SignupRequest request, MultipartFile profileImage) {
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
            SportProfile savedProfile = sportProfileService.createProfile(savedUser, newSportProfile);
            if (savedProfile == null) {
                throw new MatchPointException(ErrorCode.CANNOT_SIGN_UP, "선택한 스포츠 프로필 생성에 실패했습니다.");
            }

            savedUser.setCurrentSport(newSportProfile.getSportType());

            if (profileImage != null && !profileImage.isEmpty()) {
                savedProfile.setProfileImage(fileService.uploadProfileImage(profileImage));
            }
        }

        String token = jwtTokenProvider.generateAccessTokenFromUser(savedUser);
        savedUser.setToken(token);
        return token;
    }
}
