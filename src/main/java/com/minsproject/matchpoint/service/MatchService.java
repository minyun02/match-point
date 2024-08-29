package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.dto.MatchRequest;
import com.minsproject.matchpoint.dto.request.MatchSearchRequest;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Place;
import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.MatchRepository;
import com.minsproject.matchpoint.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final SportService sportService;

    private final PlaceService placeService;

    private final MemberRepository memberRepository;

    private final MatchRepository matchRepository;

    @Transactional
    public Match createMatch(MatchRequest request) {
        validateMatchDay(request);

        Member inviter = getMember(request.getInviterId(), request.getSportId());
        Member invitee = getMember(request.getInviteeId(), request.getSportId());
        validateMatchStatus(inviter, invitee);

        Sport sport = sportService.getSportById(request.getSportId());
        Place place = placeService.getPlaceById(request.getPlaceId());

        Match match = MatchRequest.toEntity(inviter, invitee, sport, place, request);

        return matchRepository.save(match);
    }

    public List<Match> getReceivedMatches(MatchSearchRequest request) {
        return matchRepository.findReceivedMatches(request);
    }

    private void validateMatchStatus(Member inviter, Member invitee) {
        if (!inviter.canMatch()) {
            throw new MatchPointException(ErrorCode.MATCH_INVITER_CANNOT_MATCH);
        }

        if (!invitee.canMatch()) {
            throw new MatchPointException(ErrorCode.MATCH_INVITEE_CANNOT_MATCH);
        }
    }

    private Member getMember(Long memberId, Long sportId) {
        return memberRepository.findByIdAndSportId(memberId, sportId).orElseThrow(() -> new MatchPointException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateMatchDay(MatchRequest request) {
        if (request.getMatchDay().isBefore(LocalDateTime.now())) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_DAY);
        }
    }
}
