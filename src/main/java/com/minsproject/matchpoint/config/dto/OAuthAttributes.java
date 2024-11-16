package com.minsproject.matchpoint.config.dto;

import com.minsproject.matchpoint.constant.role.UserRole;
import com.minsproject.matchpoint.constant.status.UserStatus;
import com.minsproject.matchpoint.entity.User;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String provider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.provider = provider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(registrationId, userNameAttributeName, attributes);
            case "kakao" -> ofKakao(registrationId, userNameAttributeName, attributes);
            case "naver" -> ofNaver(registrationId, userNameAttributeName, attributes);
            default -> {
                try {
                    throw new AuthException("wrong registrationId");
                } catch (AuthException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .provider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String nickname = (String) profile.get("nickname");
        return OAuthAttributes.builder()
                .name(nickname)
                .email(nickname + attributes.get("id"))
                .provider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .provider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .role(UserRole.USER)
                .status(UserStatus.NORMAL)
                .build();
    }

}
