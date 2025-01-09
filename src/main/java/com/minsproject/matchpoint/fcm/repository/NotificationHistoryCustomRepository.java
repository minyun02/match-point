package com.minsproject.matchpoint.fcm.repository;

import com.minsproject.matchpoint.fcm.dto.NotificationSearchCondition;
import com.minsproject.matchpoint.fcm.entity.NotificationHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationHistoryCustomRepository {
    List<NotificationHistory> findNotifications(NotificationSearchCondition condition);

    Long countUnreadNotifications(Long userId);

    void deleteOldNotifications(Long userId, LocalDateTime dateThreshold);

    void markAllAsRead(Long userId);
}