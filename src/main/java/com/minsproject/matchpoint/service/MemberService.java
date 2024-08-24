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

@RequiredArgsConstructor
@Service
public class MemberService {

    private final UserService userService;
    private final SportService sportService;
    private final MemberRepository memberRepository;

    public MemberResponse create(MemberCreateRequest request) {
        User user = userService.getUserById(request.getUserId());
        Sport sport = sportService.getSportById(request.getSportId());

        if (!Member.isValidLocation(request.getLatitude(), request.getLongitude())) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_PLACE, "올바른 위치 정보가 아닙니다.");
        }

        Member newMember = memberRepository.save(request.toEntity(user, sport));

        return MemberResponse.fromEntity(newMember);
    }
}
