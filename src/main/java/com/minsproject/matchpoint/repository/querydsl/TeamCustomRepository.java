package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.dto.TeamSearchDTO;
import com.minsproject.matchpoint.entity.Team;

import java.util.List;

public interface TeamCustomRepository {

    List<Team> findByTeamIdGreaterThanOffsetId(TeamSearchDTO searchDTO);

    List<Team> findTeamsForMatch(TeamSearchDTO searchDTO);
}
