package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Result;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResultResponse {

    private String sportName;

    private String placeName;

    private LocalDateTime matchDay;

    private String memberImage;

    private String nickname;

    private String result;

    private ResultResponse(String sportName, String placeName, LocalDateTime matchDay, String memberImage, String nickname, String result) {
        this.sportName = sportName;
        this.placeName = placeName;
        this.matchDay = matchDay;
        this.memberImage = memberImage;
        this.nickname = nickname;
        this.result = result;
    }

    public static ResultResponse fromEntity(Result entity) {
        Match match = entity.getMatch();
        Member member = entity.getMember();
        return new ResultResponse(
                match.getSport().getName(),
                match.getPlace().getName(),
                match.getMatchDay(),
                member.getMemberImage(),
                member.getNickname(),
                entity.getResult()
        );
    }
}
