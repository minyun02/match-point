package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.entity.UserToken;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public UserToken saveOrUpdate(String email, String provider, String refreshToken, String accessToken) {
        UserToken userToken = getByEmailAndProvider(email, provider)
                .map(entity -> entity.updateRefreshAndAccessToken(refreshToken, accessToken))
                .orElse(
                        UserToken.builder()
                                .email(email)
                                .provider(provider)
                                .refreshToken(refreshToken)
                                .accessToken(accessToken)
                                .build()
                );

        return tokenRepository.save(userToken);
    }

    public UserToken getTokenByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).orElseThrow(() -> new MatchPointException(ErrorCode.TOKEN_NOU_FOUND));
    }

    public void updateAccessToken(String newAccessToken, String email, String provider) {
        UserToken userToken = getByEmailAndProvider(email, provider)
                .orElseThrow(() -> new MatchPointException(ErrorCode.TOKEN_NOU_FOUND));

        userToken.updateAccessToken(newAccessToken);

        tokenRepository.save(userToken);
    }

    private Optional<UserToken> getByEmailAndProvider(String email, String provider) {
        return tokenRepository.findByEmailAndProvider(email, provider);
    }
}
