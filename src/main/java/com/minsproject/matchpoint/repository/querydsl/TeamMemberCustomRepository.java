package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.entity.TeamMember;

import java.util.Optional;

public interface TeamMemberCustomRepository {
    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);
}
