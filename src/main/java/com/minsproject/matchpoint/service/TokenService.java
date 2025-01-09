package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.config.jwt.JwtTokenProvider;
import com.minsproject.matchpoint.dto.response.TokenResponse;
import com.minsproject.matchpoint.entity.UserToken;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    public void saveOrUpdate(String email, String provider, String token, String refreshToken) {
        UserToken userToken = getByEmailAndProvider(email, provider)
                .map(entity -> entity.updateTokenAndRefreshToken(token, refreshToken))
                .orElse(
                        UserToken.builder()
                                .email(email)
                                .provider(provider)
                                .refreshToken(refreshToken)
                                .token(token)
                                .build()
                );

        tokenRepository.save(userToken);
    }

    public UserToken getUserByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new MatchPointException(ErrorCode.TOKEN_NOU_FOUND));
    }

    private Optional<UserToken> getByEmailAndProvider(String email, String provider) {
        return tokenRepository.findByEmailAndProvider(email, provider);
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || !refreshToken.startsWith("Bearer ")) {
            throw new MatchPointException(ErrorCode.EMPTY_REFRESH_TOKEN);
        }

        String trimmedRefreshToken = refreshToken.split(" ")[1].trim();
        if (!tokenProvider.validateToken(trimmedRefreshToken)) {
            throw new MatchPointException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        UserToken userToken = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new MatchPointException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        String trimmedToken = refreshToken.split(" ")[1].trim();

        String newAccessToken = tokenProvider.generateNewAccessToken(trimmedToken, trimmedRefreshToken);

        userToken.updateTokenAndRefreshToken(refreshToken, newAccessToken);

        return new TokenResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public TokenResponse generateToken(String email, String provider) {
        UserToken userToken = tokenRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new MatchPointException(ErrorCode.TOKEN_NOU_FOUND));

        String token = tokenProvider.generateAccessToken(email, provider);
        String refreshToken = tokenProvider.generateRefreshToken(email, provider);

        userToken.updateTokenAndRefreshToken(token, refreshToken);

        return new TokenResponse(
                token,
                refreshToken
        );
    }
}
