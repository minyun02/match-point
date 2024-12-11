package com.minsproject.matchpoint.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String provider;

    private String token;

    private String refreshToken;

    @Builder
    private UserToken(String email, String provider, String token, String refreshToken) {
        this.email = email;
        this.provider = provider;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public UserToken updateTokenAndRefreshToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;

        return this;
    }

    public UserToken updateToken(String newToken) {
        this.token = newToken;

        return this;
    }
}
