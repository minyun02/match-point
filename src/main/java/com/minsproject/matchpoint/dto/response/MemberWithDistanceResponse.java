package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Member;
import lombok.Getter;

@Getter
public class MemberWithDistanceResponse {

    private String sportName;

    private String nickname;

    private String memberImage;

    private Integer level;

    private String city;

    private String district;

    private String neighborhood;

    private Double distance;

    private MemberWithDistanceResponse(String sportName, String nickname, String memberImage, Integer level, String city, String district, String neighborhood, Double distance) {
        this.sportName = sportName;
        this.nickname = nickname;
        this.memberImage = memberImage;
        this.level = level;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.distance = distance;
    }

    public static MemberWithDistanceResponse fromEntity(MemberWithDistance entity) {
        Member member = entity.getMember();
        return new MemberWithDistanceResponse(
                member.getSport().getName(),
                member.getNickname(),
                member.getMemberImage(),
                member.getLevel(),
                member.getCity(),
                member.getDistrict(),
                member.getNeighborhood(),
                entity.getDistance()
        );
    }
}
