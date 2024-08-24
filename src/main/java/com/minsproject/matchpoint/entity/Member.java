package com.minsproject.matchpoint.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Column(nullable = false)
    private String nickname;

    private String memberImage;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    private Member(User user, Sport sport, String nickname, String memberImage, Integer level, String city, String district, String neighborhood, Double latitude, Double longitude) {
        this.user = user;
        this.sport = sport;
        this.nickname = nickname;
        this.memberImage = memberImage;
        this.level = level;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static boolean isValidLocation(Double latitude, Double longitude) {
        return isValidLatitude(latitude) && isValidLongitude(longitude);
    }

    private static boolean isValidLatitude(Double latitude) {
        return latitude != null && latitude >= -90 && latitude <=90;
    }

    private static boolean isValidLongitude(Double longitude) {
        return longitude != null && longitude >= -180 && longitude <=180;
    }
}
