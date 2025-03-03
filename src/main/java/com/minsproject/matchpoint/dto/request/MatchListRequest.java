package com.minsproject.matchpoint.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchListRequest {
    private Long userId;
    private String sportType;
    private String sort;
    private Long lastId;
    private Integer pageSize;
}