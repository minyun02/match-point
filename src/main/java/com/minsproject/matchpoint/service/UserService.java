package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.dto.request.UserRequest;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class UserService {

    private final UserRepository userRepository;

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
}
