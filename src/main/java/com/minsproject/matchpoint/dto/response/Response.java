package com.minsproject.matchpoint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode;

    private T result;

    public static Response<String> error(String errorCode, String message) {
        return new Response<>(errorCode, message);
    }
}
