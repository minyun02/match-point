package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.User;
import lombok.Getter;

@Getter
public class JoinRequest {

    private String email;

    private String name;

    private String mobileNumber;

    private String socialLoginType;

    private String socialLoginId;

    public JoinRequest(String email, String name, String mobilNumber, String socialLoginType, String socialLoginId) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobilNumber;
        this.socialLoginType = socialLoginType;
        this.socialLoginId = socialLoginId;
    }

    public static User toEntity(JoinRequest req) {
        return User.builder()
                .email(req.getEmail())
                .name(req.getName())
                .build();
    }

//    public void setPasswordEncoded(String encodedPassword) {
//        this.password = encodedPassword;
//    }

}
