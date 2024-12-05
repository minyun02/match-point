package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.QMatch;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.minsproject.matchpoint.entity.QMatch.match;

@RequiredArgsConstructor
@Repository
public class MatchCustomRepositoryImpl implements MatchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Match> list(SportType sportType, String sort, Long lastId, Integer pageSize) {
        return queryFactory
                .selectFrom(match)
                .where(
                    sportTypeEq(sportType),
                    idGt(lastId)
                )
                .orderBy(createOrderSpecifier(sort, match))
                .limit(pageSize)
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(String sort, QMatch match) {
        if (sort == null) return match.id.desc();

        String[] parts = sort.split(",");
        String field = parts[0];
        boolean isAsc = parts.length > 1 && parts[1].equalsIgnoreCase("asc");

        return switch (field) {
            case "createdAt" -> isAsc ? match.createdAt.asc() : match.createdAt.desc();
            default -> match.id.desc();
        };
    }

    private Predicate idGt(Long lastId) {
        return lastId == null ? null : match.id.gt(lastId);
    }

    private Predicate sportTypeEq(SportType sportType) {
        return sportType == null ? null : match.sportType.eq(sportType);
    }
}
