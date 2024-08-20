package com.minsproject.matchpoint.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String provider;

    private String accessToken;

    @Getter
    private String refreshToken;

    @Builder
    private UserToken(String email, String provider, String accessToken, String refreshToken) {
        this.email = email;
        this.provider = provider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserToken updateRefreshAndAccessToken(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;

        return this;
    }

    public UserToken updateAccessToken(String newAccessToken) {
        this.accessToken = newAccessToken;

        return this;
    }
}
