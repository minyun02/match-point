package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.entity.SportProfile;
import com.minsproject.matchpoint.entity.User;

import java.sql.Timestamp;
import java.util.List;

public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String provider;

    private String providerId;

    private UserRole role;

    private UserStatus status;

    private Timestamp lastLoginAt;

    private List<SportProfile> sportProfiles;

    private UserResponse(Long id, String name, String email, String provider, String providerId, UserRole role, UserStatus status, Timestamp lastLoginAt, List<SportProfile> sportProfiles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.sportProfiles = sportProfiles;
    }

    public static UserResponse fromEntity(User entity) {
        return new UserResponse(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getProvider(),
            entity.getProviderId(),
            entity.getRole(),
            entity.getStatus(),
            entity.getLastLoginAt(),
            entity.getSportProfiles()
        );
    }
}
