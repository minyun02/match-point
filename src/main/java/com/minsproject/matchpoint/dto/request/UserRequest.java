package com.minsproject.matchpoint.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minsproject.matchpoint.constant.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserRequest implements UserDetails {

    private Long userId;

    private String email;

    private String name;

    private String password;

    private String mobileNumber;

    private String socialLoginType;

    private String socialLoginId;

    private UserRole role;

    private UserStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    private UserRequest(Long userId, String email, String password, String name, String mobileNumber, String socialLoginType, String socialLoginId, UserRole role, UserStatus status, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.socialLoginType = socialLoginType;
        this.socialLoginId = socialLoginId;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static UserRequest fromEntity(User entity) {
        return UserRequest.builder()
                .userId(entity.getId())
                .email(entity.getEmail())
                .role(entity.getRole())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.password;
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
