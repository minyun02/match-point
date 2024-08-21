package com.minsproject.matchpoint.dto;

import com.minsproject.matchpoint.constant.TeamMemberRole;
import com.minsproject.matchpoint.constant.status.TeamMemberStatus;
import com.minsproject.matchpoint.entity.Team;
import com.minsproject.matchpoint.entity.TeamMember;
import com.minsproject.matchpoint.entity.User;
import lombok.Getter;

@Getter
public class TeamMemberDTO {

    private Long teamMemberId;

    private Team team;

    private User user;

    private String role;

    private String status;


    public static TeamMember toEntity(Team team, User user, TeamMemberRole role, TeamMemberStatus status) {
        return TeamMember.builder()
                .team(team)
                .user(user)
                .role(role)
                .status(status)
                .build();
    }

}
