package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Match;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchResponse {

    private Long matchId;

    private String inviter;

    private String invitee;

    private String sportName;

    private String city;

    private String district;

    private String neighborhood;

    private String detailAddress;

    private String placeName;

    private LocalDateTime matchDay;

    private String message;

    private String status;;

    private MatchResponse(Long matchId, String inviter, String invitee, String sportName, String city, String district, String neighborhood, String detailAddress, String placeName, LocalDateTime matchDay, String message, String status) {
        this.matchId = matchId;
        this.inviter = inviter;
        this.invitee = invitee;
        this.sportName = sportName;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.detailAddress = detailAddress;
        this.placeName = placeName;
        this.matchDay = matchDay;
        this.message = message;
        this.status = status;
    }

    public static MatchResponse fromEntity(Match entity) {
        return new MatchResponse(
                entity.getMatchId(),
                entity.getInviter().getNickname(),
                entity.getInvitee().getNickname(),
                entity.getSport().getName(),
                entity.getPlace().getCity(),
                entity.getPlace().getDistrict(),
                entity.getPlace().getNeighborhood(),
                entity.getPlace().getDetailAddress(),
                entity.getPlace().getName(),
                entity.getMatchDay(),
                entity.getMessage(),
                entity.getStatus().name()
        );
    }
}
