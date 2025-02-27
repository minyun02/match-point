package com.minsproject.matchpoint.sport_profile.domain;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.SportProfileDTO;
import com.minsproject.matchpoint.entity.BaseEntity;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private SportType sportType;

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

    public void isSameSportType(SportType sportType) {
        if (this.sportType != sportType) {
            throw new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE);
        }
    }
}