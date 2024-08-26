package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.dto.response.SportResponse;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDTO loadUserByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider).map(UserDTO::fromEntity).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));
    }

    public List<SportResponse> getSportsById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND))
                .getMembers().stream().map(Member::getSport).toList()
                .stream().map(SportResponse::fromEntity).toList();
    }
}
