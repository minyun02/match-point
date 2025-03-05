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
import com.minsproject.matchpoint.sport_profile.domain.SportProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        SportProfiles profiles = new SportProfiles(sportProfileService.getProfilesByUser(user));
        profiles.validateSportType(request.getSportType());

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
    public void result(Long matchId, MatchResultRequest request) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new MatchPointException(ErrorCode.MATCH_NOT_FOUND));
        match.validateSubmitter(request.getSubmitterId());
        match.validateSubmitCondition();
        match.validateResultDuplication(request.getResult());

        SportProfile submitter = match.returnSubmitterProfile(request.getSubmitterId());
        MatchResult result = MatchResult.createFromResult(match, submitter, request.getResult());

        MatchResult saved = resultRepository.save(result);

        saved.confirmMatchIfPossible();
    }
}
