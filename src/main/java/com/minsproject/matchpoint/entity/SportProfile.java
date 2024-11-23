package com.minsproject.matchpoint.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class SportProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String nickname;

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

    private Integer ranking;


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
}
