package com.minsproject.matchpoint.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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

    @Column(columnDefinition = "GEOMETRY")
    private Point location;

    private boolean canMatch;

    @Builder
    private Member(User user, Sport sport, String nickname, String memberImage, Integer level, String city, String district, String neighborhood, Double latitude, Double longitude, boolean canMatch) {
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
        this.canMatch = canMatch;
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

    @PrePersist
    @PreUpdate
    private void updateLocation() {
        if (this.latitude != null && this.longitude != null) {
            GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
            this.location = factory.createPoint(new Coordinate(this.longitude, this.latitude));
        }
    }

    public boolean canMatch() {
        return this.canMatch;
    }
}
