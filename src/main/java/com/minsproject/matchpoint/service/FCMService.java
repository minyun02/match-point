package com.minsproject.matchpoint.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.minsproject.matchpoint.dto.request.FcmMessageRequest;
import com.minsproject.matchpoint.dto.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FCMService {

    public int sendMessage(NotificationRequest request) throws IOException {
        String message = makeMessage(request);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity entity = new HttpEntity(message, headers);

        String url = "https://fcm.googleapis.com/v1/projects/nwitter-bd606/messages:send";
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getStatusCode() == HttpStatus.OK ? 1: 0;
    }

    private String makeMessage(NotificationRequest request) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmMessageRequest messageRequest = FcmMessageRequest.builder()
                .message(
                        FcmMessageRequest.Message.builder()
                                .token(request.getToken())
                                .notification(
                                        FcmMessageRequest.Notification.builder()
                                                .title(request.getTitle())
                                                .body(request.getBody())
                                                .image(null)
                                                .build()
                                )
                                .build()
                )
                .validateOnly(false)
                .build();

        return om.writeValueAsString(messageRequest);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/nwitter-bd606-firebase-adminsdk-ducd4-bdb2409d7f.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

}
