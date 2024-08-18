package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDTO loadUserByUserEmail(String email) {
        return userRepository.findByEmail(email).map(UserDTO::fromEntity).orElseThrow(() -> new LeagueCustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new LeagueCustomException(ErrorCode.USER_NOT_FOUND));
    }
}
