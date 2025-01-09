package com.minsproject.matchpoint.fcm.repository;

import com.minsproject.matchpoint.fcm.constant.NotificationType;
import com.minsproject.matchpoint.fcm.dto.NotificationSearchCondition;
import com.minsproject.matchpoint.fcm.entity.NotificationHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.minsproject.matchpoint.fcm.entity.QNotificationHistory.notificationHistory;

@Repository
@RequiredArgsConstructor
public class NotificationHistoryRepositoryImpl implements NotificationHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationHistory> findNotifications(NotificationSearchCondition condition) {
        return queryFactory
                .selectFrom(notificationHistory)
                .where(
                        userIdEq(condition.getUserId()),
                        typeEq(condition.getType()),
                        isReadEq(condition.getIsRead()),
                        dateBetween(condition.getStartDate(), condition.getEndDate()),
                        idGt(condition.getLastId())
                )
                .orderBy(notificationHistory.sentAt.desc())
                .limit(condition.getPageSize())
                .fetch();
    }

    @Override
    public Long countUnreadNotifications(Long userId) {
        return queryFactory
                .select(notificationHistory.count())
                .from(notificationHistory)
                .where(
                        notificationHistory.user.id.eq(userId),
                        notificationHistory.isRead.eq(false)
                )
                .fetchOne();
    }

    @Override
    public void deleteOldNotifications(Long userId, LocalDateTime dateThreshold) {
        queryFactory
                .delete(notificationHistory)
                .where(
                        notificationHistory.user.id.eq(userId),
                        notificationHistory.sentAt.before(dateThreshold)
                )
                .execute();
    }

    @Override
    public void markAllAsRead(Long userId) {
        queryFactory
                .update(notificationHistory)
                .set(notificationHistory.isRead, true)
                .where(
                        notificationHistory.user.id.eq(userId),
                        notificationHistory.isRead.eq(false)
                )
                .execute();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : notificationHistory.user.id.eq(userId);
    }

    private BooleanExpression typeEq(NotificationType type) {
        return type == null ? null : notificationHistory.type.eq(type);
    }

    private BooleanExpression isReadEq(Boolean isRead) {
        return isRead == null ? null : notificationHistory.isRead.eq(isRead);
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return notificationHistory.sentAt.between(startDate, endDate);
        } else if (startDate != null) {
            return notificationHistory.sentAt.goe(startDate);
        } else if (endDate != null) {
            return notificationHistory.sentAt.loe(endDate);
        }
        return null;
    }

    private BooleanExpression idGt(Long lastId) {
        return lastId == null ? null : notificationHistory.id.gt(lastId);
    }
}