package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ToString
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

    @Setter
    @Enumerated(EnumType.STRING)
    private SportType currentSport;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Timestamp lastLoginAt;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<SportProfile> sportProfiles;

    @Builder
    public User(Long id,
                String email,
                String name,
                String provider,
                String providerId,
                SportType currentSport,
                UserRole role,
                UserStatus status,
                Timestamp lastLoginAt,
                List<SportProfile> sportProfiles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.currentSport = currentSport;
        this.role = role;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.sportProfiles = sportProfiles;
    }

    public User updateLastLoginDate() {
        this.lastLoginAt = Timestamp.valueOf(LocalDateTime.now());

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
