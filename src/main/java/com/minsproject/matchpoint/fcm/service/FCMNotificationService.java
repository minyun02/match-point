package com.minsproject.matchpoint.fcm.service;

import com.google.firebase.messaging.*;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.fcm.dto.FCMNotificationRequest;
import com.minsproject.matchpoint.fcm.entity.NotificationHistory;
import com.minsproject.matchpoint.fcm.repository.NotificationHistoryRepository;
import com.minsproject.matchpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationService {
    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final NotificationHistoryRepository notificationHistoryRepository;

    public void sendNotification(FCMNotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.getTargetToken())
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .setImage(request.getImageUrl())
                        .build()
                )
                .putAllData(request.getData())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setSound("default")
                                .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                                .build()
                        )
                        .build()
                )
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .build())
                        .build()
                )
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent notifiaction: {}", response);

            // 알림 히스토리 저장
            saveNotificationHistory(request);

        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                handleInvalidToken(request.getTargetToken());
            }
            log.error("Failed to send FCM notification", e);
            throw new MatchPointException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public void sendNotificationToMultipleTokens(List<String> tokens, FCMNotificationRequest request) {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .setImage(request.getImageUrl())
                        .build()
                )
                .putAllData(request.getData())
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendMulticast(message);
            log.info("Sent messages: {}", response.getSuccessCount());

            if (response.getFailureCount() > 0) {
                handleFailedMessages(tokens, response.getResponses());
            }
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send mulitcat messgae", e);
            throw new MatchPointException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void handleInvalidToken(String token) {
        log.info("Removed invalid token: {}", token);
    }

    private void handleFailedMessages(List<String> tokens, List<SendResponse> responses) {
        for (int i = 0; i < responses.size(); i++) {
            if (!responses.get(i).isSuccessful()) {
                String failedToken = tokens.get(i);
                log.error("Failed to send message to token: {}", failedToken);
                if (responses.get(i).getException().getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    handleInvalidToken(failedToken);
                }
            }
        }
    }

    private void saveNotificationHistory(FCMNotificationRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new MatchPointException(ErrorCode.USER_NOT_FOUND));

        NotificationHistory history = NotificationHistory.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        notificationHistoryRepository.save(history);
    }
}
