package com.minsproject.matchpoint.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class UserResponse implements UserDetails {

    private Long id;

    private String name;

    private String email;

    private String provider;

    private String providerId;

    private String currentSport;

    private UserRole role;

    private UserStatus status;

    private Timestamp lastLoginAt;

    private List<SportProfileResponse> sportProfiles;

    private UserResponse(Long id, String name, String email, String provider, String providerId, String currentSport, UserRole role, UserStatus status, Timestamp lastLoginAt, List<SportProfileResponse> sportProfiles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.currentSport = currentSport;
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
            entity.getCurrentSport(),
            entity.getRole(),
            entity.getStatus(),
            entity.getLastLoginAt(),
            entity.getSportProfiles().stream().map(SportProfileResponse::fromEntity).toList()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.name;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return false;
    }
}
