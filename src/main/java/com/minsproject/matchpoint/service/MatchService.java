package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.dto.MatchRequest;
import com.minsproject.matchpoint.dto.request.MatchRespondRequest;
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
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final SportService sportService;

    private final PlaceService placeService;

    private final MemberRepository memberRepository;

    private final MatchRepository matchRepository;

    @Transactional
    public Match createMatch(MatchRequest request) {
        validateMatchDay(request.getMatchDay());

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

    public Match modifyStatus(Long matchId, MatchRespondRequest request) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));
        validateMatchDay(match.getMatchDay());

        Long memberId = request.getMemberId();
        MatchStatus status = request.getStatus();
        switch (status) {
            case CANCELED -> validateMember(match.getInviter().getId(), memberId);
            case ACCEPTED, REJECTED -> validateMember(match.getInvitee().getId(), memberId);
            default -> throw new MatchPointException(ErrorCode.MATCH_INVALID_STATUS);
        }

        match.updateStatus(status);

        return matchRepository.save(match);
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

    private void validateMatchDay(LocalDateTime matchDay) {
        if (matchDay.isBefore(LocalDateTime.now())) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_DAY);
        }
    }

    private void validateMember(Long memberIdFromMatch, Long memberId) {
        if (!Objects.equals(memberIdFromMatch, memberId)) {
            throw new MatchPointException(ErrorCode.MATCH_RESPOND_NOT_ALLOWED);
        }
    }
}
