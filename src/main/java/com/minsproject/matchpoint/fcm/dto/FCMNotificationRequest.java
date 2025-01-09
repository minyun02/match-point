package com.minsproject.matchpoint.fcm.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class FCMNotificationRequest {
    private Long userId;
    private String targetToken;
    private String title;
    private String body;
    private Map<String, String> data;
    private String imageUrl;
}