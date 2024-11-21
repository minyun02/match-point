package com.minsproject.matchpoint.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SignupRequest {

    private String provider;
    private String providerId;
    private String email;
    private String name;
    private List<SportProfileDTO> sportProfiles;

}