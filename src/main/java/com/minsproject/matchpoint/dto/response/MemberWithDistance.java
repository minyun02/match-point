package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Member;
import lombok.Getter;

@Getter
public class MemberWithDistance {

    private Member member;
    private Double distance;

    public MemberWithDistance(Member member, Double distance) {
        this.member = member;
        this.distance = distance;
    }
}
