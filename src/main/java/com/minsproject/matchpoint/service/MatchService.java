package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.dto.MatchDTO;
import com.minsproject.matchpoint.dto.TeamSearchDTO;
import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.dto.response.TeamResponse;
import com.minsproject.matchpoint.entity.Team;
import com.minsproject.matchpoint.entity.TeamMember;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.MatchRepository;
import com.minsproject.matchpoint.repository.TeamMemberRepository;
import com.minsproject.matchpoint.repository.TeamRepository;
import com.minsproject.matchpoint.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final TeamRepository teamRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final MatchRepository matchRepository;

    private final MatchValidator matchValidator;

    public List<TeamResponse> getTeamList(TeamSearchDTO searchDTO) {
        return teamRepository.findTeamsForMatch(searchDTO).stream().map(TeamResponse::fromEntity).toList();
    }

    public Long createMatch(MatchDTO matchDTO, UserDTO userDTO) {

        matchValidator.validateMatchDay(matchDTO.getMatchDay());

        matchValidator.validatePlace(matchDTO.getPlace());

        TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserId(matchDTO.getInviterTeamId(), userDTO.getUserId()).orElseThrow(() -> new LeagueCustomException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        matchValidator.validateTeamMemberRole(teamMember);

        Team inviter = teamMember.getTeam();
        Team invitee = teamRepository.findById(matchDTO.getInviteeTeamId()).orElseThrow(() -> new LeagueCustomException(ErrorCode.TEAM_NOT_FOUND));

        matchValidator.validateTeamAddress(inviter.getFullAddress(), invitee.getFullAddress());

        matchValidator.validateTeamStatus(invitee.getStatus());

        return matchRepository.save(MatchDTO.toEntity(inviter, invitee, matchDTO)).getMatchId();
    }

}
