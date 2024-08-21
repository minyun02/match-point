package com.minsproject.matchpoint.validator;

import com.minsproject.matchpoint.constant.TeamMemberRole;
import com.minsproject.matchpoint.constant.status.TeamStatus;
import com.minsproject.matchpoint.dto.PlaceDTO;
import com.minsproject.matchpoint.entity.TeamMember;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class MatchValidator {
    public void validateMatchDay(LocalDateTime matchDay) {
        if (matchDay.isBefore(LocalDateTime.now())) {
            throw new LeagueCustomException(ErrorCode.INVALID_MATCH_DAY);
        }
    }

    public void validatePlace(PlaceDTO place) {
        if (place.getPlaceId() == null && (Stream.of(
                    place.getCity()
                    , place.getTown()
                    , place.getDong()
                    , place.getDetailAddress()
            ).anyMatch(Objects::isNull))) {
                throw new LeagueCustomException(ErrorCode.INVALID_MATCH_PLACE);

        }
    }

    public void validateTeamMemberRole(TeamMember teamMember) {
        if (teamMember.getRole() == TeamMemberRole.NORMAL) {
            throw new LeagueCustomException(ErrorCode.MATCH_INVITE_NOT_ALLOWED);
        }
    }

    public void validateTeamAddress(String inviterAddress, String inviteeAddress) {
        if (!Objects.equals(inviterAddress, inviteeAddress)) {
            throw new LeagueCustomException(ErrorCode.ADDRESS_NOT_SUITABLE_FOR_MATCH);
        }
    }

    public void validateTeamStatus(TeamStatus status) {
        if (status == TeamStatus.PAUSED) {
            throw new LeagueCustomException(ErrorCode.TEAM_NOT_ACCEPTING_MATCHES);
        }
    }
}
