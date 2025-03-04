package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.dto.request.MatchCreateRequest;
import com.minsproject.matchpoint.dto.request.MatchListRequest;
import com.minsproject.matchpoint.dto.request.MatchResultRequest;
import com.minsproject.matchpoint.dto.request.QuickMatchCreate;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.MatchResult;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.MatchRepository;
import com.minsproject.matchpoint.repository.MatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final UserService userService;
    private final MatchRepository matchRepository;
    private final MatchResultRepository resultRepository;
    private final SportProfileService sportProfileService;

    public List<Match> list(MatchListRequest request) {
        User user = userService.getUserById(request.getUserId());
        List<SportProfile> profiles = sportProfileService.getProfilesByUser(user);
        profiles.stream()
                .filter(profile -> profile.getSportType() == request.getSportType())
                .findFirst()
                .orElseThrow(() -> new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE));

        return matchRepository.list(request);
    }

    public Match createQuickMatch(QuickMatchCreate request) {
        SportProfile inviter = sportProfileService.getProfileById(request.getInviterId());
        SportProfile invitee = sportProfileService.getProfileById(request.getInviteeId());

        inviter.validateSportType(request.getSportType());
        invitee.validateSportType(request.getSportType());

        return matchRepository.save(Match.createQuickMatch(inviter, invitee));
    }

    public Match createMatch(MatchCreateRequest request) {
        SportProfile inviter = sportProfileService.getProfileById(request.getInviterId());
        SportProfile invitee = sportProfileService.getProfileById(request.getInviteeId());

        inviter.validateSportType(request.getSportType());
        invitee.validateSportType(request.getSportType());

        return matchRepository.save(Match.createRegularMatch(inviter, invitee, request.getMatchDate()));
    }

    public Match getMatch(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));
    }

    @Transactional
    public boolean result(Long matchId, MatchResultRequest request) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));

        if (!(match.getStatus() == MatchStatus.ACCEPTED || match.getStatus() == MatchStatus.FINISHED)) {
            throw new MatchPointException(ErrorCode.MATCH_STATUS_NOT_ALLOWED);
        }

        if (match.getMatchDate().isAfter(LocalDateTime.now())) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_DAY);
        }

        if (!("win".equals(request.getResult()) || "lose".equals(request.getResult()))) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_RESULT);
        }

        if (!(match.getInviter().getId() == request.getSubmitterId() || (match.getInvitee().getId() == request.getSubmitterId()))) {
            throw new MatchPointException(ErrorCode.MATCH_RESULT_UNAUTHORIZED);
        }

        if (match.getResult() != null) {
            if ("win".equals(request.getResult())) {
                if (match.getResult().getWinner() != null) {
                    throw new MatchPointException(ErrorCode.RESULT_ALREADY_SUBMITTED);
                }
            }

            if ("lose".equals(request.getResult())) {
                if (match.getResult().getLoser() != null) {
                    throw new MatchPointException(ErrorCode.RESULT_ALREADY_SUBMITTED);
                }
            }
        }

        SportProfile submitter = match.getInviter().getId() == request.getSubmitterId() ? match.getInviter() : match.getInvitee();
        MatchResult result;
        if ("win".equals(request.getResult())) {
            result = MatchResult.builder()
                    .match(match)
                    .winner(submitter)
                    .build();
        } else {
            result = MatchResult.builder()
                    .match(match)
                    .loser(submitter)
                    .build();
        }

        MatchResult saved = resultRepository.save(result);

        if (saved.getWinner() != null && saved.getLoser() != null) {
            match.updateStatus(MatchStatus.CONFIRMED);
        }

        return true;
    }
}
