package com.minsproject.matchpoint.service

import com.minsproject.matchpoint.dto.TeamSearchDTO
import com.minsproject.matchpoint.dto.response.TeamResponse
import com.minsproject.matchpoint.entity.Sports
import com.minsproject.matchpoint.entity.Team
import com.minsproject.matchpoint.repository.TeamRepository
import spock.lang.Specification
import spock.lang.Subject


class MatchServiceTest extends Specification {

    def teamRepository = Mock(TeamRepository)

    @Subject
    def matchService = new MatchService(teamRepository)

    def "매칭이 가능한 팀 조회해오기"() {
        given:
        TeamSearchDTO searchDTO = new TeamSearchDTO(10, 0L, "서울시", "강남구", "신사동", 1L)
        def sports = new Sports(sportsId: 1L, name: "축구")
        def teamEntities = [
                new Team(teamId: 1L, sports: sports),
                new Team(teamId: 2L, sports: sports)
        ]
        def teamResponses = teamEntities.collect { TeamResponse.fromEntity(it) }

        when:
        teamRepository.findTeamsForMatch(searchDTO) >> teamEntities
        List<TeamResponse> result = matchService.getTeamList(searchDTO)

        then:
        result == teamResponses
    }


}