package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.NotificationRequest;
import com.minsproject.matchpoint.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final FCMService fcmService;

    @PostMapping("/send")
    public int sendMessage(@RequestBody NotificationRequest request) throws IOException {
        return fcmService.sendMessage(request);
    }
}
