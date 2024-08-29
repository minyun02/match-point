package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.dto.request.MatchSearchRequest;
import com.minsproject.matchpoint.entity.Match;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.minsproject.matchpoint.entity.QMatch.match;

@RequiredArgsConstructor
@Repository
public class MatchCustomRepositoryImpl implements MatchCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Match> findReceivedMatches(MatchSearchRequest request) {
        return jpaQueryFactory.selectFrom(match)
                .where(
                        match.invitee.id.eq(request.getMemberId()),
                        searchByInviterNickname(request.getInviterNickname()),
                        searchByStatus(request.getStatus()),
                        searchWithinDates(request.getStartDate(), request.getEndDate())
                )
                .fetch();
    }

    private BooleanExpression searchWithinDates(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate != null && endDate != null) {
            return match.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return match.createdAt.goe(startDate);
        } else if (endDate != null) {
            return match.createdAt.loe(endDate);
        }

        return null;
    }

    private BooleanExpression searchByStatus(MatchStatus status) {
        return status == null ? null : match.status.eq(status);
    }

    private BooleanExpression searchByInviterNickname(String inviterNickname) {
        return StringUtils.hasText(inviterNickname) ? match.inviter.nickname.contains(inviterNickname) : null;
    }
}
