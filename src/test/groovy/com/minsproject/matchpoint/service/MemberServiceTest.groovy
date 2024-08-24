package com.minsproject.matchpoint.service

import com.minsproject.matchpoint.dto.request.MemberCreateRequest
import com.minsproject.matchpoint.dto.response.MemberResponse
import com.minsproject.matchpoint.entity.Member
import com.minsproject.matchpoint.entity.Sport
import com.minsproject.matchpoint.entity.User
import com.minsproject.matchpoint.exception.ErrorCode
import com.minsproject.matchpoint.exception.MatchPointException
import com.minsproject.matchpoint.repository.MemberRepository
import spock.lang.Specification
import spock.lang.Subject

class MemberServiceSpec extends Specification {

    @Subject
    MemberService memberService

    UserService userService = Mock()
    SportService sportService = Mock()
    MemberRepository memberRepository = Mock()

    def setup() {
        memberService = new MemberService(userService, sportService, memberRepository)
    }

    def "create-성공"() {
        given:
        def request = new MemberCreateRequest(userId: 1L, sportId: 2L, latitude: 37.5665, longitude: 126.9780)
        def user = new User(id: 1L)
        def sport = new Sport(id: 2L, name: "배드민턴")
        def newMember = new Member(id: 3L, user: user, sport: sport, latitude: 37.5665, longitude: 126.9780)

        when:
        def result = memberService.create(request)

        then:
        1 * userService.getUserById(1L) >> user
        1 * sportService.getSportById(2L) >> sport
        1 * memberRepository.save(_ as Member) >> newMember
        result instanceof MemberResponse
        result.sportName == "배드민턴"
        result.latitude == 37.5665
        result.longitude == 126.9780
    }

    def "create-운동 계정을 만드려는 사용자가 존재하지않으면 예외를 던져야 한다"() {
        given:
        def request = new MemberCreateRequest(userId: 999L, sportId: 2L, latitude: 37.5665, longitude: 126.9780)

        when:
        memberService.create(request)

        then:
        1 * userService.getUserById(999L) >> { throw new MatchPointException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.") }
        0 * sportService.getSportById(_)
        0 * memberRepository.save(_)
        thrown(MatchPointException)
    }

    def "create-운동 종목이 존재하지않으면 예외를 던져야 한다"() {
        given:
        def request = new MemberCreateRequest(userId: 1L, sportId: 999L, latitude: 37.5665, longitude: 126.9780)
        def user = new User(id: 1L)

        when:
        memberService.create(request)

        then:
        1 * userService.getUserById(1L) >> user
        1 * sportService.getSportById(999L) >> { throw new MatchPointException(ErrorCode.SPORT_NOT_FOUND, "스포츠를 찾을 수 없습니다.") }
        0 * memberRepository.save(_)
        thrown(MatchPointException)
    }

    def "create-운동 계정의 좌표 정보가 올바르지 않으면 예외를 던져야 한다"() {
        given:
        def request = new MemberCreateRequest(userId: 1L, sportId: 2L, latitude: 1000.0, longitude: 2000.0)
        def user = new User(id: 1L)
        def sport = new Sport(id: 2L)

        when:
        memberService.create(request)

        then:
        1 * userService.getUserById(1L) >> user
        1 * sportService.getSportById(2L) >> sport
        0 * memberRepository.save(_)
        def exception = thrown(MatchPointException)
        exception.errorCode == ErrorCode.INVALID_MATCH_PLACE
        exception.message == "올바른 위치 정보가 아닙니다."
    }
}