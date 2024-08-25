package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponse {

    private Long sportId;

    private String sportName;

    private String nickname;

    private String memberImage;

    private Integer level;

    private String city;

    private String district;

    private String neighborhood;

    private Double latitude;

    private Double longitude;

    private MemberResponse(Long sportId, String sportName, String nickname, String memberImage, Integer level, String city, String district, String neighborhood, Double latitude, Double longitude) {
        this.sportId = sportId;
        this.sportName = sportName;
        this.nickname = nickname;
        this.memberImage = memberImage;
        this.level = level;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static MemberResponse fromEntity(Member entity) {
        return new MemberResponse(
                entity.getSport().getId(),
                entity.getSport().getName(),
                entity.getNickname(),
                entity.getMemberImage(),
                entity.getLevel(),
                entity.getCity(),
                entity.getDistrict(),
                entity.getNeighborhood(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }
}