package com.minsproject.matchpoint.dto;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.dto.request.PlaceRequest;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.Team;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchDTO {

    @NotNull
    private Long inviterTeamId;

    @NotNull(message = "매칭 상대를 선택해주세요.")
    private Long inviteeTeamId;

    @NotNull(message = "매칭 장소를 선택해주세요.")
    private PlaceRequest place;

    @NotNull(message = "매칭 날짜를 선택해주세요.")
    private LocalDateTime matchDay;

    private MatchStatus status;

    public MatchDTO(Long inviterTeamId, Long inviteeTeamId, PlaceRequest place, LocalDateTime matchDay, MatchStatus status) {
        this.inviterTeamId = inviterTeamId;
        this.inviteeTeamId = inviteeTeamId;
        this.place = place;
        this.matchDay = matchDay;
        this.status = status;
    }

    public static Match toEntity(Team inviter, Team invitee, MatchDTO dto) {
        return new Match(
                inviter,
                invitee,
                PlaceRequest.toEntity(dto.getPlace()),
                dto.getMatchDay(),
                dto.getStatus()
        );
    }

}
