package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Result;
import lombok.Getter;

@Getter
public class MatchResultRequest {

    private String resultType;

    private Long memberId;

    public static Result toEntity(Match match, String resultType, Member member) {
        return Result.builder()
                .match(match)
                .result(resultType)
                .member(member)
                .build();
    }
}
