package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.TeamMemberRole;
import com.minsproject.matchpoint.constant.status.TeamMemberStatus;
import com.minsproject.matchpoint.constant.status.TeamStatus;
import com.minsproject.matchpoint.dto.TeamMemberDTO;
import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.dto.response.TeamMemberResponse;
import com.minsproject.matchpoint.entity.Team;
import com.minsproject.matchpoint.entity.TeamMember;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.LeagueCustomException;
import com.minsproject.matchpoint.repository.TeamMemberRepository;
import com.minsproject.matchpoint.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final UserService userService;

    private final TeamRepository teamRepository;

    private final TeamMemberRepository teamMemberRepository;

    public Long create(Long teamId, UserDTO user) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new LeagueCustomException(ErrorCode.TEAM_NOT_FOUND));
        if (team.getStatus() == TeamStatus.PAUSED) {
            throw new LeagueCustomException(ErrorCode.TEAM_NOT_ACCEPTING_MEMBER);
        }

        List<TeamMember> teamMembers = team.getTeamMembers();
        boolean isUserInTeam = teamMembers.stream().anyMatch(member -> Objects.equals(member.getUser().getId(), user.getUserId()));
        if (isUserInTeam) {
            throw new LeagueCustomException(ErrorCode.ALREADY_IN_TEAM);
        }

        User userInfo = userService.getUserById(user.getUserId());

        TeamMember saved = teamMemberRepository.save(TeamMemberDTO.toEntity(team, userInfo, TeamMemberRole.NORMAL, TeamMemberStatus.NORMAL));

        return saved.getTeamMemberId();
    }

    public TeamMemberResponse modify(TeamMemberDTO teamMemberDTO, UserDTO userDTO) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberDTO.getTeamMemberId()).orElseThrow(() -> new LeagueCustomException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        if (teamMember.isOwnerOrMyself(userDTO.getUserId())) {
            teamMember.modify(teamMemberDTO);
            return TeamMemberResponse.fromEntity(teamMemberRepository.save(teamMember));
        }

        throw new LeagueCustomException(ErrorCode.MODIFICATION_NOT_ALLOWED);
    }

    @Transactional
    public void delete(Long teamMemberId, UserDTO userDTO) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId).orElseThrow(() -> new LeagueCustomException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        if (teamMember.isOwnerOrMyself(userDTO.getUserId())) {
            teamMember.delete();
            teamMemberRepository.save(teamMember);
            return;
        }

        throw new LeagueCustomException(ErrorCode.MODIFICATION_NOT_ALLOWED);
    }
}
