package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByEmailAndProvider(String email, String provider);

    Optional<UserToken> findByAccessToken(String accessToken);
}
