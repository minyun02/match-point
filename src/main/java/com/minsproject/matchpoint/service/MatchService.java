package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.dto.request.*;
import com.minsproject.matchpoint.entity.*;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.MatchRepository;
import com.minsproject.matchpoint.repository.MemberRepository;
import com.minsproject.matchpoint.repository.ResultRepository;
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

    private final ResultRepository resultRepository;

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

    public Match viewMatch(Long matchId, Long memberId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));

        if (!isParticipant(memberId, match)) {
            throw new MatchPointException(ErrorCode.MATCH_VIEW_UNAUTHORIZED);
        }

        return match;
    }

    public Result result(Long matchId, MatchResultRequest request) {
        validateResultType(request.getResultType());

        Match match = matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));

        if (!isParticipant(request.getMemberId(), match)) {
            throw new MatchPointException(ErrorCode.MATCH_VIEW_UNAUTHORIZED);
        }

        if (!match.isFinished()) {
            throw new MatchPointException(ErrorCode.MATCH_NOT_FINISHED);
        }

        boolean haveSubmittedResult = resultRepository.findByMatchIdAndMemberId(matchId, request.getMemberId()).isPresent();
        if (haveSubmittedResult) {
            throw new MatchPointException(ErrorCode.RESULT_ALREADY_SUBMITTED);
        }

        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new MatchPointException(ErrorCode.MEMBER_NOT_FOUND));

        return resultRepository.save(MatchResultRequest.toEntity(match, request.getResultType(), member));
    }

    private void validateResultType(String resultType) {
        if ("W".equals(resultType) || "L".equals(resultType)) {
            return;
        }
        throw new MatchPointException(ErrorCode.RESULT_TYPE_INVALID);
    }

    private boolean isParticipant(Long memberId, Match match) {
        long inviterId = match.getInviter().getId();
        long inviteeId = match.getInvitee().getId();

        return memberId == inviterId || memberId == inviteeId;
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
