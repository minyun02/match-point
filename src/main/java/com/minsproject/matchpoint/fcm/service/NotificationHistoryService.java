package com.minsproject.matchpoint.fcm.service;

import com.minsproject.matchpoint.fcm.dto.NotificationSearchCondition;
import com.minsproject.matchpoint.fcm.entity.NotificationHistory;
import com.minsproject.matchpoint.fcm.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationHistoryService {
    private final NotificationHistoryRepository historyRepository;

    public List<NotificationHistory> getUserNotifications(NotificationSearchCondition condition) {
        return historyRepository.findNotifications(condition);
    }

    public Long getUnreadNotifications(Long userId) {
        return historyRepository.countUnreadNotifications(userId);
    }

    @Transactional
    public void deleteOldNotifications(Long userId, LocalDateTime dateThreshold) {
        historyRepository.deleteOldNotifications(userId, dateThreshold);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        historyRepository.markAllAsRead(userId);
    }
}