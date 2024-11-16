package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String token;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Timestamp lastLoginAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<SportProfile> sportProfiles;

    @Builder
    public User(String name, String email, String provider, String providerId, UserRole role, UserStatus status) {
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.status = status;
    }

    public User updateLastLoginDate() {
        this.lastLoginAt = Timestamp.valueOf(LocalDateTime.now());

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
