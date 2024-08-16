package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.dto.request.JoinRequest;
import com.minsproject.matchpoint.dto.request.LoginRequest;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.UserRepository;
import com.minsproject.matchpoint.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public UserDTO loadUserByUserEmail(String email) {
        return userRepository.findByEmail(email).map(UserDTO::fromEntity).orElseThrow(() -> new LeagueCustomException(ErrorCode.USER_NOT_FOUND));
    }

    public String login(LoginRequest req) {
        UserDTO user = loadUserByUserEmail(req.getEmail());

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new LeagueCustomException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtTokenUtils.generateToken(req.getEmail(), secretKey, expiredTimeMs);
    }

    @Transactional
    public Long join(JoinRequest req) {
        userRepository.findByEmail(req.getEmail()).ifPresent(it -> {
            throw new LeagueCustomException(ErrorCode.DUPLICATED_USER_EMAIL);
        });
        return userRepository.save(JoinRequest.toEntity(req)).getId();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new LeagueCustomException(ErrorCode.USER_NOT_FOUND));
    }
}
