package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.dto.request.MemberCreateRequest;
import com.minsproject.matchpoint.dto.response.MemberResponse;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final UserService userService;
    private final SportService sportService;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse create(MemberCreateRequest request) {
        User user = userService.getUserById(request.getUserId());
        Sport sport = sportService.getSportById(request.getSportId());

        validateDuplicateMember(user, sport);
        validateNickname(request.getNickname());
        validateLevel(request.getLevel());
        validateLocation(request.getLatitude(), request.getLongitude());

        Member newMember = memberRepository.save(request.toEntity(user, sport));

        return MemberResponse.fromEntity(newMember);
    }

    public MemberResponse getMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(MemberResponse::fromEntity)
                .orElseGet(MemberResponse::new);
    }

    public MemberResponse getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::fromEntity)
                .orElseThrow(() -> new MatchPointException(ErrorCode.TEAM_MEMBER_NOT_FOUND));
    }

    private void validateLocation(Double latitude, Double longitude) {
        if (!Member.isValidLocation(latitude, longitude)) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_PLACE, "올바른 위치 정보가 아닙니다.");
        }
    }

    private void validateLevel(Integer level) {
        if (level < 1 || level > 100) {
            throw new MatchPointException(ErrorCode.INVALID_LEVEL);
        }
    }

    private void validateNickname(String nickname) {
        if (getMemberByNickname(nickname).getNickname() != null) {
            throw new MatchPointException(ErrorCode.MEMBER_DUPLICATED_NICKNAME);
        }
    }

    private void validateDuplicateMember(User user, Sport sport) {
        if (memberRepository.findByUserAndSport(user, sport).isPresent()) {
            throw new MatchPointException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
    }
}
