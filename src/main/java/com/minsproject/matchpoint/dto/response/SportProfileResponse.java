package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.SportProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer points;
    private Integer totalMatches;
    private Integer wins;
    private Integer loses;
    private Integer ranking;

    public SportProfileResponse(Long id, Long userId, String nickname, String sportType, String sido, String sigungu, String dong, String detail, String fullAddress, Double latitude, Double longitude, String profileImage, Double mannerRate, Integer points, Integer totalMatches, Integer wins, Integer loses, Integer ranking) {
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
        this.ranking = ranking;
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
            entity.getRanking()
        );
    }

}
