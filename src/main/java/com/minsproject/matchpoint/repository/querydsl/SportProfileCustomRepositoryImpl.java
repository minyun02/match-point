package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.entity.ProfileWithInfo;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.minsproject.matchpoint.sport_profile.domain.QSportProfile.sportProfile;

@RequiredArgsConstructor
@Repository
public class SportProfileCustomRepositoryImpl implements SportProfileCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SportProfile> list(SportType sportType, String range, String address, Integer pageSize, Long lastId, String sort) {
        return queryFactory
                .selectFrom(sportProfile)
                .where(
                        sportTypeEq(sportType),
                        addressEq(range, address),
                        idGt(lastId)
                )
                .orderBy(createOrderSpecifier(sort))
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<ProfileWithInfo<SportProfile>> findProfileListForMatch(Long profileId, SportType sportType, Double latitude, Double longitude, String searchWord, String sort, Integer distance, Long lastId, Integer pageSize) {

        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(POINT({0}, {1}), POINT(sportProfile.longitude, sportProfile.latitude))",
                longitude,
                latitude
        );

        List<Tuple> results = queryFactory
                .select(sportProfile, distanceExpression)
                .from(sportProfile)
                .where(
                        sportTypeEq(sportType),
                        nickNameEq(searchWord),
                        idGt(lastId),
                        distanceExpression.loe(distance),
                        sportProfile.id.ne(profileId)
                )
                .orderBy(createOrderSpecifier(sort))
                .limit(pageSize)
                .fetch();

        return results.stream()
                .map(tuple -> new ProfileWithInfo<>(
                        tuple.get(sportProfile),
                        tuple.get(distanceExpression),
                        null
                ))
                .toList();
    }

    @Override
    public Integer findMaxRankingBySportType(SportType sportType) {
        return queryFactory
                .select(
                        sportProfile.ranking.max()
                )
                .from(sportProfile)
                .where(
                        sportTypeEq(sportType)
                )
                .fetchOne();
    }

    private Predicate nickNameEq(String searchWord) {
        return (searchWord.isEmpty()) ? null : sportProfile.nickname.in(searchWord);
    }

    private Predicate addressEq(String range, String address) {
        switch (range) {
            case "시" -> {
                return sportProfile.sido.eq(address);
            }
            case "구" -> {
                return sportProfile.sigungu.eq(address);
            }
            default -> {
                return sportProfile.dong.eq(address);
            }
        }
    }

    private OrderSpecifier<?> createOrderSpecifier(String sort) {
        if (sort == null) return sportProfile.id.desc();

        String[] parts = sort.split(",");
        String field = parts[0];
        boolean isAsc = parts.length > 1 && parts[1].equalsIgnoreCase("asc");

        if (field.equals("createdAt")) {
            return isAsc ? sportProfile.createdAt.asc() : sportProfile.createdAt.desc();
        }
        return sportProfile.id.desc();
    }

    private Predicate idGt(Long lastId) {
        return lastId < 1 ? null : sportProfile.id.gt(lastId);
    }

    private Predicate sportTypeEq(SportType sportType) {
        return sportType == null ? null : sportProfile.sportType.eq(sportType);
    }
}