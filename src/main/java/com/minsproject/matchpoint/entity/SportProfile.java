package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@NoArgsConstructor
@Getter
public class SportProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter private String nickname;

    private String sportType;

    private String sido;

    private String sigungu;

    private String dong;

    private String detail;

    private String fullAddress;

    private Double latitude;

    private Double longitude;

    @Setter
    private String profileImage;

    private Double mannerRate = 5.0;

    private Integer points;

    private Integer totalMatches;

    private Integer wins;

    private Integer loses;

    @Column(precision = 4, scale = 1)
    private BigDecimal winRate;

    @Setter private Integer ranking;


    @Builder
    public SportProfile(Long id, User user, String nickname, String sportType, String sido, String sigungu, String dong, String detail, String fullAddress, Double latitude, Double longitude, String profileImage, Double mannerRate, Integer points, Integer totalMatches, Integer wins, Integer loses, Integer ranking) {
        this.id = id;
        this.user = user;
        this.nickname = nickname;
        this.sportType = sportType;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dong = dong;
        this.detail = detail;
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImage = profileImage;
        this.mannerRate = mannerRate;
        this.points = points;
        this.totalMatches = totalMatches;
        this.wins = wins;
        this.loses = loses;
        this.ranking = ranking;
    }

    public void calculateWinRate() {
        if (wins + loses == 0) {
            this.winRate = BigDecimal.ZERO;
            return;
        }

        this.winRate = BigDecimal.valueOf(wins)
                .divide(BigDecimal.valueOf(wins + loses), 1, RoundingMode.HALF_UP);
    }

    public void updateProfileAddress(SportProfileDTO request) {
        this.sido = request.getSido();
        this.sigungu = request.getSigungu();
        this.dong = request.getDong();
        this.detail = request.getDetail();
        this.fullAddress = request.getFullAddress();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
    }
}