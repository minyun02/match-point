package com.minsproject.matchpoint.dto.request;


import com.minsproject.matchpoint.constant.type.SportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchListRequest {
    private Long userId;
    private SportType sportType;
    private String sort;
    private Long lastId;
    private Integer pageSize;
}