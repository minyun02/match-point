package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.dto.request.MatchCreateRequest;
import com.minsproject.matchpoint.dto.request.MatchResultRequest;
import com.minsproject.matchpoint.dto.request.QuickMatchCreate;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.MatchResult;
import com.minsproject.matchpoint.entity.SportProfile;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.MatchRepository;
import com.minsproject.matchpoint.repository.MatchResultRepository;
import com.minsproject.matchpoint.repository.SportProfileRepository;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchResultRepository resultRepository;
    private final SportProfileService sportProfileService;
    private final SportProfileRepository sportProfileRepository;


    public List<Match> list(Long userId, String sportType, String sort, Long lastId, Integer pageSize) {
        userRepository.findById(userId).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));

        SportType selectedSportType = Arrays.stream(SportType.values())
                .filter(type1 -> StringUtils.equals(type1.getName(), sportType))
                .findFirst()
                .orElse(null);

        return matchRepository.list(userId, selectedSportType, sort, lastId, pageSize);
    }

    public Match createQuickMatch(QuickMatchCreate request) {
        SportProfile inviter = sportProfileService.getProfileById(request.getInviterId());
        SportProfile invitee = sportProfileService.getProfileById(request.getInviteeId());

        /*
        TODO: 스포츠타입을 string으로 받는데 이걸 enum으로 받으면 처음부터 존재하는 스포츠타입인지 확인할수있지않을까?
         */
        boolean hasSportType = Arrays.stream(SportType.values())
                .filter(sport -> sport.getName().equals(request.getSportType()))
                .findFirst()
                .isEmpty();
        if (hasSportType) {
            throw new MatchPointException(ErrorCode.SPORT_NOT_FOUND);
        }

        /*
        TODO: 2/26/25
            매칭 신청자와 수락자의 종목이 같은지 검사하는거니까 프로필 엔티티에게 이 역할을 맡겨야할거같다.
        */
        if (!StringUtils.equals(inviter.getSportType(), request.getSportType())) {
            throw new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE);
        }

        if (!StringUtils.equals(inviter.getSportType(), invitee.getSportType())) {
            throw new MatchPointException(ErrorCode.CANNOT_MATCH_WITH_PROFILE);
        }

        return matchRepository.save(
            Match.createQuickMatch(
                inviter,
                invitee,
                Arrays.stream(SportType.values()).filter(sportType -> StringUtils.equals(sportType.getName(), request.getSportType())).findFirst().get(),
                MatchStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );
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

    public Match createMatch(MatchCreateRequest request) {
        SportProfile inviter = sportProfileRepository.findById(request.getInviterId()).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));

        if (!request.getSportType().getName().equals(inviter.getSportType())) {
            throw new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE);
        }

        SportProfile invitee = sportProfileRepository.findById(request.getInviteeId()).orElseThrow(() -> new MatchPointException(ErrorCode.PROFILE_NOT_FOUND));

        if (!request.getSportType().getName().equals(invitee.getSportType())) {
            throw new MatchPointException(ErrorCode.INCORRECT_SPORT_TYPE);
        }

        Match match = Match.builder()
                .inviter(inviter)
                .invitee(invitee)
                .matchDate(request.getMatchDate())
                .sportType(request.getSportType())
                .status(MatchStatus.PENDING)
                .build();

        return matchRepository.save(match);
    }
}
