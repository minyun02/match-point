package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.entity.SportProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class SportProfileResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String sportType;
    private String sido;
    private String sigungu;
    private String dong;
    private String detail;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
    private String profileImage;
    private Double mannerRate = 5.0;
    private Integer points = 0;
    private Integer totalMatches;
    private Integer wins;
    private Integer loses;
    private BigDecimal winRate;
    private Integer ranking;
    private Double distance;

    private SportProfileResponse(Long id, Long userId, String nickname, String sportType, String sido, String sigungu, String dong, String detail, String fullAddress, Double latitude, Double longitude, String profileImage, Double mannerRate, Integer points, Integer totalMatches, Integer wins, Integer loses, BigDecimal winRate, Integer ranking) {
        this.id = id;
        this.userId = userId;
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
        this.winRate = winRate;
        this.ranking = ranking;
    }

    private SportProfileResponse(Long id, Long userId, String nickname, String sportType, String sido, String sigungu, String dong, String detail, String fullAddress, Double latitude, Double longitude, String profileImage, Double mannerRate, Integer points, Integer totalMatches, Integer wins, Integer loses, BigDecimal winRate, Integer ranking, Double distance) {
        this.id = id;
        this.userId = userId;
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
        this.winRate = winRate;
        this.ranking = ranking;
        this.distance = distance;
    }

    public static SportProfileResponse fromEntity(SportProfile entity) {
        return new SportProfileResponse(
            entity.getId(),
            entity.getUser().getId(),
            entity.getNickname(),
            entity.getSportType(),
            entity.getSido(),
            entity.getSigungu(),
            entity.getDong(),
            entity.getDetail(),
            entity.getFullAddress(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getProfileImage(),
            entity.getMannerRate(),
            entity.getPoints(),
            entity.getTotalMatches(),
            entity.getWins(),
            entity.getLoses(),
            entity.getWinRate(),
            entity.getRanking()
        );
    }

    public static SportProfileResponse of(ProfileWithInfo profile) {
        SportProfile entity = (SportProfile) profile.getEntity();
        return new SportProfileResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getNickname(),
                entity.getSportType(),
                entity.getSido(),
                entity.getSigungu(),
                entity.getDong(),
                entity.getDetail(),
                entity.getFullAddress(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getProfileImage(),
                entity.getMannerRate(),
                entity.getPoints(),
                entity.getTotalMatches(),
                entity.getWins(),
                entity.getLoses(),
                entity.getWinRate(),
                entity.getRanking(),
                profile.getDistance()
        );
    }
}