package com.minsproject.matchpoint.fcm.entity;

import com.minsproject.matchpoint.entity.BaseEntity;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.fcm.constant.NotificationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class NotificationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(columnDefinition = "TEXT")
    private String extraData;

    @Builder
    public NotificationHistory(User user, String title, String body, LocalDateTime sentAt, boolean isRead, NotificationType type, String extraData) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.type = type;
        this.extraData = extraData;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}