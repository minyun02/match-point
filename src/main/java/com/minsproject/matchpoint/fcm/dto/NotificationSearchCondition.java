package com.minsproject.matchpoint.fcm.dto;

import com.minsproject.matchpoint.fcm.constant.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationSearchCondition {
    private Long userId;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long lastId;
    private Integer pageSize;
}