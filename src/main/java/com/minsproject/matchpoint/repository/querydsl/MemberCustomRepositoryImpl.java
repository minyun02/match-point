package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.dto.request.MemberSearchRequest;
import com.minsproject.matchpoint.entity.MemberWithDistance;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.minsproject.matchpoint.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberWithDistance> findAvailableMembers(MemberSearchRequest request) {
        Long memberId = request.getMemberId();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        Integer radius = request.getRadius();
        Long sportId = request.getSportId();
        Integer minLevel = request.getMinLevel();
        Integer maxLevel = request.getMaxLevel();
        Long lastMemberId = request.getLastMemberId();
        Integer pageSize = request.getPageSize();

        return jpaQueryFactory
                .select(
                        Projections.constructor(MemberWithDistance.class,
                                member,
                                Expressions.numberTemplate(Double.class,
                                        "CAST(ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {1}, ' ', {2}, ')'), 4326)) AS DOUBLE) / 1000.0",
                                        member.location, latitude, longitude).as("distance")
                        ))
                .from(member)
                .where(
                        member.id.ne(memberId),
                        member.sport.id.eq(sportId),
                        withinDistance(latitude, longitude, radius),
                        levelBetween(minLevel, maxLevel),
                        member.id.gt(lastMemberId)
                )
                .orderBy(
                        Expressions.numberTemplate(
                                Double.class,
                                "ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {1}, ' ', {2}, ')'), 4326))",
                                member.location,  latitude, longitude).asc()
                )
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression withinDistance(Double latitude, Double longitude, Integer radius) {
        return Expressions.booleanTemplate(
                "CAST(ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {1}, ' ', {2}, ')'), 4326)) AS DOUBLE) <= {3} * 1000",
                member.location, latitude, longitude, radius
        );
    }

    private BooleanExpression levelBetween(Integer minLevel, Integer maxLevel) {
        if (minLevel != null && maxLevel != null) {
            return member.level.between(minLevel, maxLevel);
        }

        if (minLevel != null) {
            return member.level.goe(minLevel);
        }

        if (maxLevel != null) {
            return member.level.loe(maxLevel);
        }

        return null;
    }
}
