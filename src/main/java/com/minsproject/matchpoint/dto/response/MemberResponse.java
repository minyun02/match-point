package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Member;

public class MemberResponse {

    private String sportName;

    private String nickname;

    private String memberImage;

    private Integer level;

    private String city;

    private String district;;

    private String neighborhood;

    private MemberResponse(String sportName, String nickname, String memberImage, Integer level, String city, String district, String neighborhood) {
        this.sportName = sportName;
        this.nickname = nickname;
        this.memberImage = memberImage;
        this.level = level;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
    }

    public static MemberResponse fromEntity(Member entity) {
        return new MemberResponse(
                entity.getSport().getName(),
                entity.getNickname(),
                entity.getMemberImage(),
                entity.getLevel(),
                entity.getCity(),
                entity.getDistrict(),
                entity.getNeighborhood()
        );
    }
}
