package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.MemberWithDistance;
import lombok.Getter;

@Getter
public class MemberWithDistanceResponse {

    private Long memberId;

    private String sportName;

    private String nickname;

    private String memberImage;

    private Integer level;

    private String city;

    private String district;

    private String neighborhood;

    private Double distance;

    private MemberWithDistanceResponse(Long memberId, String sportName, String nickname, String memberImage, Integer level, String city, String district, String neighborhood, Double distance) {
        this.memberId = memberId;
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
                member.getId(),
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
