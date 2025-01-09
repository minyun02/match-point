package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.Match;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchResponse {

    private Long id;
    private SportProfileResponse inviter;
    private SportProfileResponse invitee;
    private SportType sportType;
    private MatchStatus status;
    private LocalDateTime matchDate;
    private LocalDateTime canceledAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime confirmedAt;
    private MatchResultResponse result;

    private MatchResponse(Long id, SportProfileResponse inviter, SportProfileResponse invitee, SportType sportType, MatchStatus status, LocalDateTime matchDate, LocalDateTime canceledAt, LocalDateTime acceptedAt, LocalDateTime rejectedAt, LocalDateTime confirmedAt, MatchResultResponse result) {
        this.id = id;
        this.inviter = inviter;
        this.invitee = invitee;
        this.sportType = sportType;
        this.status = status;
        this.matchDate = matchDate;
        this.canceledAt = canceledAt;
        this.acceptedAt = acceptedAt;
        this.rejectedAt = rejectedAt;
        this.confirmedAt = confirmedAt;
        this.result = result;
    }

    public static MatchResponse fromEntity(Match entity) {
        return new MatchResponse(
                entity.getId(),
                SportProfileResponse.fromEntity(entity.getInviter()),
                SportProfileResponse.fromEntity(entity.getInvitee()),
                entity.getSportType(),
                entity.getStatus(),
                entity.getMatchDate(),
                entity.getCanceledAt(),
                entity.getAcceptedAt(),
                entity.getRejectedAt(),
                entity.getConfirmedAt(),
                entity.getResult() == null ? null : MatchResultResponse.fromEntity(entity.getResult())
        );
    }
}
